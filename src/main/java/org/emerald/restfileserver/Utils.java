package org.emerald.restfileserver;

import java.util.concurrent.Callable;

public final class Utils {
    private Utils() {}

    public static <T> T silently(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
