package com.hyy;


import org.junit.jupiter.api.Test;

import static com.hyy.CustomSSL.customSSL;


class CustomSSLTest {

    @Test
    public void customSSLTest() {
        try {
            customSSL();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}