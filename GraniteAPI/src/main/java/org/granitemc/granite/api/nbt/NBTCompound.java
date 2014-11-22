package org.granitemc.granite.api.nbt;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.google.common.collect.Maps;
import org.granitemc.granite.api.Granite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBTCompound {

    private Map<String, Object> nbtMap;

    public NBTCompound() {
        nbtMap = Maps.newHashMap();
    }

    public NBTCompound(Map<String, Object> map) {
        nbtMap = map;
    }

    public Byte getByte(String key) {
        if (!hasKey(key)) return null;
        return (byte) nbtMap.get(key);
    }

    public byte[] getByteArray(String key) {
        if (!hasKey(key)) return null;
        return (byte[]) nbtMap.get(key);
    }

    public Double getDouble(String key) {
        if (!hasKey(key)) return null;
        return (double) nbtMap.get(key);
    }

    public String getEnd(String key) {
        if (!hasKey(key)) return null;
        return (String) nbtMap.get(key);
    }

    public Float getFloat(String key) {
        if (!hasKey(key)) return null;
        return (float) nbtMap.get(key);
    }

    public Integer getInt(String key) {
        if (!hasKey(key)) return null;
        return (int) nbtMap.get(key);
    }

    public int[] getIntArray(String key) {
        if (!hasKey(key)) return null;
        return (int[]) nbtMap.get(key);
    }

    public List getList(String key) {
        if (!hasKey(key)) return null;
        return (List) nbtMap.get(key);
    }

    public Long getLong(String key) {
        if (!hasKey(key)) return null;
        return (long) nbtMap.get(key);
    }

    public Short getShort(String key) {
        if (!hasKey(key)) return null;
        return (short) nbtMap.get(key);
    }

    public String getString(String key) {
        if (!hasKey(key)) return null;
        return (String) nbtMap.get(key);
    }

    public NBTCompound getNBTCompound(String key) {
        if (!hasKey(key)) return null;
        return (NBTCompound) nbtMap.get(key);
    }

    public void setByte(String key, Byte data) {
        nbtMap.put(key, data);
    }

    public void setByteArray(String key, Byte[] data) {
        nbtMap.put(key, data);
    }

    public void setDouble(String key, Double data) {
        nbtMap.put(key, data);
    }

    public void setEnd(String key) {
        nbtMap.put(key, "END");
    }

    public void setFloat(String key, Float data) {
        nbtMap.put(key, data);
    }

    public void setInt(String key, Integer data) {
        nbtMap.put(key, data);
    }

    public void setIntArray(String key, Integer[] data) {
        nbtMap.put(key, data);
    }

    public void setList(String key, List data) {
        nbtMap.put(key, data);
    }

    public void setLong(String key, Long data) {
        nbtMap.put(key, data);
    }

    public void setShort(String key, Short data) {
        nbtMap.put(key, data);
    }

    public void setString(String key, String data) {
        nbtMap.put(key, data);
    }

    public void setNBTCompound(String key, NBTCompound data) {
        nbtMap.put(key, data);
    }

    public void removeNBTTag(String key) {
        if (nbtMap.containsKey(key)) {
            nbtMap.remove(key);
        }
    }

    public boolean hasKey(String key) {
        return nbtMap.containsKey(key);
    }

    public Map<String, Object> getNBTMap() {
        Map<String, Object> newMap = new HashMap<>(nbtMap);
        for (Map.Entry<String, Object> entry : newMap.entrySet()) {
            if (entry.getValue() instanceof NBTCompound) {
                newMap.put(entry.getKey(), ((NBTCompound) entry.getValue()).getNBTMap());
            }
        }
        return newMap;
    }

    public void setNBTMap(Map<String, Object> nbtMap) {
        this.nbtMap = nbtMap;
    }

    @Override
    public String toString() {
        String str = "{";

        for (Map.Entry<String, Object> entry : nbtMap.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Map) {
                val = new NBTCompound((Map<String, Object>) val);
            } else if (val instanceof String) {
                val = "\"" + ((String) val).replaceAll("\"", "\\\"") + "\"";
            }

            str += entry.getKey() + ":" + val.toString() + ",";
        }

        return str.substring(0, str.length() - 1) + "}";
    }

    public static NBTCompound fromString(String str) {
        return Granite.getAPIHelper().getNBTFromString(str);
    }
}
