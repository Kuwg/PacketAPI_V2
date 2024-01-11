package kuwg.packetapi.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUtil {

    private static final ExecutorService executor = Executors.newFixedThreadPool(100);
    public static void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, executor);
    }
}
