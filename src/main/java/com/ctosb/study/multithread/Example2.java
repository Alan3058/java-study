
package com.ctosb.study.multithread;

/**
 * 顺序执行线程
 * @author liliangang-1163
 * @date 2017/10/27 16:56
 * @see
 */
public class Example2 {

	public static void main(String[] args) throws InterruptedException {
		Print a = new Print("a");
		Print b = new Print("b");
		Print c = new Print("c");
		a.start();
		a.join();
		b.start();
		b.join();
		c.start();
	}

	static class Print extends Thread {

		public Print(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(getName() + " running------");
		}
	}
}
