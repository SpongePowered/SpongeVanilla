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

    public byte getByte(String key) {
        if (!hasKey(key)) return Byte.parseByte(null);
        return (byte) nbtMap.get(key);
    }

    public byte[] getByteArray(String key) {
        if (!hasKey(key)) return null;
        return (byte[]) nbtMap.get(key);
    }

    public double getDouble(String key) {
        if (!hasKey(key)) return Double.parseDouble(null);
        return (double) nbtMap.get(key);
    }

    public String getEnd(String key) {
        if (!hasKey(key)) return null;
        return (String) nbtMap.get(key);
    }

    public float getFloat(String key) {
        if (!hasKey(key)) return Float.parseFloat(null);
        return (float) nbtMap.get(key);
    }

    public int getInt(String key) {
        if (!hasKey(key)) return Integer.parseInt(null);
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

    public long getLong(String key) {
        if (!hasKey(key)) return Long.parseLong(null);
        return (long) nbtMap.get(key);
    }

    public short getShort(String key) {
        if (!hasKey(key)) return Short.parseShort(null);
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
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setByteArray(String key, Byte[] data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setDouble(String key, Double data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setEnd(String key) {
        if (hasKey(key)) return;
        nbtMap.put(key, "END");
    }

    public void setFloat(String key, Float data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setInt(String key, Integer data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setIntArray(String key, Integer[] data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setList(String key, List data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setLong(String key, Long data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setShort(String key, Short data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setString(String key, String data) {
        if (hasKey(key)) return;
        nbtMap.put(key, data);
    }

    public void setNBTCompound(String key, org.granitemc.granite.api.nbt.NBTCompound data) {
        if (hasKey(key)) return;
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
        return nbtMap;
    }

    public void setNBTMap(Map<String, Object> nbtMap) {
        this.nbtMap = nbtMap;
    }
}
