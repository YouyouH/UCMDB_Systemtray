package com.hyy;

/**
 * @author youyouhuang
 * @create 2019-05-19
 * @desc
 **/

public class Consts {

    enum UcmdbStatus {
        /**
         * UCMDB Server is up and running
         */
        UP("FullyUp"),

        DOWN("Down");

        private String status;

        UcmdbStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    enum ServiceState {

        /**
         * Service
         */
        RUNNING("RUNNING"),
        START_PENDING("START_PENDING"),
        STOP_PENDING("STOP_PENDING"),
        STOPPED("STOPPED");

        private String state;

        ServiceState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }
    }

    public static class Version {
        private String serverVersion;
        private String serverBuild;

        public Version(String serverVersion, String serverBuild) {
            this.serverVersion = serverVersion;
            this.serverBuild = serverBuild;
        }

        public String getServerVersion() {
            return serverVersion;
        }

        public void setServerVersion(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public String getServerBuild() {
            return serverBuild;
        }

        public void setServerBuild(String serverBuild) {
            this.serverBuild = serverBuild;
        }

        @Override
        public String toString() {
            return getServerVersion() + "\n" + getServerBuild();
        }
    }
}
