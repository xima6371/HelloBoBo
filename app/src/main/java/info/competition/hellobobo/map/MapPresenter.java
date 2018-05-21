package info.competition.hellobobo.map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

import info.competition.hellobobo.R;

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;
    private Context mContext;
    private AMapLocationClient mLocationClient;

    private Circle mCircle;

    public static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    public static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    public MapPresenter(MapContract.View view, Context context) {
        mView = view;
        mContext = context;
        mLocationClient = new AMapLocationClient(context);

        initClientOption();
    }

    public AMapLocationClient getLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
            initClientOption();
        }
        return mLocationClient;
    }

    public void startLocation() {
        mLocationClient.startLocation();
    }

    /**
     * 配置地图的设置
     *
     * @param aMap
     */
    public void setUpMap(AMap aMap) {
        MyLocationStyle style = new MyLocationStyle();
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);

        aMap.setMyLocationStyle(style);
        aMap.setMyLocationEnabled(true);

        UiSettings uiSettings = aMap.getUiSettings();
        //高德logo底部居中
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
        //地图缩放设置显示在地图右边正中间
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    private void initClientOption() {

        // 设置定位监听
        mLocationClient.setLocationListener((AMapLocationListener) mView);
        //定位参数
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //设置单次定位
        mOption.setOnceLocation(true);
        //设置为高精度定位模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mLocationClient.setLocationOption(mOption);
    }

    @Override
    public void openDrawer() {
        mView.showDrawer();
    }


    public void setCircle(AMap aMap, LatLng latLng, CircleOptions options) {
        if (mCircle == null)
            mCircle = aMap.addCircle(options);
        else
            mCircle.setCenter(latLng);
    }


    public CircleOptions getAccuracyCircleOption(LatLng latLng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);

        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latLng);
        options.radius(radius);

        return options;

    }

    public void addMarker(LatLng latLng, AMap aMap, Marker marker) {
        if (marker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latLng);
        marker = aMap.addMarker(options);
        marker.setTitle("location");
    }

    @Override
    public void enterSearch() {
        mView.showSearch();
    }

    @Override
    public void exitSearch() {
        mView.hideSearch();
    }

    @Override
    public void loadSearchData(List<Tip> tips, LatLng latLng) {
        List<PoiTip> pois = new ArrayList<>();

        for (Tip tip : tips) {

            LatLng latLng1;
            String dis;
            //获取该地点的经纬度
            if (tip.getPoint() != null) {
                latLng1 = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                //计算与所在地之间距离
                float distance = AMapUtils.calculateLineDistance(latLng1, latLng);
                if (distance > 1000)
                    dis = (int) (distance / 1000) + "公里";
                else
                    dis = (int) distance + "米";
            } else {
                dis = "";
            }
            PoiTip poi = new PoiTip(dis, tip.getName(), tip.getAddress());
            pois.add(poi);
        }

        mView.showResult(pois);
    }
}
