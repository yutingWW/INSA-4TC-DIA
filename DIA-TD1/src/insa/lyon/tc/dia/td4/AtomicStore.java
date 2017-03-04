package insa.lyon.tc.dia.td4;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Created by cajus on 04.03.17.
 */
public class AtomicStore implements Store {

    private final int size;
    private final AtomicLongArray atomicValues;

    AtomicStore(int size) {
        this.size = size;
        atomicValues = new AtomicLongArray(size);
    }

    @Override
    public String name() {
        return "Atomic";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public long at(int index) {
        return atomicValues.get(index);
    }

    @Override
    public void add(int index, long amount) {
        atomicValues.getAndAdd(index, amount);
    }

    @Override
    public void substract(int index, long amount) {
        boolean updated = false;
        while (!updated) {
            long soll = atomicValues.get(index);
            updated = atomicValues.compareAndSet(index, soll, soll-amount);
        }
    }


}
