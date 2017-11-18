import java.util.concurrent.ThreadLocalRandom;

public class ThreadPi {
	
	public static void main(String[] args){
		
		long startTime = System.currentTimeMillis();
		long endTime;
		long nIterations = 0;
		int nThreads = 0;
		
		PiCalculatorThread[] threads;
		long threadCalculations[];
		float calculatedValue;
		
		//Get and check input
		try {
			nThreads = Integer.parseInt(args[0]);
			nIterations = Long.parseLong(args[1]);
		
		} catch (Exception e) {
			System.out.println("Invalid Input!");
		}
		
		if (nIterations < 1 || nThreads < 1) {
			System.out.println("Number of iterations or threads is too low");
			System.exit(1);
		}
		
		threads = new PiCalculatorThread[nThreads];
		threadCalculations = new long[nThreads];
		
		//Each thread will do nIterations / nThreads. If there is a remainder after this, give those iterations to the first thread
		long iterationsPerThread = (nIterations / nThreads) + (nIterations % nThreads);
		for (int i = 0; i < nThreads; i++) {

			threads[i] = new PiCalculatorThread(i, iterationsPerThread, threadCalculations);
			threads[i].start();
			
			//Each thread will do nIterations / nThreads
			iterationsPerThread = (nIterations / nThreads);
		}
		
		//Join threads
		for (Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException ie) {
                ie.printStackTrace();
            }
		}
		
		//Calculate total and display output
		int totalCount = 0;
		float ratio = 0;
		
		for (long i : threadCalculations) {
			totalCount += i;
		}
		
		ratio = (float) totalCount / (float) nIterations;
		
		calculatedValue = totalCount / (float) nIterations;
		calculatedValue *= 4;
		
		System.out.println("Total Number of Points:\t\t" + nIterations);
		System.out.println("Inside Number of Points:\t" + totalCount);
		System.out.println("Ratio of Inside to Outside:\t" + ratio);
		System.out.println("Calculated Value of Pi:\t\t" + calculatedValue);
		
		endTime = System.currentTimeMillis();
		double elapsedTime = (double)(endTime - startTime);
		System.out.println("Elapsed Time:\t\t" + elapsedTime + " ms");
	}
}

class PiCalculatorThread extends Thread {
	
	int threadNumber;
	long numIterations;
	long[] resultArray;
	
	public PiCalculatorThread(int threadNum, long numIters, long[] results){
		this.threadNumber = threadNum;
		this.numIterations = numIters;
		resultArray = results;
	}
	
	@Override
	public void run(){
		double xVal;
		double yVal;
		
		for (long i = 0; i < numIterations; i++){
			xVal = ThreadLocalRandom.current().nextDouble(0, 1);
			yVal = ThreadLocalRandom.current().nextDouble(0, 1);
			
			if (Math.pow(xVal, 2) + Math.pow(yVal, 2) < 1){
				resultArray[threadNumber]++;
			}
		}
	}

}