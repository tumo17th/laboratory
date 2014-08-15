package test01_multi_thread.thread;

import test01_multi_thread.obj.NiseList;

public class TestThread extends Thread {

	static NiseList obj = new NiseList();

	@Override
	public void run() {
		obj.access(this.getName());
	}

}
