package test01_multi_thread;

import test01_multi_thread.thread.TestThread;

public class Main {

	public static void main(String[] args) {
		Thread th1 = new TestThread();
		Thread th2 = new TestThread();
		Thread th3 = new TestThread();
		Thread th4 = new TestThread();

		th1.start();
		th2.start();
		th3.start();
		th4.start();
	}

}
