package com.saber.app.distributionplanning;

import java.util.List;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-20 16:54
 */
public class PlaceSuggestionBean {

    /**
     * status : 0
     * message : ok
     * result : [{"name":"天安门","location":{"lat":39.915174,"lng":116.403875},"uid":"65e1ee886c885190f60e77ff","city":"北京市","district":"东城区"},{"name":"天安门广场","location":{"lat":39.912735,"lng":116.404015},"uid":"c9b5fb91d49345bc5d0d0262","city":"北京市","district":"东城区"}]
     */

    private int status;
    private String message;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PlaceSuggestionBean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public static class ResultBean {
        /**
         * name : 天安门
         * location : {"lat":39.915174,"lng":116.403875}
         * uid : 65e1ee886c885190f60e77ff
         * city : 北京市
         * district : 东城区
         */

        private String name;
        private LocationBean location;
        private String uid;
        private String city;
        private String district;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "name='" + name + '\'' +
                    ", location=" + location +
                    ", uid='" + uid + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    '}';
        }

        public static class LocationBean {
            /**
             * lat : 39.915174
             * lng : 116.403875
             */

            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            @Override
            public String toString() {
                return "LocationBean{" +
                        "lat=" + lat +
                        ", lng=" + lng +
                        '}';
            }
        }
    }
}
