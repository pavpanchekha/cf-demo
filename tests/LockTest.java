// -*- cf-file-processor: lock; flycheck-checker: javacf -*-

import org.checkerframework.checker.lock.qual.*;
import java.util.concurrent.locks.*;

class LockTest {
    final Lock lock;
    @GuardedBy("lock") Object o = new Object();

    LockTest() {
        lock = new ReentrantLock();
    }

    @MayReleaseLocks void test() {
        // Read from `o`
        need_lock();
        lock();
        need_lock();
        o.toString();
        method();
        o.toString();
        lock.unlock();

        // Lock `lock`
        
        // Release `lock`
    }

    // @LockingFree
    @LockingFree void method() {
        
    }

    // @EnsuresLockHeld
    @EnsuresLockHeld("lock") void lock() {
        lock.lock();
    }

    // @Holding
    @Holding("lock") void need_lock() {
        o.toString();
    }
}
