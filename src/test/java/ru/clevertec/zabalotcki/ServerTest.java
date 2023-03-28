package ru.clevertec.zabalotcki;

import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

    @RepeatedTest(2)
    public void checkVerifyListShouldReturnTrue() {
        int n = 10;
        Server server = new Server(n);

        for (int i = 1; i <= n; i++) {
            server.processRequest(i);
        }

        assertTrue(server.verifyList(n));
    }
}
