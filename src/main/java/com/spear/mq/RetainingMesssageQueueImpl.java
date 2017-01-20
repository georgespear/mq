package com.spear.mq;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.spear.mq.distributor.Distributor;
import com.spear.mq.distributor.Subscriber;
import com.spear.mq.distributor.impl.ConcurrentUniformDistribution;
import com.spear.mq.distributor.impl.SingleThreadedDistributor;
import com.spear.mq.distributor.impl.SingleThreadedDistributorFactory;

public abstract class RetainingMesssageQueueImpl<S extends Subscriber<M>, M> extends SingleThreadedDistributor<S, M> {

    private static final int SINGLE_THREADED_QUEUE = 1;

    ConcurrentUniformDistribution<M, Subscriber<M>, Distributor<Subscriber<M>, M>> executorDistribution;
    Lock lock = new ReentrantReadWriteLock().writeLock();

    public RetainingMesssageQueueImpl(int maxThreads) {
        SingleThreadedDistributorFactory<Subscriber<M>, M> factory = SingleThreadedDistributorFactory.getFactory();
        executorDistribution = new ConcurrentUniformDistribution<>(maxThreads, factory);
    }

    public RetainingMesssageQueueImpl() {
        this(SINGLE_THREADED_QUEUE);
    }

}
