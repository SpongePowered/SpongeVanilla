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
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.service.persistence.DataSerializable;
import org.spongepowered.api.service.persistence.SerializationService;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.service.persistence.data.DataQuery;
import org.spongepowered.api.service.persistence.data.DataView;

import java.util.*;

public class GraniteDataView implements DataView {

    protected DataContainer container;
    private Map<String, Object> children;
    private DataQuery currentPath;
    private Optional<DataView> parent;

    public GraniteDataView(Map<String, Object> data, DataQuery currentPath, DataContainer container) {
        this.currentPath = currentPath;
        this.container = container;

        children = data;

        if (currentPath.getParts().size() > 1) {
            // Using getContainer() here so DataContainer can override
            parent = Optional.fromNullable((DataView) getContainer().get(currentPath).orNull());
        } else {
            parent = Optional.absent();
        }
    }

    @Override
    public DataContainer getContainer() {
        return container;
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
    public Set<DataQuery> getKeys(boolean b) {
        Set<DataQuery> keys = new HashSet<>();

        for (Map.Entry<String, Object> key : children.entrySet()) {
            keys.add(new DataQuery(key.getKey()));

            if (b) {
                if (key.getValue() instanceof DataView) {
                    for (DataQuery keyy : ((DataView) key.getValue()).getKeys(true)) {
                        keys.add(new DataQuery(key.getKey()).then(keyy));
                    }
                }
            }
        }
        return keys;
    }

    @Override
    public Map<DataQuery, Object> getValues(boolean b) {
        Map<DataQuery, Object> values = new HashMap<>();

        for (Map.Entry<String, Object> key : children.entrySet()) {
            values.put(new DataQuery(key.getKey()), key.getValue());

            if (b) {
                if (key.getValue() instanceof DataView) {
                    for (Map.Entry<DataQuery, Object> entry : ((DataView) key.getValue()).getValues(true).entrySet()) {
                        values.put(new DataQuery(key.getKey()).then(entry.getKey()), entry.getValue());
                    }
                }
            }
        }

        return values;
    }

    @Override
    public boolean contains(DataQuery dataQuery) {
        if (dataQuery.getParts().size() == 1) {
            return children.containsKey(dataQuery.getParts().get(0));
        } else {
            if (!children.containsKey(dataQuery.getParts().get(0))) {
                return false;
            }

            List<String> dataQueries = new ArrayList<>(dataQuery.getParts());
            dataQueries.remove(0);

            return ((DataView) children.get(dataQuery.getParts().get(0))).contains(new DataQuery(dataQueries));
        }
    }

    @Override
    public Optional<Object> get(DataQuery dataQuery) {
        if (dataQuery.getParts().size() == 1) {
            return Optional.fromNullable(children.get(dataQuery.getParts().get(0)));
        } else {
            if (!children.containsKey(dataQuery.getParts().get(0))) {
                return Optional.absent();
            }

            List<String> dataQueries = new ArrayList<>(dataQuery.getParts());
            dataQueries.remove(0);

            return ((DataView) children.get(dataQuery.getParts().get(0))).get(new DataQuery(dataQueries));
        }
    }

    @Override
    public void set(DataQuery dataQuery, Object o) {
        if (dataQuery.getParts().size() == 1) {
            children.put(dataQuery.getParts().get(0), o);
        } else {
            List<String> dataQueries = new ArrayList<>(dataQuery.getParts());
            dataQueries.remove(0);

            ((DataView) children.get(dataQuery.getParts().get(0))).set(new DataQuery(dataQueries), o);
        }

        if (o instanceof GraniteDataView) {
            List<String> dataQueries = new ArrayList<>(dataQuery.getParts());

            ((GraniteDataView) o).currentPath = new DataQuery(dataQueries);
        }
    }

    @Override
    public void remove(DataQuery dataQuery) {
        if (dataQuery.getParts().size() == 1) {
            children.remove(dataQuery.getParts().get(0));
        } else {
            List<String> dataQueries = new ArrayList<>(dataQuery.getParts());
            dataQueries.remove(0);

            ((DataView) children.get(dataQuery.getParts().get(0))).remove(new DataQuery(dataQueries));
        }
    }

    @Override
    public DataView createView(DataQuery dataQuery) {
        return createView(dataQuery, new HashMap<>());
    }

    @Override
    public DataView createView(DataQuery dataQuery, Map<?, ?> map) {
        List<String> dataQueries = new ArrayList<>(dataQuery.getParts());
        dataQueries.remove(dataQueries.size() - 1);

        DataView view = new GraniteDataView((Map<String, Object>) map, currentPath.then(dataQuery).then(new DataQuery(dataQueries)), container);
        set(dataQuery, view);

        return view;
    }

    @Override
    public Optional<DataView> getView(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof DataView) {
            return Optional.fromNullable((DataView) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<Boolean> getBoolean(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof Boolean) {
            return Optional.fromNullable((Boolean) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<Integer> getInt(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof Integer) {
            return Optional.fromNullable((Integer) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<Long> getLong(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof Long) {
            return Optional.fromNullable((Long) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<Double> getDouble(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof Double) {
            return Optional.fromNullable((Double) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<String> getString(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof String) {
            return Optional.fromNullable((String) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<?>> getList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof DataView) {
            return Optional.<List<?>>fromNullable((List<?>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<String>> getStringList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<String>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Character>> getCharacterList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Character>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Boolean>> getBooleanList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Boolean>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Byte>> getByteList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Byte>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Short>> getShortList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Short>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Integer>> getIntegerList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Integer>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Long>> getLongList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Long>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Float>> getFloatList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Float>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Double>> getDoubleList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Double>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<Map<?, ?>>> getMapList(DataQuery dataQuery) {
        if (get(dataQuery).isPresent() && get(dataQuery).get() instanceof List) {
            return Optional.fromNullable((List<Map<?, ?>>) get(dataQuery).orNull());
        }
        return Optional.absent();
    }

    @Override
    public Optional<List<DataView>> getViewList(DataQuery dataQuery) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends DataSerializable> Optional<T> getSerializable(DataQuery dataQuery, Class<T> aClass, SerializationService serializationService) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends DataSerializable> Optional<List<T>> getSerializableList(DataQuery dataQuery, Class<T> aClass, SerializationService serializationService) {
        throw new NotImplementedException("");
    }
}
