import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerWithThreadPool {

	public Object mutex = new Object();
	Queue<Integer> list = new LinkedList<Integer>();

	public static void main(String args[]) throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(3);

		ProducerConsumerWithThreadPool pc = new ProducerConsumerWithThreadPool();
		service.execute(pc.new Producer());
		service.execute(pc.new Producer());
		service.execute(pc.new Consumer());

		service.shutdown();
	}

	private class Producer implements Runnable {
		@Override
		public void run() {
			int counter = 0;
			while (true) {
				try {
					synchronized (mutex) {
						while (list.size() >= 3) {
							System.out.println(Thread.currentThread().getId() + " : produce waiting");
							mutex.wait();
						}
						System.out.println(Thread.currentThread().getId() + " : produce " + counter);
						list.add(counter++);
						mutex.notifyAll();
						Thread.sleep(1000);
					}
				} catch (InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
			}
		}
	}

	private class Consumer implements Runnable {
		@Override
		public void run() {

			while (true) {
				try {
					synchronized (mutex) {
						while (list.size() == 0) {
							System.out.println(Thread.currentThread().getId() + " : consumer waiting");
							mutex.wait();
						}
						int value = list.poll();

						System.out.println(Thread.currentThread().getId() + " : consume " + value);

						mutex.notifyAll();
						Thread.sleep(1000);
					}
				} catch (InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
			}
		}
	}
}
