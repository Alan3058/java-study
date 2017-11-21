/*
 * @Project Name: java-study
 * @File Name: Example1.java
 * @Package Name: multithread
 * @Date: 2017年10月16日下午4:59:40
 * @Creator: liliangang-1163
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ctosb.study.multithread;

/**
 * notify-wait
 * @author liliangang-1163
 * @date 2017年10月16日下午4:59:40
 * @see
 */
public class Example1 {

	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		PrintA a = new PrintA(lock);
		PrintB b = new PrintB(lock);
		a.start();
		b.start();
	}

	static class PrintA extends Thread {

		private Object lock;

		PrintA(Object lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			for (int t = 1; t < 5; t++) {
				synchronized (lock) {
					System.out.println(t);
					lock.notifyAll();
					try {
						lock.wait(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	static class PrintB extends Thread {

		private Object lock;

		PrintB(Object lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			for (char t = 'A'; t < 'E'; t++) {
				synchronized (lock) {
					System.out.println(t);
					lock.notifyAll();
					try {
						lock.wait(200);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
