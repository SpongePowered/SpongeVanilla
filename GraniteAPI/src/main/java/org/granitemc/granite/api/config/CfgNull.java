package org.granitemc.granite.api.config;

public class CfgNull extends CfgValue {
    public static CfgValue instance;
    static {
        instance = new CfgNull();
    }

    private CfgNull() {}

    @Override
    public Object unwrap() {
        return null;
    }

    @Override
    public CfgValueType getType() {
        return CfgValueType.NULL;
    }
}
