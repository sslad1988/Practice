import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JavaLocks {

	public Object mutex = new Object();
	Queue<Integer> list = new LinkedList<Integer>();

	public static void main(String args[]) throws InterruptedException {
		JavaLocks jl = new JavaLocks();
		ReentrantLock lock = new ReentrantLock(true);
		Thread p = new Thread(jl.new Producer(lock));
		Thread c = new Thread(jl.new Consumer(lock));

		p.start();
		c.start();

		p.join();
		c.join();
	}

	private class Producer implements Runnable {
		Lock lock;

		public Producer(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			int counter = 0;
			while (true) {

				lock.lock();

				System.out.println("produce " + counter);
				list.add(counter++);
				lock.unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class Consumer implements Runnable {

		Lock lock;

		public Consumer(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {

			while (true) {
				System.out.println("consumer waiting");
				lock.lock();
				int value = list.poll();

				System.out.println("consume " + value);
				lock.unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
