// -*- cf-file-processor: index; flycheck-checker: javacf -*-

import org.checkerframework.checker.index.qual.*;
import org.checkerframework.common.value.qual.*;
import java.util.*;

class IndexTest {
    int[] c;
    List<String> q;
    void test(String s) {
        // LengthOf, IndexFor

        // IndexOrHigh, for loops
        for (int i = 0; i < c.length; i++) {
            (c[i] + "").toString();
        }

        // MinLen
        @MinLen(5) int[] d = new int[5];
        ("" + d[3]).toString();

        // SameLen

        // IndexOrLow
        @IndexOrLow("q") int k = q.indexOf("hi");
        if (k >= 0) {
            q.get(k);
        }

        // Arrays.binarySearch, SearchIndexFor
        @SearchIndexFor("c") int j = Arrays.binarySearch(c, 3);
        if (j >= 0) {
            @IndexFor("c") int j2 = j;
        }
    }
}
