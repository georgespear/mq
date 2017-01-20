package com.spear.mq.distributor;

public interface DistributorFactory<D> {

    /**
     * Creates and returns new distributor.
     * 
     * @return
     */
    public D createDistributor();

}
