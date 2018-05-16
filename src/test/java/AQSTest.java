import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;

/**
 * aqs test
 * @author liliangang-1163
 * @date 2018/5/15 13:06
 * @see
 */
public class AQSTest {

	@Before
	public void before() {
	}

	@Test
	public void testLock() throws InterruptedException {
		ReentrantLock lock = new ReentrantLock();
		Run run = new Run(lock);
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(run);
		threadPool.execute(run);
		threadPool.execute(run);
		threadPool.awaitTermination(1, TimeUnit.SECONDS);
		print(run.sum);
	}

	class Run implements Runnable {

		private Lock lock;
		private int sum;

		Run(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			int i = 0;
			while (i++ < 10000) {
				lock.lock();
				sum++;
				lock.unlock();
			}
			print(sum);
		}
	}

	private void print(Object object) {
		System.out.println(Thread.currentThread().getName() + ":" + object);
	}
}
