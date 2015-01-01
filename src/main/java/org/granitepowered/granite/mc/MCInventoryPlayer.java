package org.granitepowered.granite.mc;

@Implement(name = "InventoryPlayer")
public interface MCInventoryPlayer extends MCInterface {

    MCItemStack getCurrentItem();

}
