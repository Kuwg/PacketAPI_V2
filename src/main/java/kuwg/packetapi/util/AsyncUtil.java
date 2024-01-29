package kuwg.packetapi.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class AsyncUtil {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static void completableFutureAsync(final Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }, executor);
    }

    public static ExecutorService getExecutor() {
        return executor;
    }
    public static void executorAsync(final Runnable runnable){
        executor.execute(runnable);
    }
    public static void threadAsync(Runnable runnable){
        new Thread(runnable).start();
    }
}
