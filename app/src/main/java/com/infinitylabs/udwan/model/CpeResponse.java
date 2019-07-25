package com.infinitylabs.udwan.model;

import java.io.Serializable;

public class CpeResponse implements Serializable {


    /**
     * result : success
     * message : Device already installed!
     * data : {"name":"License2","price":60,"lid":"6db0mhojy01rbvb","duration":3,"isActive":"1"}
     */

    private String result;
    private String message;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : License2
         * price : 60
         * lid : 6db0mhojy01rbvb
         * duration : 3
         * isActive : 1
         */

        private String name;
        private int price;
        private String lid;
        private int duration;
        private String isActive;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }
    }
}
