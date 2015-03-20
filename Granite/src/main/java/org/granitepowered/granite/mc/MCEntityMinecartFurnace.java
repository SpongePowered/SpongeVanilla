package org.granitepowered.granite.mc;

@Implement(name = "EntityMinecartFurnace")
public interface MCEntityMinecartFurnace extends MCEntityMinecartContainer {

    int fieldGet$fuel();

    void fieldSet$fuel(int fuel);
}
