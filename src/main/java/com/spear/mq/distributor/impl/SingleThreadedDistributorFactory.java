package com.spear.mq.distributor.impl;

import com.spear.mq.distributor.Distributor;
import com.spear.mq.distributor.DistributorFactory;
import com.spear.mq.distributor.Subscriber;

public class SingleThreadedDistributorFactory<S extends Subscriber<M>, M> implements DistributorFactory<Distributor<S, M>> {

    @Override
    public Distributor<S, M> createDistributor() {
        return new SingleThreadedDistributor<>();
    }

    public static <S1 extends Subscriber<M1>, M1> SingleThreadedDistributorFactory<S1, M1> getFactory() {
        return new SingleThreadedDistributorFactory<>();
    }

}
