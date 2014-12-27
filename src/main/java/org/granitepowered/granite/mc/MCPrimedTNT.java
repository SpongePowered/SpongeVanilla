package org.granitepowered.granite.mc;

@Implement(name = "EntityTNTPrimed")
public interface MCPrimedTNT extends MCInterface, MCEntity {

    MCEntityLiving fieldGet$tntTriggeredBy();

    int fieldGet$fuse();

    void fieldSet$fuse(int ticks);

}
