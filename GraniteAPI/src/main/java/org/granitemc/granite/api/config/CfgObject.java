package org.granitemc.granite.api.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CfgObject extends CfgCollection implements Map<String, CfgValue> {
    Map<String, CfgValue> value;
    CfgObject defaults;

    public CfgObject() {
        value = new HashMap<>();
    }

    public CfgObject(Map<String, CfgValue> value) {
        this.value = value;
    }

    @Override
    CfgValue getSegment(String name) {
        return value.get(name);
    }

    @Override
    void putSegment(String name, CfgValue value) {
        this.value.put(name, value);
    }

    @Override
    public Object unwrap() {
        Map<String, Object> new_ = new HashMap<>();
        for (Map.Entry<String, CfgValue> entry : value.entrySet()) {
            new_.put(entry.getKey(), entry.getValue().unwrap());
        }
        return new_;
    }

    @Override
    public CfgValueType getType() {
        return CfgValueType.OBJECT;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return value.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public CfgValue get(Object key) {
        CfgValue ret = super.get((String) key);
        if (ret == null) {
            if (defaults == null) defaults = new CfgObject();
            ret = defaults.get(key);
        }
        return ret;
    }

    public CfgValue get(String path) {
        return get((Object) path);
    }

    public CfgValue put(String key, Object value) {
        return put(key, CfgValue.fromObject(value));
    }

    @Override
    public CfgValue remove(Object key) {
        return value.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends CfgValue> m) {
        value.putAll(m);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    @Override
    public Collection<CfgValue> values() {
        return value.values();
    }

    @Override
    public Set<Entry<String, CfgValue>> entrySet() {
        return value.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CfgObject cfgObject = (CfgObject) o;

        if (!value.equals(cfgObject.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CfgObject{" +
                "value=" + value +
                '}';
    }

    public void addDefault(String path, CfgValue value) {
        if (defaults == null) defaults = new CfgObject();
        defaults.put(path, value);
    }

    public void addDefault(String path, Object value) {
        addDefault(path, CfgValue.fromObject(value));
    }
}