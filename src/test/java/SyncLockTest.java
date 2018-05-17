import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized lock test
 * @author liliangang-1163
 * @date 2018/5/17 17:02
 * @see
 */
public class SyncLockTest {

	// synchronized对象锁
	private Object object = new Object();
	// lock对象锁
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	// 商品数量
	private int sku = 0;
	// 最大数量
	private int limit = 10;

	/**
	 * 测试synchronized版消费生产模式
	 * @date 2018/5/17 17:29
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws InterruptedException
	 */
	@Test
	public void testSynchronized() throws InterruptedException {
		// 生产线程
		Thread producer = new Thread(() -> {
			while (true) {
				synchronized (object) {
					if (sku >= limit) {
						print("当前商品已满,数量%s，暂停添加", sku);
						try {
							object.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					object.notify();
					++sku;
					print("添加一个商品，当前剩余数量为%s。", sku);
				}
			}
		}, "sync-producer");
		// 消费线程
		Thread consumer = new Thread(() -> {
			while (true) {
				synchronized (object) {
					if (sku == 0) {
						print("当前商品不足，数量为%s，暂停消费", sku);
						try {
							object.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					object.notify();
					--sku;
					print("消费一个商品，当前剩余数量为%s。", sku);
				}
			}
		}, "sync-consumer");
		producer.start();
		consumer.start();
		Thread.sleep(5);
	}

	/**
	 * 测试lock版消费生产模式
	 * @date 2018/5/17 17:29
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws InterruptedException
	 */
	@Test
	public void testLock() throws InterruptedException {
		// 生产线程
		Thread producer = new Thread(() -> {
			while (true) {
				lock.lock();
				if (sku >= limit) {
					print("当前商品已满,数量%s，暂停添加", sku);
					try {
						condition.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				condition.signal();
				++sku;
				print("添加一个商品，当前剩余数量为%s。", sku);
				lock.unlock();
			}
		}, "lock-producer");
		// 消费线程
		Thread consumer = new Thread(() -> {
			while (true) {
				lock.lock();
				if (sku == 0) {
					print("当前商品不足，数量为%s，暂停消费", sku);
					try {
						condition.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				condition.signal();
				--sku;
				print("消费一个商品，当前剩余数量为%s。", sku);
				lock.unlock();
			}
		}, "lock-consumer");
		producer.start();
		consumer.start();
		Thread.sleep(5);
	}

	private void print(String str, int sku) {
		System.out.println(String.format("%s" + str, Thread.currentThread().getName(), sku));
	}
}
