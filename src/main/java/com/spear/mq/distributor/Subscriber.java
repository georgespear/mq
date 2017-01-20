package com.spear.mq.distributor;

public interface Subscriber<M> {

    public void onMessages(M message);
}
