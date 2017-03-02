package insa.lyon.tc.dia.td4;

/**
 * Created by cajus on 02.03.17.
 */

public class MerkelStore implements Store {

    private final int size;
    private final long[] values;

    MerkelStore(int size){
        this.size = size;
        values = new long[size];
    }

    @Override
    public String name() {
        return "MerkelStore";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public synchronized long at(int index) {
        return values[index];
    }

    @Override
    public synchronized void add(int index, long amount) {
        values[index] += amount;
    }

    @Override
    public synchronized void substract(int index, long amount) {
        values[index] -= amount;
    }
}
