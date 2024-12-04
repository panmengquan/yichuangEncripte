package example.utils;

import java.util.function.Supplier;

/**
 * @author QingBinGuo
 * @date 2023/3/26 17:09
 */
public class Assert {
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }
}
