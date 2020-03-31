// -*- cf-file-processor: lock; flycheck-checker: javacf -*-

import org.checkerframework.checker.lock.qual.*;
import java.util.concurrent.locks.*;

class LockTest {
    final Lock lock;
    @GuardedBy("lock") Object o = new Object();

    LockTest() {
        lock = new ReentrantLock();
    }

    void test() {
        // Read from `o`

        // Lock `lock`
        
        // Release `lock`
    }

    // @LockingFree

    // @EnsuresLockHeld

    // @Holding
}
