package com.spear.mq.distributor;

public interface Distributor<S extends Subscriber<M>, M> {

	public void addSubscriber(S subscriber, M initialMessage);

	public boolean removeSubscriber(S subscriber);

	public void sendMessage(M message);

}
