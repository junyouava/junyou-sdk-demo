package com.junyou.storage;

import com.junyou.model.EwtReleaseBizNo;
import com.junyou.model.Order;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class StorageService {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final Map<String, EwtReleaseBizNo> ewtBizNos = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void saveOrder(Order order) {
        lock.writeLock().lock();
        try {
            orders.put(order.getOrderNo(), order);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Order getOrderByNo(String orderNo) {
        lock.readLock().lock();
        try {
            return orders.get(orderNo);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void saveEwtReleaseBizNo(EwtReleaseBizNo ewtBizNo) {
        lock.writeLock().lock();
        try {
            ewtBizNos.put(ewtBizNo.getOrderNo(), ewtBizNo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public EwtReleaseBizNo getEwtReleaseBizNoByOrderNo(String orderNo) {
        lock.readLock().lock();
        try {
            return ewtBizNos.get(orderNo);
        } finally {
            lock.readLock().unlock();
        }
    }
}

