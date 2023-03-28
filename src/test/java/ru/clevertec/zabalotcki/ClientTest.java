package ru.clevertec.zabalotcki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {
    private int n;
    private List<Integer> dataList;

    @BeforeEach
    void setUp() {
        n = 10;
        dataList = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            dataList.add(i);
        }
    }

    @RepeatedTest(2)
    public void checkSendRequestsShouldReturnExpectedAccumulator() {
        Server server = new Server(n);
        Client client = new Client(dataList, server);

        client.sendRequests();

        int expectedAccumulator = (1 + n) * (n / 2);
        int actualAccumulator = client.getAccumulator();
        assertEquals(expectedAccumulator, actualAccumulator);
    }
}
