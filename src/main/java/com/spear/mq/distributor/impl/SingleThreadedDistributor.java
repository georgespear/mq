package com.spear.mq.distributor.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.MapMaker;
import com.spear.mq.distributor.Distributor;
import com.spear.mq.distributor.Subscriber;

public class SingleThreadedDistributor<S extends Subscriber<M>, M> implements Distributor<S, M> {

    private Set<S> subscribers;
    private Set<S> pendingSubscribers;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public SingleThreadedDistributor() {
        ConcurrentMap<S, Boolean> tmp1 = new MapMaker().weakKeys().makeMap();
        subscribers = Collections.newSetFromMap(tmp1);
        ConcurrentMap<S, Boolean> tmp2 = new MapMaker().weakKeys().makeMap();
        pendingSubscribers = Collections.newSetFromMap(tmp2);

    }

    @Override
    public void addSubscriber(S subscriber, final M message) {
        pendingSubscribers.add(subscriber);
        executor.submit(new Task(message, TaskType.SUBSCRIBE));
    }

    @Override
    public boolean removeSubscriber(S subscriber) {
        return subscribers.remove(subscriber);
    }

    @Override
    public void sendMessage(M message) {
        executor.submit(new Task(message, TaskType.DISTRIBUTE));
    }

    private void distribute(M message) {
        for (S s : subscribers) {
            s.onMessages(message);
        }
    }

    private void subscribe(M message) {
        Iterator<S> it = pendingSubscribers.iterator();
        while (it.hasNext()) {
            S pending = it.next();
            pending.onMessages(message);
            subscribers.add(pending);
            it.remove();
        }
    }

    private enum TaskType {
        SUBSCRIBE,
        DISTRIBUTE
    }

    private class Task implements Runnable {

        M message;
        TaskType taskType;

        Task(M message, TaskType taskType) {
            this.message = message;
            this.taskType = taskType;

        }

        @Override
        public void run() {

            switch (taskType) {
                case DISTRIBUTE:
                    distribute(message);
                    break;
                case SUBSCRIBE:
                    subscribe(message);
                    break;
                default:
                    break;

            }
        }

    }

}
