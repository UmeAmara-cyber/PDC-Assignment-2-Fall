
public class CacheLocalityBenchmark {
    private static final int SIZE = 2000;
    private static final int ITERATIONS = 10;

    private static double[][] matrix = new double[SIZE][SIZE];
    private static volatile double result;

    public static double traverseRowMajor() {
        double sum = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sum += matrix[i][j];
            }
        }

        return sum;
    }

    public static double traverseColumnMajor() {
        double sum = 0;

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                sum += matrix[i][j];
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = i + j;
            }
        }

        // Warm-up before measuring
        result = traverseRowMajor();
        result = traverseColumnMajor();

        long rowStart = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            result = traverseRowMajor();
        }

        long rowEnd = System.nanoTime();

        long columnStart = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            result = traverseColumnMajor();
        }

        long columnEnd = System.nanoTime();

        double rowTimeMs =
            (rowEnd - rowStart) / 1_000_000.0 / ITERATIONS;

        double columnTimeMs =
            (columnEnd - columnStart) / 1_000_000.0 / ITERATIONS;

        double speedup = columnTimeMs == 0
            ? 0 : rowTimeMs / columnTimeMs;

        System.out.printf("Matrix Size: %d x %d%n", SIZE, SIZE);
        System.out.printf("Iterations: %d%n", ITERATIONS);
        System.out.printf("Average Row-Major Time: %.3f ms%n", rowTimeMs);
        System.out.printf("Average Column-Major Time: %.3f ms%n", columnTimeMs);
        System.out.printf("Row/Column Speedup Ratio: %.3f%n", speedup);
    }
}
