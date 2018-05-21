package info.competition.hellobobo.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.help.Tip;

import java.util.List;

public interface MapContract {
    interface View {

        void showSearch();

        void hideSearch();

        //显示搜索结果
        void showResult(List<PoiTip> pois);

        void showDrawer();
    }


    interface Presenter {

        void openDrawer();

        void enterSearch();

        void exitSearch();

        void loadSearchData(List<Tip> tips, LatLng latLng);
    }
}
