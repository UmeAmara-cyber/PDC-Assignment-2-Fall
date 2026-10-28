import java.util.*;
import java.util.concurrent.*;

public class InventoryCatalogManager {

    private static final int THREADS = 10;
    private static final int OPERATIONS = 50000;

    public static void benchmarkSynchronizedMap() throws Exception {
        Map<String, Integer> stock =
            Collections.synchronizedMap(new HashMap<>());

        runBenchmark(stock, "SynchronizedMap");
    }

    public static void benchmarkConcurrentMap() throws Exception {
        Map<String, Integer> stock = new ConcurrentHashMap<>();

        runBenchmark(stock, "ConcurrentHashMap");
    }

    private static void runBenchmark(
            Map<String, Integer> stock, String mapName)
            throws Exception {

        for (int i = 0; i < 100; i++) {
            stock.put("P" + i, 100);
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        Future<?>[] futures = new Future<?>[THREADS];

        long startTime = System.nanoTime();

        for (int t = 0; t < THREADS; t++) {
            futures[t] = pool.submit(() -> {
                ready.countDown();

                try {
                    start.await();

                    for (int i = 0; i < OPERATIONS / THREADS; i++) {
                        String id = "P" + (i % 100);

                        Integer value = stock.get(id);

                        if (value != null) {
                            stock.put(id, value + 1);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        ready.await();
        start.countDown();

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.nanoTime();
        pool.shutdown();

        double seconds = (endTime - startTime) / 1_000_000_000.0;
        double throughput = OPERATIONS / seconds;

        System.out.println("\nCollection: " + mapName);
        System.out.printf("Operations: %d%n", OPERATIONS);
        System.out.printf("Execution Time: %.3f ms%n",
            seconds * 1000);
        System.out.printf("Throughput: %.2f operations/second%n",
            throughput);
    }

    public static void main(String[] args) throws Exception {
        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();
    }
}