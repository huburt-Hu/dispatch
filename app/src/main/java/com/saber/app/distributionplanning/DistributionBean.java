package com.saber.app.distributionplanning;

import android.os.Parcel;
import android.os.Parcelable;

import com.saber.app.distributionplanning.base_adapter.Visitable;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-20 15:27
 */
public class DistributionBean implements Parcelable, Visitable {
    //地址
    private String address;
    //姓名
    private String name;
    //手机号
    private String phone;
    //快递单号
    private String tracking;

    private PlaceSuggestionBean.ResultBean detail;

    private boolean isReady;


    public DistributionBean(String address) {
        this.address = address;
    }


    protected DistributionBean(Parcel in) {
        address = in.readString();
        name = in.readString();
        phone = in.readString();
        tracking = in.readString();
        isReady = in.readByte() != 0;
    }

    public static final Creator<DistributionBean> CREATOR = new Creator<DistributionBean>() {
        @Override
        public DistributionBean createFromParcel(Parcel in) {
            return new DistributionBean(in);
        }

        @Override
        public DistributionBean[] newArray(int size) {
            return new DistributionBean[size];
        }
    };

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public PlaceSuggestionBean.ResultBean getDetail() {
        return detail;
    }

    public void setDetail(PlaceSuggestionBean.ResultBean detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "DistributionBean{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", tracking='" + tracking + '\'' +
                '}';
    }

    @Override
    public int type() {
        return Mapping.LAYOUT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(tracking);
        dest.writeByte((byte) (isReady ? 1 : 0));
    }


}
