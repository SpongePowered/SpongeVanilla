package org.granitemc.granite.api.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CfgObject extends CfgCollection implements Map<String, CfgValue> {
    Map<String, CfgValue> value;
    CfgObject defaults;

    Map<String, CfgValue> merged;

    public CfgObject() {
        value = new HashMap<>();
        calculateMerged();
    }

    private void calculateMerged() {
        merged = CfgValue.deepMerge(defaults, value);
    }

    public CfgObject(Map<String, CfgValue> value) {
        this.value = value;
        calculateMerged();
    }

    @Override
    CfgValue getSegment(String name) {
        return merged.get(name);
    }

    @Override
    void putSegment(String name, CfgValue value) {
        this.value.put(name, value);
        calculateMerged();
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
        return merged.size();
    }

    @Override
    public boolean isEmpty() {
        return merged.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return merged.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.merged.containsValue(value);
    }

    @Override
    public CfgValue get(Object key) {
        CfgValue ret = super.getValue((String) key);
        return ret;
    }

    public CfgValue getValue(String path) {
        return get((Object) path);
    }

    public CfgValue put(String key, Object value) {
        CfgValue ret = put(key, CfgValue.fromObject(value));
        calculateMerged();
        return ret;
    }

    @Override
    public CfgValue remove(Object key) {
        CfgValue ret = value.remove(key);
        calculateMerged();
        return ret;
    }

    @Override
    public void putAll(Map<? extends String, ? extends CfgValue> m) {
        value.putAll(m);
        calculateMerged();
    }

    @Override
    public void clear() {
        value.clear();
        calculateMerged();
    }

    @Override
    public Set<String> keySet() {
        return merged.keySet();
    }

    @Override
    public Collection<CfgValue> values() {
        return merged.values();
    }

    @Override
    public Set<Entry<String, CfgValue>> entrySet() {
        return merged.entrySet();
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
        calculateMerged();
    }

    public void addDefault(String path, Object value) {
        addDefault(path, CfgValue.fromObject(value));
        calculateMerged();
    }
}