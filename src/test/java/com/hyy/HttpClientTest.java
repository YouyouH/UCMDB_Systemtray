package com.hyy;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HttpClientTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkUcmdbStatus() {
        Assert.assertTrue(HttpClient.checkUcmdbStatus() == Consts.UcmdbStatus.UP);
    }

    @Test
    void checkUcmdbVersion() throws IOException {
        Consts.Version version = HttpClient.checkUcmdbVersion();
        System.out.println(version.getServerVersion());
        System.out.println(version.getServerBuild());
    }
}