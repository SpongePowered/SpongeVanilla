package org.granitemc.granite.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableList<E> extends ArrayList<E> {
    private Listener listener;

    public static interface Listener {
        void update();
    }

    public ObservableList(Listener listener) {
        super();
        this.listener = listener;
    }

    public ObservableList(List<E> list, Listener listener) {
        super(list);
        this.listener = listener;
    }

    private void notifyUpdate() {
        listener.update();
    }

    @Override
    public E set(int index, E element) {
        E set = super.set(index, element);
        notifyUpdate();
        return set;
    }

    @Override
    public boolean add(E e) {
        boolean add = super.add(e);
        notifyUpdate();
        return add;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        notifyUpdate();
    }

    @Override
    public E remove(int index) {
        E remove = super.remove(index);
        notifyUpdate();
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);
        notifyUpdate();
        return remove;
    }

    @Override
    public void clear() {
        super.clear();
        notifyUpdate();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean b = super.addAll(c);
        notifyUpdate();
        return b;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean b = super.addAll(index, c);
        notifyUpdate();
        return b;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notifyUpdate();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = super.removeAll(c);
        notifyUpdate();
        return b;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean b = super.retainAll(c);
        notifyUpdate();
        return b;
    }
}
