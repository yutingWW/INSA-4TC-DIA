package insa.lyon.tc.dia.td4;

import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by cajus on 02.03.17.
 */
public class Runner {

    private final static int THREAD_COUNT = 4;
    private final static int JOB_COUNT = 50000;
    private final static int STORE_SIZE = 20;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    private final LinkedList<Callable<Void>> jobs = new LinkedList<>();

    void bench(final Store database) throws InterruptedException {

        for (int i = 0; i<JOB_COUNT; i++){
            final boolean adding = i % 2 == 0;
            jobs.add(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (int j = 0; j<STORE_SIZE; j++) {
                        if (adding) {
                            database.add(j, 1);
                        } else {
                            database.substract(j, 1);
                        }
                    }
                    return null;
                }
            });
        }

        long start = System.currentTimeMillis();
        executor.invokeAll(jobs);
        for (Future<Void> future : executor.invokeAll(jobs)) {
            while (!future.isDone()) {
                // loop!
            }
        }

        long stop = System.currentTimeMillis();
        jobs.clear();
        System.out.println(database.name() + ": " + (stop - start) + "ms");
        for (int i = 0; i < database.size(); i++) {
            System.out.print(database.at(i) + " ");
        }
        System.out.println();
        System.gc();

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runner runner = new Runner();

//        for (int i = 0; i < 2; i++) {
//            runner.bench(new HippieStore(STORE_SIZE));
//        }

        for (int i = 0; i < 2; i++) {
            runner.bench(new MerkelStore(STORE_SIZE));
        }

        for (int i = 0; i<2; i++) {
            runner.bench(new AtomicStore(STORE_SIZE));
        }
    }


}
