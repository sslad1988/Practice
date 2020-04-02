import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeUsingFuture {
	public static void main(String args[]) {

		ExecutorService service = Executors.newFixedThreadPool(3);
		List<Future<String>> result = new ArrayList<Future<String>>(50);
		PrimeUsingFuture primeUsingFuture = new PrimeUsingFuture();
		for (int i = 2; i < 50; i++) {
			PrimeCalculator prime = primeUsingFuture.new PrimeCalculator(i);
			result.add(service.submit(prime));
		}

		for (Future<String> output : result) {
			try {
				System.out.println(output.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		service.shutdown();

	}

	private class PrimeCalculator implements Callable<String> {
		private int n;

		public PrimeCalculator(int n) {
			super();
			this.n = n;
		}

		@Override
		public String call() throws Exception {
			boolean isPrime = true;

			for (int i = 2; i < n; i++) {
				if (n % i == 0) {
					isPrime = false;
					break;
				}
			}
			return isPrime ? n + " : Prime" : n + " : Not Prime";
		}
	}

}
