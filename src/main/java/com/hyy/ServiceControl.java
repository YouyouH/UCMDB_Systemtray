package com.hyy;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author youyouhuang
 * @create 2019-05-19
 * @desc
 **/

public class ServiceControl {


    public static void startUcmdbServer() {

        String[] cmd = {"net", "start", "UCMDB_Server"};
        Boolean success = executeCommand(cmd, null, 60);

    }

    public static void stopUcmdbServer() {

        String[] cmd = {"net", "stop", "UCMDB_Server"};
        Boolean success = executeCommand(cmd, null, 60);

    }


    public static Boolean executeCommand(String[] cmd, String[] env, long timeout) {
        try {
            Process process = Runtime.getRuntime().exec(cmd, env);
            if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
                process.destroy();
                return false;
            }
            int value = process.exitValue();
            return value == 0;

        } catch (Exception e) {
            return false;
        }
    }

    public static Consts.ServiceState checkServiceStatus(String[] cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            try (InputStream in = process.getInputStream(); InputStream error = process.getErrorStream()) {
                String result = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining());
                if (result.contains("STATE")) {
                    if (result.contains(Consts.ServiceState.RUNNING.getState())) {
                        return Consts.ServiceState.RUNNING;
                    }
                    if (result.contains(Consts.ServiceState.STOPPED.getState())) {
                        return Consts.ServiceState.STOPPED;
                    }
                    if (result.contains(Consts.ServiceState.START_PENDING.getState())) {
                        return Consts.ServiceState.STOP_PENDING;
                    }
                    if (result.contains(Consts.ServiceState.START_PENDING.getState())) {
                        return Consts.ServiceState.START_PENDING;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
