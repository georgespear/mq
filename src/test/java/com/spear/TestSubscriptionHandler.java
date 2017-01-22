package com.spear;

import java.util.HashSet;
import java.util.Set;

import com.spear.mq.QueueSubscriptionHandler;

public class TestSubscriptionHandler implements QueueSubscriptionHandler<TestListener, TestMessage> {
	TestMessageContainer container = new TestMessageContainer();

	public TestMessage onNewSubscriber(TestListener subscriber, TestMessage subscribingMessage) {
		if (subscribingMessage != null)
			container.messages.add(subscribingMessage);
		return new TestMessageContainer(container);
	}

	@Override
	public void storeMessage(TestMessage message) {
		container.messages.add(message);

	}

	private class TestMessageContainer extends TestMessage {
		public TestMessageContainer(TestMessageContainer copy) {
			this();
			this.messages = new HashSet<>(copy.messages);
		}

		Set<TestMessage> messages = new HashSet<>();

		public TestMessageContainer() {
			super(-1);
		}

		@Override
		public int getMsgNo() {
			return messages.size();
		}

	}
}
