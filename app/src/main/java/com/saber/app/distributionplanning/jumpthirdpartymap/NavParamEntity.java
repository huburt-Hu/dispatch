package com.saber.app.distributionplanning.jumpthirdpartymap;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-18 16:19
 */
public class NavParamEntity {
    //起点名称
    private String fromName;
    //起点坐标
    private Location from;
    //终点名称
    private String toName;
    //终点坐标
    private Location to;

    public NavParamEntity(Location to) {
        this(null, to);
    }

    public NavParamEntity(Location fromLocation, Location to) {
        this.from = fromLocation;
        this.to = to;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "NavParamEntity{" +
                "fromName='" + fromName + '\'' +
                ", fromLocation=" + from +
                ", toName='" + toName + '\'' +
                ", to=" + to +
                '}';
    }
}
