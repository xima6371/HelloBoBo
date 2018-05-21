package info.competition.hellobobo.map;

public class PoiTip {
    private String mDistance;
    private String mPoiName;
    private String mAddress;


    public PoiTip(String distance, String poiName, String address) {
        mDistance = distance;
        mPoiName = poiName;
        mAddress = address;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getPoiName() {
        return mPoiName;
    }

    public void setPoiName(String poiName) {
        mPoiName = poiName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
