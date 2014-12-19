package org.granitepowered.granite.mc;

@Implement(name = "GameRules")
public interface MCGameRules extends MCInterface {

    String getGameRuleStringValue(String name);

    void setOrCreateGameRule(String key, String value);

    String[] getRules();

}
