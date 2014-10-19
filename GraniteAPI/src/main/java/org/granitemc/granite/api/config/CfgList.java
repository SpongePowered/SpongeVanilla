package org.granitemc.granite.api.config;

import java.util.*;

@SuppressWarnings("unchecked")
public class CfgList extends CfgCollection implements List<CfgValue> {
    List<CfgValue> value;

    public CfgList(List<CfgValue> list) {
        value = list;
    }

    public CfgList() {
        value = new ArrayList<>();
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
    public boolean contains(Object o) {
        return value.contains(o);
    }

    @Override
    public Iterator<CfgValue> iterator() {
        return value.iterator();
    }

    @Override
    public Object[] toArray() {
        return value.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return value.toArray(a);
    }

    @Override
    public boolean add(CfgValue cfgValue) {
        return value.add(cfgValue);
    }

    @Override
    public boolean remove(Object o) {
        return value.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return value.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends CfgValue> c) {
        return value.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends CfgValue> c) {
        return value.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return value.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return value.retainAll(c);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public CfgValue get(int index) {
        return value.get(index);
    }

    @Override
    public CfgValue set(int index, CfgValue element) {
        return value.set(index, element);
    }

    @Override
    public void add(int index, CfgValue element) {
        value.set(index, element);
    }

    @Override
    public CfgValue remove(int index) {
        return value.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return value.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return value.lastIndexOf(o);
    }

    @Override
    public ListIterator<CfgValue> listIterator() {
        return value.listIterator();
    }

    @Override
    public ListIterator<CfgValue> listIterator(int index) {
        return value.listIterator(index);
    }

    @Override
    public List<CfgValue> subList(int fromIndex, int toIndex) {
        return value.subList(fromIndex, toIndex);
    }

    @Override
    CfgValue getSegment(String name) {
        int i = Integer.parseInt(name);
        return value.get(i);
    }

    @Override
    void putSegment(String name, CfgValue value) {
        int i = Integer.parseInt(name);
        this.value.set(i, value);
    }

    @Override
    public Object unwrap() {
        List<Object> new_ = new ArrayList<>();
        for (CfgValue obj : value) {
            new_.add(obj.unwrap());
        }
        return new_;
    }

    @Override
    public CfgValueType getType() {
        return CfgValueType.LIST;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CfgList cfgValues = (CfgList) o;

        if (!value.equals(cfgValues.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CfgList{" +
                "value=" + value +
                '}';
    }
}
