package com.spear;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.spear.mq.distributor.Subscriber;

public class TestListener implements Subscriber<TestMessage> {
	public AtomicInteger a = new AtomicInteger(0);
	private boolean log = false;

	public TestListener() {

	}

	public TestListener(boolean log) {
		this.log = log;

	}

	@Override
	public void onMessages(TestMessage message) {
		a.incrementAndGet();
		if (log)
			System.out.println(message.getMsgNo() + " " + Thread.currentThread().getName() + " " + this.hashCode() + " "
					+ new Date().toInstant() + " " + message.getClass());

	}

}
