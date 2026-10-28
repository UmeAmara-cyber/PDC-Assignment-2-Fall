import java.util.concurrent.*;

public class SensorGridMonitorApp {

    public static void main(String[] args) throws Exception {
        double[] sensorData = new double[50000];

        for (int i = 0; i < sensorData.length; i++) {
            sensorData[i] = (i * 17 % 10000) / 10.0;
        }

        int mid = sensorData.length / 2;
        int quarter = sensorData.length / 4;
        ExecutorService pool = Executors.newFixedThreadPool(4);

        long startTime = System.nanoTime();

        Future<Double> min1 = pool.submit(() ->
            new ParallelMinSearch(sensorData, 0, quarter)
                .processGrid());

        Future<Double> min2 = pool.submit(() ->
            new ParallelMinSearch(sensorData, quarter, mid)
                .processGrid());

        Future<Double> max1 = pool.submit(() ->
            new ParallelMaxSearch(sensorData, mid, mid + quarter)
                .processGrid());

        Future<Double> max2 = pool.submit(() ->
            new ParallelMaxSearch(sensorData, mid + quarter,
                sensorData.length).processGrid());

        double minValue = Math.min(min1.get(), min2.get());
        double maxValue = Math.max(max1.get(), max2.get());

        long endTime = System.nanoTime();

        System.out.printf("Minimum Sensor Value: %.2f%n", minValue);
        System.out.printf("Maximum Sensor Value: %.2f%n", maxValue);
        System.out.printf("Execution Time: %.3f ms%n",
            (endTime - startTime) / 1_000_000.0);

        pool.shutdown();
    }
}