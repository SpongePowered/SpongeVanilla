package org.granitemc.granite.api.nbt;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class GraniteNBT {

    private Map<String, Object> nbtMap;

    public GraniteNBT() {
        nbtMap = Maps.newHashMap();
    }

    public GraniteNBT(Map<String, Object> map) {
        nbtMap = map;
    }

    public void addByteNBT(String key, Byte data) {
        nbtMap.put(key, data);
    }

    public void addByteArrayNBT(String key, Byte[] data) {
        nbtMap.put(key, data);
    }

    public void addDoubleNBT(String key, Double data) {
        nbtMap.put(key, data);
    }

    public void addEndNBT(String key) {
        nbtMap.put(key, "END");
    }

    public void addFloatNBT(String key, Float data) {
        nbtMap.put(key, data);
    }

    public void addIntegerNBT(String key, Integer data) {
        nbtMap.put(key, data);
    }

    public void addIntegerArrayNBT(String key, Integer[] data) {
        nbtMap.put(key, data);
    }

    public void addListNBT(String key, List data) {
        nbtMap.put(key, data);
    }

    public void addLongNBT(String key, Long data) {
        nbtMap.put(key, data);
    }

    public void addShortNBT(String key, Short data) {
        nbtMap.put(key, data);
    }

    public void addStringNBT(String key, String data) {
        nbtMap.put(key, data);
    }

    public void addGraniteNBT(String key, GraniteNBT data) {
        nbtMap.put(key, data);
    }

    public void removeNBTTag(String key) {
        if (nbtMap.containsKey(key)) {
            nbtMap.remove(key);
        }
    }

    public Map<String, Object> getNbtMap() {
        return nbtMap;
    }

    public void setNbtMap(Map<String, Object> nbtMap) {
        this.nbtMap = nbtMap;
    }
}
