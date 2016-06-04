package net.minecraftforge.common.chunkio;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.common.SpongeImpl; // Sponge
import org.spongepowered.server.world.chunkio.AsyncAnvilChunkLoader; // Sponge
//import net.minecraftforge.common.MinecraftForge; // Sponge
//import net.minecraftforge.event.world.ChunkDataEvent; // Sponge

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

class ChunkIOProvider implements Runnable
{
    private final QueuedChunk chunkInfo;
    private final AnvilChunkLoader loader;
    private final ChunkProviderServer provider;

    private Chunk chunk;
    private NBTTagCompound nbt;
    private final ConcurrentLinkedQueue<Consumer<Chunk>> callbacks = new ConcurrentLinkedQueue<>(); // Sponge: Runnable -> Consumer<Chunk>
    private boolean ran = false;

    ChunkIOProvider(QueuedChunk chunk, AnvilChunkLoader loader, ChunkProviderServer provider)
    {
        this.chunkInfo = chunk;
        this.loader = loader;
        this.provider = provider;
    }

    public void addCallback(Consumer<Chunk> callback) // Sponge: Runnable -> Consumer<Chunk>
    {
        this.callbacks.add(callback);
    }
    public void removeCallback(Consumer<Chunk> callback) // Sponge: Runnable -> Consumer<Chunk>
    {
        this.callbacks.remove(callback);
    }

    @Override
    public void run() // async stuff
    {
        synchronized(this)
        {
            //Object[] data = null; // Sponge
            try
            {
                // Sponge start: Use Sponge's async chunk load method
                //data = this.loader.loadChunk__Async(chunkInfo.world, chunkInfo.x, chunkInfo.z);
                this.nbt = AsyncAnvilChunkLoader.read(this.loader, this.chunkInfo.x, this.chunkInfo.z);
                if (this.nbt != null) {
                    this.chunk = this.loader.checkedReadChunkFromNBT(this.chunkInfo.world, this.chunkInfo.x, this.chunkInfo.z, this.nbt);
                }
                // Sponge end
            }
            catch (IOException e)
            {
                // Sponge: Use Sponge logging
                //e.printStackTrace();
                SpongeImpl.getLogger().error("Could not load chunk in {} @ ({}, {})", this.chunkInfo.world, this.chunkInfo.x, this.chunkInfo.z, e);
            }

            // Sponge start: data is not used
            /*if (data != null)
            {
                this.nbt   = (NBTTagCompound)data[1];
                this.chunk = (Chunk)data[0];
            }*/
            // Sponge end

            this.ran = true;
            this.notifyAll();
        }
    }

    // sync stuff
    public void syncCallback()
    {
        if (chunk == null)
        {
            this.runCallbacks();
            return;
        }

        // Load Entities
        // Sponge: Use Sponge's loadEntities method
        //this.loader.loadEntities(this.chunkInfo.world, this.nbt.getCompoundTag("Level"), this.chunk);
        AsyncAnvilChunkLoader.loadEntities(this.chunkInfo.world, this.chunk, this.nbt);

        // Sponge: Don't call Forge event
        //MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(this.chunk, this.nbt)); // Don't call ChunkDataEvent.Load async

        this.chunk.setLastSaveTime(provider.worldObj.getTotalWorldTime());
        this.provider.chunkGenerator.recreateStructures(this.chunk, this.chunkInfo.x, this.chunkInfo.z);

        provider.id2ChunkMap.put(ChunkPos.chunkXZ2Int(this.chunkInfo.x, this.chunkInfo.z), this.chunk);
        this.chunk.onChunkLoad();
        this.chunk.populateChunk(provider, provider.chunkGenerator);

        this.runCallbacks();
    }

    public Chunk getChunk()
    {
        return this.chunk;
    }

    public boolean runFinished()
    {
        return this.ran;
    }

    public boolean hasCallback()
    {
        return this.callbacks.size() > 0;
    }

    public void runCallbacks()
    {
        for (Consumer<Chunk> r : this.callbacks) // Sponge: Runnable -> Consumer<Chunk>
        {
            // Sponge: Pass chunk argument
            //r.run();
            r.accept(this.chunk);
        }

        this.callbacks.clear();
    }
}
