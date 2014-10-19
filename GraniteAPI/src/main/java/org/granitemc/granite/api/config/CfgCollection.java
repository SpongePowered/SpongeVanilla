package org.granitemc.granite.api.config;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class CfgCollection extends CfgValue {
    abstract CfgValue getSegment(String name);

    abstract void putSegment(String name, CfgValue value);

    public CfgValue get(String path) {
        CfgValue currentValue = this;
        for (String pathSection : path.split("\\.")) {
            /*if (currentValue instanceof List) {
                int idx = Integer.parseInt(pathSection);
                currentValue = ((List<Object>) currentValue).get(idx);
            } else if (currentValue instanceof Map) {
                currentValue = ((Map<String, Object>) currentValue).get(pathSection);
            } else {
                throw new RuntimeException("Could not resolve path section " + pathSection + " - is a " + currentValue.getClass().getName() + ", not a Map or List");
            }*/
            currentValue = ((CfgCollection) currentValue).getSegment(pathSection);
        }
        return currentValue;
    }

    public String getString(String path) {
        return (String) get(path).unwrap();
    }

    public int getInteger(String path) {
        return (int) get(path).unwrap();
    }

    public double getDouble(String path) {
        return (double) get(path).unwrap();
    }

    public int getObject(String path) {
        return (int) get(path).unwrap();
    }

    public CfgList getList(String path) {
        return new CfgList((List<CfgValue>) get(path));
    }

    public CfgValue put(String key, CfgValue value) {
        if (key.contains(".")) {
            String collPath = key.substring(0, key.lastIndexOf('.'));
            String rest = key.substring(key.lastIndexOf('.') + 1, key.length());

            CfgCollection current = this;
            for (String pathSegment : collPath.split("\\.")) {
                if (current.getSegment(pathSegment) == null) {
                    current.putSegment(pathSegment, new CfgObject());
                }
                current = (CfgCollection) current.getSegment(pathSegment);
            }

            ((CfgCollection) get(collPath)).putSegment(rest, value);
        } else {
            putSegment(key, value);
        }
        return value;
    }
}
