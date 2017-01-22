package com.spear;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.spear.mq.MessageQueue;

public class TestQueue {
	static MessageQueue<TestListener, TestMessage> queue = new MessageQueue<>(16, new TestSubscriptionHandler());
	static AtomicInteger counter = new AtomicInteger(0);

	@Test
	public void test() throws InterruptedException {
		//Thread.sleep(20*1000);
		System.err.println(new Date().toInstant().toString());
		Set<TestListener> listeners = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			TestListener listener = new TestListener();
			queue.addSubscriber(listener, null);
			listeners.add(listener);
		}

		// Thread.sleep(20*1000);
		System.err.println(new Date().toInstant().toString() + "  GOOOOOO!!!========================");
		senderThread.start();
//		while (true) {
//			Thread.sleep(1000);
//			for (TestListener testListener : listeners) {
//				System.err.println(testListener.a.get() + " " + testListener.hashCode() + " " + new Date().toInstant());
//			}
//		}
		Thread.sleep(10);
		TestListener logListener1 = new TestListener(true);
		queue.addSubscriber(logListener1, null);
		Thread.sleep(1);
		TestListener logListener2 = new TestListener(true);
		queue.addSubscriber(logListener2, null);
		Thread.sleep(20*1000);
		
	}

	static Thread senderThread = new Thread() {
		public void run() {
			for (int i = 0; i < 10000; i++) {

				queue.sendMessage(new TestMessage(counter.incrementAndGet()));
			}
		};
	};

}
