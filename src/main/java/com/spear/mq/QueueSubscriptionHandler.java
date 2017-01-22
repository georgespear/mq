package com.spear.mq;

import com.spear.mq.distributor.Subscriber;

public interface QueueSubscriptionHandler<S extends Subscriber<M>, M> {
	public M onNewSubscriber(S subscriber, M subscribingMessage);

	public void storeMessage(M message);
}
