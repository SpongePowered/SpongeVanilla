package net.minecraftforge.common.chunkio;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // Sponge
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.common.SpongeImpl; // Sponge
//import net.minecraftforge.fml.common.FMLLog; // Sponge

public class ChunkIOExecutor
{
    private static final int BASE_THREADS = 1;
    private static final int PLAYERS_PER_THREAD = 50;

    private static final AtomicInteger threadCounter = new AtomicInteger(); // Sponge: Add static thread counter

    private static final Map<QueuedChunk, ChunkIOProvider> tasks = new ConcurrentHashMap<>(); // Sponge: Construct ConcurrentHashMap directly
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(BASE_THREADS, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            /*new ThreadFactory()
            {
                private AtomicInteger count = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r)
                {
                    Thread thread = new Thread(r, "Chunk I/O Executor Thread-" + count.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            }*/
            // Sponge start: Use lambda
            r -> {
                Thread thread = new Thread(r, "Chunk I/O Thread #" + threadCounter.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
            // Sponge end
    );

    //Load the chunk completely in this thread. Dequeue as needed...
    public static Chunk syncChunkLoad(World world, AnvilChunkLoader loader, ChunkProviderServer provider, int x, int z)
    {
        QueuedChunk key = new QueuedChunk(x, z, world);
        ChunkIOProvider task = tasks.remove(key); // Remove task because we will call the sync callbacks directly
        if (task != null)
        {
            if (!pool.remove(task)) // If it wasn't in the pool, and run hasn't finished, then wait for the async thread.
            {
                synchronized(task)
                {
                    while (!task.runFinished())
                    {
                        try
                        {
                            task.wait();
                        }
                        catch (InterruptedException e)
                        {
                            // Sponge start: Rethrow interruption
                            //e.printStackTrace(); // Something happened? Log it?
                            Thread.currentThread().interrupt();
                            throw new RuntimeException("Failed to wait for chunk load", e);
                            // Sponge end
                        }
                    }
                }
            }
            else
            {
                // If the task was not run yet we still need to load the chunk
                task.run();
            }
        }
        else
        {
            task = new ChunkIOProvider(key, loader, provider);
            task.run();
        }
        task.syncCallback();
        return task.getChunk();
    }

    //Queue the chunk to be loaded, and call the runnable when finished
    // Sponge: Runnable -> Consumer<Chunk>
    public static void queueChunkLoad(World world, AnvilChunkLoader loader, ChunkProviderServer provider, int x, int z, Consumer<Chunk> runnable)
    {
        QueuedChunk key = new QueuedChunk(x, z, world);
        ChunkIOProvider task = tasks.get(key);
        if (task == null)
        {
            task = new ChunkIOProvider(key, loader, provider);
            task.addCallback(runnable); // Add before calling execute for thread safety
            tasks.put(key, task);
            pool.execute(task);
        }
        else
        {
            task.addCallback(runnable);
        }
    }

    // Remove the chunk from the queue if it's in the list.
    public static void dropQueuedChunkLoad(World world, int x, int z, Consumer<Chunk> runnable) // Sponge: Runnable -> Consumer<Chunk>
    {
        QueuedChunk key = new QueuedChunk(x, z, world);
        ChunkIOProvider task = tasks.get(key);
        if (task == null)
        {
            // Sponge: Use Sponge logging
            //FMLLog.warning("Attempted to dequeue chunk that wasn't queued? %d @ (%d, %d)", world.provider.getDimension(), x, z);
            SpongeImpl.getLogger().warn("Attempting to drop chunk that wasn't queued in {} @ ({}, {})", world, x, z);
            return;
        }

        task.removeCallback(runnable);

        if (!task.hasCallback())
        {
            tasks.remove(key);
            pool.remove(task);
        }
    }

    public static void adjustPoolSize(int players)
    {
        pool.setCorePoolSize(Math.max(BASE_THREADS, players / PLAYERS_PER_THREAD));
    }

    public static void tick()
    {
        Iterator<ChunkIOProvider> itr = tasks.values().iterator();
        while (itr.hasNext())
        {
            ChunkIOProvider task = itr.next();
            if (task.runFinished())
            {
                if (task.hasCallback())
                    task.syncCallback();

                itr.remove();
            }
        }
    }
}
