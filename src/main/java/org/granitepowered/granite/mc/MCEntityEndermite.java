package org.granitepowered.granite.mc;

@Implement(name = "EntityEndermite")
public interface MCEntityEndermite extends MCEntityMob {

    boolean fieldGet$playerSpawned();

    void fieldSet$playerSpawned(boolean playerCreated);
}
