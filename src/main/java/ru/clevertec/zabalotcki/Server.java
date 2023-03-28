package ru.clevertec.zabalotcki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final List<Integer> dataList;
    private final Lock lock;

    public Server(int n) {
        dataList = new ArrayList<>(n);
        lock = new ReentrantLock();
    }

    public int processRequest(int value) {
        try {
            Thread.sleep(new Random().nextInt(901) + 100);

            lock.lock();
            dataList.add(value);
            int size = dataList.size();
            lock.unlock();

            return size;
        } catch (InterruptedException e) {
            System.out.println("Error processing request: " + e.getMessage());
            return -1;
        }
    }

    public boolean verifyList(int n) {
        lock.lock();
        List<Integer> copy = new ArrayList<>(dataList);
        lock.unlock();

        if (copy.size() != n) {
            return false;
        }

        Collections.sort(copy);
        for (int i = 1; i <= n; i++) {
            if (copy.get(i - 1) != i) {
                return false;
            }
        }
        return true;
    }
}
