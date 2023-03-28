package ru.clevertec.zabalotcki;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int N = 1000;

    public static void main(String[] args) {

        Server server = new Server(N);
        Client client = new Client(getDataList(N), server);

        client.sendRequests();

        checkServerList(N, server);

        int expectedAccumulator = (1 + N) * (N / 2);
        int actualAccumulator = client.getAccumulator();

        checkAccumulator(expectedAccumulator, actualAccumulator);
    }

    private static void checkServerList(int n, Server server) {
        if (!server.verifyList(n)) {
            System.out.println("Error: Data list is not valid on the server");
        }
    }

    private static void checkAccumulator(int expectedAccumulator, int actualAccumulator) {
        if (expectedAccumulator != actualAccumulator) {
            System.out.println("Error: Accumulator has incorrect value: " + actualAccumulator);
        }
    }

    private static List<Integer> getDataList(int n) {
        List<Integer> dataList = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            dataList.add(i);
        }
        return dataList;
    }
}
