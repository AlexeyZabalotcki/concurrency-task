package ru.clevertec.zabalotcki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientServerTest {

    private static final int N = 10;
    private List<Integer> dataList;
    private Client client;
    private Server server;

    @BeforeEach
    void setUp() {
        dataList = new ArrayList<>(N);
        for (int i = 1; i <= N; i++) {
            dataList.add(i);
        }
        Collections.shuffle(dataList);

        server = new Server(N);
        client = new Client(dataList, server);
    }

    @RepeatedTest(N)
    void checkConcurrentProcessing() {
        List<Future<Integer>> futures = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(N);

        getFutureList(futures, executor);
        getResponse(futures, integers);

        int accumulator = integers.stream().reduce(0, Integer::sum);

        executor.shutdown();

        int expectedAccumulator = (1 + N) * (N / 2);

        assertTrue(server.verifyList(N), "Error: Data list is not valid on the server");
        assertEquals(expectedAccumulator, accumulator);
    }

    private void getFutureList(List<Future<Integer>> futures, ExecutorService executor) {
        for (int i = 1; i < N + 1; i++) {
            int index = i - 1;
            int value = dataList.get(index);

            Callable<Integer> task = () -> server.processRequest(value);
            futures.add(executor.submit(task));
        }
    }

    private void getResponse(List<Future<Integer>> futures, List<Integer> integers) {
        try {
            for (Future<Integer> integerFuture : futures) {
                Integer integer = integerFuture.get();
                integers.add(integer);
            }
        } catch (Exception e) {
            System.out.println("Error processing response: " + e.getMessage());
        }
    }

    @Test
    void checkClientServerInteraction() {
        client.sendRequests();
        assertTrue(server.verifyList(N), "Data list is not valid on the server");
        assertEquals((1 + N) * (N / 2), client.getAccumulator(), "Accumulator has incorrect value");
    }
}
