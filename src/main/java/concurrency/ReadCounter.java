package concurrency;

import java.util.concurrent.atomic.AtomicInteger;

// Concurrency dùng để xử lý nhiều tác vụ đồng thời
// concurrency.ReadCounter là một lớp đơn giản để đếm số lần đọc trong môi trường đa luồng.

public class ReadCounter {
    // atomic counter là một biến đếm được sử dụng trong môi trường đa luồng mà không cần phải sử dụng synchronized hay locks.
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}