package com.spear.mq;

import com.spear.mq.distributor.Distributor;
import com.spear.mq.distributor.Subscriber;
import com.spear.mq.distributor.impl.ConcurrentUniformDistribution;
import com.spear.mq.distributor.impl.SingleThreadedDistributor;
import com.spear.mq.distributor.impl.SingleThreadedDistributorFactory;

public class MessageQueue<S extends Subscriber<M>, M> extends SingleThreadedDistributor<S, M> {

	private static final int SINGLE_THREADED_QUEUE = 1;

	private QueueSubscriptionHandler<S, M> subscriptionHandler = null;
	ConcurrentUniformDistribution<M, Subscriber<M>, Distributor<Subscriber<M>, M>> executorDistribution;

	public MessageQueue(int maxThreads, QueueSubscriptionHandler<S, M> subscriptionHandler) {
		SingleThreadedDistributorFactory<Subscriber<M>, M> factory = SingleThreadedDistributorFactory.getFactory();
		executorDistribution = new ConcurrentUniformDistribution<>(maxThreads, factory);
		this.subscriptionHandler = subscriptionHandler;
	}

	public MessageQueue(int maxThreads) {
		this(maxThreads, null);
	}

	public MessageQueue() {
		this(SINGLE_THREADED_QUEUE);
	}

	@Override
	public void addSubscriber(S subscriber, M message) {
		synchronized (executorDistribution) {
			if (subscriptionHandler != null) {
				executorDistribution.addSubscriber(subscriber,
						subscriptionHandler.onNewSubscriber(subscriber, message));
			} else {
				executorDistribution.addSubscriber(subscriber, message);
			}

		}
	}

	@Override
	public boolean removeSubscriber(S subscriber) {
		return executorDistribution.removeSubscriber(subscriber);
	}

	@Override
	public void sendMessage(M message) {
		synchronized (executorDistribution) {
			if (subscriptionHandler != null) {
				subscriptionHandler.storeMessage(message);
			}
			executorDistribution.sendMessage(message);
		}

	}

}
