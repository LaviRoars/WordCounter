import java.io.*;

class WordsCounter extends Thread {
	private String filename;
	private NoWords nw;


	public WordsCounter(String filename, NoWords nw) {
		this.filename = filename;
		this.nw = nw;

	}

	//read file while updating word count
	public void run() {
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(filename);
			java.util.Scanner sc = new java.util.Scanner(fis);
			while(sc.hasNext()) {
				nw.incTotalWordCount(1);
				sc.next();
			}
			nw.decActiveThread();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			if(fis != null)
				try {
					fis.close();
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
				}
		}

	}


}

public class NoWords {

	//shared variable to store no of active threads and total word count
	private int activeThreads;
	private int totalWordCount;

	public NoWords(int activeThreads) {
		this.activeThreads = activeThreads;
	}

	//to count words in all files and protected with synchronized
	public synchronized void incTotalWordCount(int n) {
		totalWordCount += n;

	}

	//print total word count when all files have been read
	public synchronized void decActiveThread() {
		activeThreads --;
		if(activeThreads == 0)
			System.out.println("Total number of words in the file is " + totalWordCount);

	}

	public static void main(String args[]) {
		NoWords nw = new NoWords(args.length);

		for(String s : args) {
			(new WordsCounter(s, nw)).start();

		}

	}

}