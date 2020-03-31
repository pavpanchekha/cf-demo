// -*- cf-file-processor: nullness; flycheck-checker: javacf -*-

import org.checkerframework.checker.nullness.qual.*;
import java.util.*;

public class NullTest {
    @Nullable Object o = null;
    Map<String, Object> map;

    @Nullable Object method(@NonNull Object o) {
        return null;
    }

    void test() {
        if (this.o == null) {
        } else {
            @NonNull Object o2 = this.o;
            o.toString();
        }

        // Calling methods on `c` requires checking for null
        @Nullable Object p;
        p = method(o == null ? new Object() : o);

        // Creating maps and KeyFor annotations
        map = new TreeMap();
        String k = "hi";
        map.put(k, "hello");
        @KeyFor("map") String l2 = k;
        @NonNull Object q = method2(k);
    }

    @NonNull Object method2(@KeyFor("this.map") String key) {
        return map.get(key);
    }
}
