public class AsyncWebEngineApp {

    public static void main(String[] args) {
        System.out.println("Main Thread: " +
                           Thread.currentThread().getName());

        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");

        LogProcessorTask task = new LogProcessorTask();
        Thread logWorker = new Thread(task);
        logWorker.setName("Log-Worker");

        telemetry.start();
        logWorker.start();

        System.out.println("Both worker threads started.");
    }
}