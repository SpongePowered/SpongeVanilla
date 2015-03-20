package org.granitepowered.granite.mc;

@Implement(name = "CommandBlockLogic")
public interface MCCommandBlockLogic extends MCInterface {

    String fieldGet$commandStored();

    void fieldSet$commandStored(String command);

    String fieldGet$customName();

    void fieldSet$customName(String name);

    int fieldSet$successCount(int count);
}
