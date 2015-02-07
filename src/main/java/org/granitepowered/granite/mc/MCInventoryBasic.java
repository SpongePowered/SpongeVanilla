package org.granitepowered.granite.mc;

@Implement(name = "InventoryBasic")
public interface MCInventoryBasic {

    MCItemStack[] fieldGet$inventoryContents();

    void fieldSet$inventoryContents(MCItemStack[] inventory);
}
