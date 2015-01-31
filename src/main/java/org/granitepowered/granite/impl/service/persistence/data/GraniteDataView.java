/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.impl.service.persistence.data;

import com.google.common.base.Optional;
import org.spongepowered.api.service.persistence.DataSerializable;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.service.persistence.data.DataQuery;
import org.spongepowered.api.service.persistence.data.DataView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraniteDataView implements DataView {

    private final Optional<DataView> parent;
    private final Map<DataQuery, Object> data;
    private final DataQuery currentPath;

    public GraniteDataView(Optional<DataView> parent, DataQuery currentPath) {
        data = new HashMap<>();
        this.parent = parent;
        this.currentPath = currentPath;
    }

    @Override
    public DataContainer getContainer() {
        return null;
    }

    @Override
    public DataQuery getCurrentPath() {
        return currentPath;
    }

    @Override
    public String getName() {
        return currentPath.getParts().get(currentPath.getParts().size() - 1);
    }

    @Override
    public Optional<DataView> getParent() {
        return parent;
    }

    @Override
    public Set<DataQuery> getKeys(boolean deep) {
        if (!deep) {
            return data.keySet();
        }

        Set<DataQuery> keys = new HashSet<>(data.keySet());

        for (Map.Entry<DataQuery, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof DataView) {
                DataView v = (DataView) entry.getValue();

                for (DataQuery key : v.getKeys(true)) {
                    keys.add(currentPath.then(key));
                    // TODO: Get the seperator properly
                }
            }
        }
        return keys;
    }

    @Override
    public Map<DataQuery, Object> getValues(boolean deep) {
        if (!deep) {
            return data;
        }

        Map<DataQuery, Object> retData = new HashMap<>(data);

        for (Map.Entry<DataQuery, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof DataView) {
                DataView v = (DataView) entry.getValue();

                for (Map.Entry<DataQuery, Object> entry2 : v.getValues(true).entrySet()) {
                    retData.put(currentPath.then(entry2.getKey()), entry2.getValue());
                    // TODO: Get the seperator properly
                }
            }
        }
        return retData;
    }

    @Override
    public boolean contains(DataQuery path) {
        return false;
    }

    @Override
    public Optional<Object> get(DataQuery path) {
        return null;
    }

    @Override
    public void set(DataQuery path, Object value) {

    }

    @Override
    public void remove(DataQuery path) {

    }

    @Override
    public DataView createView(DataQuery path) {
        return null;
    }

    @Override
    public DataView createView(DataQuery path, Map<?, ?> map) {
        return null;
    }

    @Override
    public Optional<DataView> getView(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Boolean> getBoolean(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Integer> getInt(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Long> getLong(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Double> getDouble(DataQuery path) {
        return null;
    }

    @Override
    public Optional<String> getString(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<?>> getList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<String>> getStringList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Character>> getCharacterList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Boolean>> getBooleanList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Byte>> getByteList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Short>> getShortList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Integer>> getIntegerList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Long>> getLongList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Float>> getFloatList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Double>> getDoubleList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Map<?, ?>>> getMapList(DataQuery path) {
        return null;
    }

    @Override
    public <T extends DataSerializable> Optional<T> getSerializable(DataQuery path, Class<T> clazz) {
        return null;
    }
}
