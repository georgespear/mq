package com.spear.mq.distributor.impl;

import java.util.ArrayList;
import java.util.List;

import com.spear.mq.distributor.Distributor;
import com.spear.mq.distributor.DistributorFactory;
import com.spear.mq.distributor.Subscriber;

/**
 * Splits subscribers S for the distributors D
 * 
 * @author glezhava
 * @param <S>
 * @param <D>
 */
public class ConcurrentUniformDistribution<M, S extends Subscriber<M>, D extends Distributor<S, M>>
		implements Distributor<S, M> {

	private int numberOfDistributors;
	private List<D> distributors;

	public ConcurrentUniformDistribution(int numberOfDistributors, DistributorFactory<D> factory) {

		this.numberOfDistributors = numberOfDistributors;
		this.distributors = new ArrayList<>(numberOfDistributors);
		for (int i = 0; i < numberOfDistributors; i++) {
			D bucket = factory.createDistributor();
			distributors.add(bucket);
		}

	}

	@Override
	public void addSubscriber(S subscriber, M message) {
		int index = getDistributorIndex(subscriber);
		D distributor = distributors.get(index);
		distributor.addSubscriber(subscriber, message);
	}

	@Override
	public boolean removeSubscriber(S subscriber) {
		int index = getDistributorIndex(subscriber);
		D distributor = distributors.get(index);
		return distributor.removeSubscriber(subscriber);

	}

	@Override
	public void sendMessage(M message) {
		for (D d : distributors) {
			d.sendMessage(message);
		}
	}

	private int getDistributorIndex(S subscriber) {
		return Math.abs(subscriber.hashCode() % numberOfDistributors);
	}

}
