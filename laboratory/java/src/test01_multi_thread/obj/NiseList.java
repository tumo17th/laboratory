package test01_multi_thread.obj;

public class NiseList {

	private int size = 0;

	public void access(String threadName) {
		check(size + 1, threadName);
		System.out.println("Access : " + size++ + " [" + threadName + "]");
	}

	private void check(int minCapacity, String threadName) {
		System.out.println("Check : " + minCapacity + " [" + threadName + "]");
	}

}
