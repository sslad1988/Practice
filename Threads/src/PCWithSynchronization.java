import java.util.LinkedList;
import java.util.Queue;

public class PCWithSynchronization {

	public Object mutex = new Object();
	Queue<Integer> list = new LinkedList<Integer>();

	public static void main(String args[]) throws InterruptedException {
		PCWithSynchronization pc = new PCWithSynchronization();
		Thread p = new Thread(pc.new Producer());
		Thread c = new Thread(pc.new Consumer());

		p.start();
		c.start();

		p.join();
		c.join();
	}

	private class Producer implements Runnable {
		@Override
		public void run() {
			int counter = 0;
			while (true) {
				try {
					synchronized (mutex) {
						while (list.size() >= 3) {
							System.out.println("produce waiting");
							mutex.wait();
						}
						System.out.println("produce " + counter);
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
							System.out.println("consumer waiting");
							mutex.wait();
						}
						int value = list.poll();

						System.out.println("consume " + value);

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
