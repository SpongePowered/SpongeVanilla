package org.granitepowered.granite.mc;

@Implement(name = "Chunk")
public interface MCChunk extends MCInterface {
    boolean fieldGet$isChunkLoaded();

    int fieldGet$xPosition();

    int fieldGet$zPosition();
}
