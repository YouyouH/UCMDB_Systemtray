package com.hyy;

import org.junit.jupiter.api.Test;

class ServiceControlTest {

    @Test
    void startUcmdbServer() {
    }

    @Test
    void stopUcmdbServer() {
    }

    @Test
    void executeCommand() {
    }

    @Test
    void checkServiceStatus() {
        Consts.ServiceState serviceState = ServiceControl.checkServiceStatus(new String[]{"sc", "query", "UCMDB_Server"});
        System.out.println(serviceState.getState());
    }
}