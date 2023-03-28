package ru.clevertec.zabalotcki;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Client {

    private final List<Integer> dataList;
    private final Server server;
    private final int expectedSize;
    private int accumulator;

    public Client(List<Integer> dataList, Server server) {
        this.dataList = dataList;
        this.server = server;
        this.expectedSize = dataList.size();
    }

    public void sendRequests() {
        ExecutorService executor = Executors.newFixedThreadPool(dataList.size());

        List<Future<Integer>> futures = getFuturesList(executor);
        List<Integer> integers = getResponses(futures);
        accumulator = calculateAccumulator(integers);

        executor.shutdown();

        checkList();
    }

    private List<Future<Integer>> getFuturesList(ExecutorService executor) {
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 1; i < expectedSize + 1; i++) {
            int index = new Random().nextInt(dataList.size());
            int value = dataList.remove(index);

            Callable<Integer> task = () -> server.processRequest(value);

            futures.add(executor.submit(task));
        }

        return futures;
    }

    private void checkList() {
        if (!dataList.isEmpty()) {
            System.out.println("Error: dataList still contains elements");
        }
    }

    private List<Integer> getResponses(List<Future<Integer>> futures) {
        List<Integer> integers = new ArrayList<>();

        try {
            for (Future<Integer> integerFuture : futures) {
                Integer integer = integerFuture.get();
                integers.add(integer);
            }
        } catch (Exception e) {
            System.out.println("Error processing response: " + e.getMessage());
        }

        return integers;
    }

    private int calculateAccumulator(List<Integer> integers) {
        return integers.stream().reduce(0, Integer::sum);
    }

    public int getAccumulator() {
        return accumulator;
    }
}
