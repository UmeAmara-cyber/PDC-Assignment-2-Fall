import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        double[] assets = new double[10000];

        for (int i = 0; i < assets.length; i++) {
            assets[i] = 500 + (i % 1500);
        }

        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.nanoTime();

        pool.invoke(
            new PortfolioThresholdTask(
                assets, 0, assets.length, 1500
            )
        );

        double totalRisk = pool.invoke(
            new ParallelRiskCalculator(assets, 0, assets.length)
        );

        long endTime = System.nanoTime();

        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.printf("Total Asset Risk: %.2f%n", totalRisk);
        System.out.printf("Processing Time: %.3f ms%n", timeMs);

        pool.shutdown();
    }
}