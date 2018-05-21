package info.competition.hellobobo.map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.List;

import es.dmoral.toasty.Toasty;
import info.competition.hellobobo.R;
import info.competition.hellobobo.utils.SensorEventHelper;

import static info.competition.hellobobo.map.MapPresenter.FILL_COLOR;
import static info.competition.hellobobo.map.MapPresenter.STROKE_COLOR;

public class MapFragment extends Fragment implements View.OnClickListener, MapContract.View, LocationSource
        , AMapLocationListener, TextWatcher, Inputtips.InputtipsListener {

    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mLocationChangedListener;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    private Marker locMarker;
    private SensorEventHelper mHelper;
    private Circle mCircle;

    private ProgressBar pbSearch;
    private EditText etSearch;
    private ImageView ivSearch;
    private ImageView ivDrawerSwitch;
    private FloatingActionButton fabLoc;
    private RecyclerView recyclerView;
    private View searchView;

    private String currentCity;
    private LatLng currentLatLng;

    private PoiAdapter mAdapter;

    private boolean isSearch = false;//判断是否在搜索页面
    private boolean isFirstFix = false;//判断是否第一次定位

    private MapPresenter mPresenter;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        InputtipsQuery inputtipsQuery = new InputtipsQuery(s.toString().trim(), currentCity);
        inputtipsQuery.setCityLimit(true);
        Inputtips inputtips = new Inputtips(getActivity(), inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (i == 1000 && list != null)
            mPresenter.loadSearchData(list, currentLatLng);
    }

    @Override
    public void showResult(List<PoiTip> pois) {

        if (recyclerView.getVisibility() == View.GONE)
            recyclerView.setVisibility(View.VISIBLE);

        mAdapter.addResultTips(pois);
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MapPresenter(this, getActivity().getApplicationContext());
        mHelper = new SensorEventHelper(getActivity());
        mHelper.registerSensorListener();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        //初始化地图
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setLocationSource(this);
            //配置地图的属性
            mPresenter.setUpMap(mAMap);
        }

        mPresenter.startLocation();

    }

    private void initView(@NonNull View view) {
        pbSearch = view.findViewById(R.id.pb_search);
        recyclerView = view.findViewById(R.id.rv_search_result);
        fabLoc = view.findViewById(R.id.fab_loc);
        etSearch = view.findViewById(R.id.et_search);
        searchView = view.findViewById(R.id.search_view);
        ivDrawerSwitch = view.findViewById(R.id.iv_drawer_switch);
        ivSearch = view.findViewById(R.id.iv_search);

        fabLoc.setOnClickListener(this);
        ivDrawerSwitch.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        etSearch.addTextChangedListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new PoiAdapter(getActivity());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            currentCity = aMapLocation.getCity();
            Toasty.success(getActivity(), currentCity).show();
            double latitude = aMapLocation.getLatitude();//获取纬度
            double longitude = aMapLocation.getLongitude();//获取经度
            currentLatLng = new LatLng(latitude, longitude);//定位到经纬度


            //判断marker是否初始化
            if (!isFirstFix) {
                isFirstFix = true;

                addCircle(aMapLocation);
                addMarker();

                mHelper.setCurrentMarker(locMarker);
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
            } else {
                mCircle.setCenter(currentLatLng);
                mCircle.setRadius(aMapLocation.getAccuracy());
                locMarker.setPosition(currentLatLng);
                mAMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            }

        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("A map Error", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
            Toasty.error(getActivity(), aMapLocation.getErrorInfo()).show();
        }
    }

    private void addMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.navi_map_gps_locked)));
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(currentLatLng);
        locMarker = mAMap.addMarker(markerOptions);
        locMarker.setTitle("location");
    }

    private void addCircle(AMapLocation aMapLocation) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(currentLatLng);
        options.radius(aMapLocation.getAccuracy());
        mCircle = mAMap.addCircle(options);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_drawer_switch:
                if (isSearch)
                    mPresenter.exitSearch();
                else
                    mPresenter.openDrawer();
                break;

            case R.id.et_search:
                if (!isSearch)
                    mPresenter.enterSearch();
                break;
            case R.id.fab_loc:
                mPresenter.startLocation();//进行手动定位
                break;

        }
    }

    @Override
    public void showSearch() {
        isSearch = true;
        fabLoc.setVisibility(View.GONE);
        //由菜单键变成返回键
        ivDrawerSwitch.setImageResource(R.drawable.ic_back_black);
        Animation animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in);
        searchView.startAnimation(animIn);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchView.setVisibility(View.VISIBLE);
                etSearch.setCursorVisible(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void hideSearch() {

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        isSearch = false;
        fabLoc.setVisibility(View.VISIBLE);
        //重置搜索框,关闭输入法
        etSearch.setText("");
        etSearch.setCursorVisible(false);
        recyclerView.setVisibility(View.GONE);
        ivDrawerSwitch.setImageResource(R.drawable.ic_drawer_switch);
        Animation animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out);
        searchView.startAnimation(animOut);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void showDrawer() {
        ((MapActivity) getActivity()).openDrawer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_map, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mHelper.registerSensorListener();

    }

    @Override
    public void onPause() {
        super.onPause();
        mHelper.unRegisterSensorListener();
        mMapView.onPause();
        deactivate();
        isFirstFix = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locMarker != null)
            locMarker.destroy();
        mMapView.onDestroy();
        mPresenter.getLocationClient().onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangedListener = onLocationChangedListener;
        mPresenter.startLocation();
    }

    @Override
    public void deactivate() {
        mLocationChangedListener = null;

        if (mPresenter.getLocationClient() != null) {
            mPresenter.getLocationClient().stopLocation();
        }
    }
}
