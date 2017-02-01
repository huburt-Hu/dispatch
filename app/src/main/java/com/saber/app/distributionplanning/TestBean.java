package com.saber.app.distributionplanning;

import java.util.List;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-24 9:29
 */
public class TestBean {

    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"mailNo":"968018776110","update":1466926312666,"updateStr":"2016-06-26 15:31:52","ret_code":0,"flag":true,"status":4,"tel":"400-889-5543","expSpellName":"shentong","data":[{"time":"2016-06-26 12:26","context":"已签收,签收人是:【本人】"},{"time":"2016-06-25 15:31","context":"【陕西陇县公司】的派件员【西城业务员】正在派件"},{"time":"2016-06-25 14:11","context":"快件已到达【陕西陇县公司】"},{"time":"2016-06-25 09:08","context":"由【陕西宝鸡公司】发往【陕西陇县公司】"},{"time":"2016-06-24 14:08","context":"由【陕西西安中转部】发往【陕西宝鸡公司】"},{"time":"2016-06-22 13:23","context":"由【山东临沂公司】发往【陕西西安中转部】"},{"time":"2016-06-21 23:02","context":"【江苏常熟公司】正在进行【装袋】扫描"},{"time":"2016-06-21 23:02","context":"由【江苏常熟公司】发往【江苏江阴航空部】"},{"time":"2016-06-21 18:30","context":"【江苏常熟公司】的收件员【严继东】已收件"},{"time":"2016-06-21 16:41","context":"【江苏常熟公司】的收件员【凌明】已收件"}],"expTextName":"申通快递"}
     */

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "showapi_res_code=" + showapi_res_code +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                ", showapi_res_body=" + showapi_res_body +
                '}';
    }

    public static class ShowapiResBodyBean {
        /**
         * mailNo : 968018776110
         * update : 1466926312666
         * updateStr : 2016-06-26 15:31:52
         * ret_code : 0
         * flag : true
         * status : 4
         * tel : 400-889-5543
         * expSpellName : shentong
         * data : [{"time":"2016-06-26 12:26","context":"已签收,签收人是:【本人】"},{"time":"2016-06-25 15:31","context":"【陕西陇县公司】的派件员【西城业务员】正在派件"},{"time":"2016-06-25 14:11","context":"快件已到达【陕西陇县公司】"},{"time":"2016-06-25 09:08","context":"由【陕西宝鸡公司】发往【陕西陇县公司】"},{"time":"2016-06-24 14:08","context":"由【陕西西安中转部】发往【陕西宝鸡公司】"},{"time":"2016-06-22 13:23","context":"由【山东临沂公司】发往【陕西西安中转部】"},{"time":"2016-06-21 23:02","context":"【江苏常熟公司】正在进行【装袋】扫描"},{"time":"2016-06-21 23:02","context":"由【江苏常熟公司】发往【江苏江阴航空部】"},{"time":"2016-06-21 18:30","context":"【江苏常熟公司】的收件员【严继东】已收件"},{"time":"2016-06-21 16:41","context":"【江苏常熟公司】的收件员【凌明】已收件"}]
         * expTextName : 申通快递
         */

        private String mailNo;
        private long update;
        private String updateStr;
        private int ret_code;
        private boolean flag;
        private int status;
        private String tel;
        private String expSpellName;
        private String expTextName;
        private List<DataBean> data;

        public String getMailNo() {
            return mailNo;
        }

        public void setMailNo(String mailNo) {
            this.mailNo = mailNo;
        }

        public long getUpdate() {
            return update;
        }

        public void setUpdate(long update) {
            this.update = update;
        }

        public String getUpdateStr() {
            return updateStr;
        }

        public void setUpdateStr(String updateStr) {
            this.updateStr = updateStr;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getExpSpellName() {
            return expSpellName;
        }

        public void setExpSpellName(String expSpellName) {
            this.expSpellName = expSpellName;
        }

        public String getExpTextName() {
            return expTextName;
        }

        public void setExpTextName(String expTextName) {
            this.expTextName = expTextName;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ShowapiResBodyBean{" +
                    "mailNo='" + mailNo + '\'' +
                    ", update=" + update +
                    ", updateStr='" + updateStr + '\'' +
                    ", ret_code=" + ret_code +
                    ", flag=" + flag +
                    ", status=" + status +
                    ", tel='" + tel + '\'' +
                    ", expSpellName='" + expSpellName + '\'' +
                    ", expTextName='" + expTextName + '\'' +
                    ", data=" + data +
                    '}';
        }

        public static class DataBean {
            /**
             * time : 2016-06-26 12:26
             * context : 已签收,签收人是:【本人】
             */

            private String time;
            private String context;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "time='" + time + '\'' +
                        ", context='" + context + '\'' +
                        '}';
            }
        }
    }
}
