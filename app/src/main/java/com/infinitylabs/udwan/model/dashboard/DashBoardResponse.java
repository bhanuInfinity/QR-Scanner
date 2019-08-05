package com.infinitylabs.udwan.model.dashboard;

import java.io.Serializable;

public class DashBoardResponse implements Serializable {


    /**
     * result : success
     * data : {"total_inactive_devices":0,"total_active_devices":1,"total_inactive_licenses":5,"total_active_licenses":5}
     */

    private String result;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total_inactive_devices : 0
         * total_active_devices : 1
         * total_inactive_licenses : 5
         * total_active_licenses : 5
         */

        private int total_inactive_devices;
        private int total_active_devices;
        private int total_inactive_licenses;
        private int total_active_licenses;

        public int getTotal_inactive_devices() {
            return total_inactive_devices;
        }

        public void setTotal_inactive_devices(int total_inactive_devices) {
            this.total_inactive_devices = total_inactive_devices;
        }

        public int getTotal_active_devices() {
            return total_active_devices;
        }

        public void setTotal_active_devices(int total_active_devices) {
            this.total_active_devices = total_active_devices;
        }

        public int getTotal_inactive_licenses() {
            return total_inactive_licenses;
        }

        public void setTotal_inactive_licenses(int total_inactive_licenses) {
            this.total_inactive_licenses = total_inactive_licenses;
        }

        public int getTotal_active_licenses() {
            return total_active_licenses;
        }

        public void setTotal_active_licenses(int total_active_licenses) {
            this.total_active_licenses = total_active_licenses;
        }
    }
}

