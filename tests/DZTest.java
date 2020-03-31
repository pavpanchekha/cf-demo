// -*- cf-file-processor: divbyzero.DivByZeroChecker; flycheck-checker: javacf -*-

import divbyzero.qual.*;

class DZTest {
    static void test(@NonNegative int x) {
        int k;

        @NonNegative int z = 0;
        @Positive int p = 1;
        k = 1 / z;
        k = 1 / p;
        k = 1 / x;

        k = 1 / (0 + 0);
        k = 1 / (0 + 1);


        if (x == 0) {
            k = 1 / (x + 1);
        } else {
            k = 1 / x;
        }
    }
}
