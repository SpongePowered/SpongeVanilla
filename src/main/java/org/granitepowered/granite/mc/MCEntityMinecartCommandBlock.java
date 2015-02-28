package org.granitepowered.granite.mc;

@Implement(name = "EntityMinecartCommandBlock")
public interface MCEntityMinecartCommandBlock extends MCEntityMinecart {

    MCCommandBlockLogic fieldGet$commandLogic();
}
