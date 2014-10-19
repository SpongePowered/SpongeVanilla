package org.granitemc.granite.api.config;

import java.io.Serializable;

public class CfgPrimitive extends CfgValue {
    Serializable value;

    public CfgPrimitive(Serializable value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CfgPrimitive that = (CfgPrimitive) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Object unwrap() {
        return value;
    }

    @Override
    public CfgValueType getType() {
        if (value == null) {
            return CfgValueType.NULL;
        } else if (value instanceof String) {
            return CfgValueType.STRING;
        } else if (value instanceof Number) {
            return CfgValueType.NUMBER;
        } else if (value instanceof Boolean) {
            return CfgValueType.BOOLEAN;
        }
        return CfgValueType.NULL;
    }
}
