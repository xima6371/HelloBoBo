package info.competition.hellobobo.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import info.competition.hellobobo.R;
import info.competition.hellobobo.login.LoginActivity;
import info.competition.hellobobo.login.LoginPresenter;
import info.competition.hellobobo.utils.ActivityUtils;

import static info.competition.hellobobo.login.UserBean.KEY_USERNAME;
import static info.competition.hellobobo.login.UserBean.REQUESTCODE_LOGIN;
import static info.competition.hellobobo.utils.ActivityUtils.LOCATION_PERMISSION_CODE;
import static info.competition.hellobobo.utils.ActivityUtils.STORAGE_PERMISSION_CODE;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ImageView ivUser;
    private TextView tvTitle;
    private TextView tvLogin;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        initView();

        for (String permission : permissions) {
            if (!ActivityUtils.checkPermission(this, permission)) {
                requestPermission(permission);
            }
        }

    }

    private void requestPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission}, ActivityUtils.getCode(permission));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(MapActivity.this, "开始定位").show();
                } else {
                    Toasty.error(MapActivity.this, "无法定位,请给予权限").show();
                }
                break;

            default:
                break;
        }
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //设置Drawer的Item点击事件
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //绑定headView中的控件
        View headView = navigationView.getHeaderView(0);
        tvTitle = headView.findViewById(R.id.tv_title);
        tvLogin = headView.findViewById(R.id.tv_login);
        ivUser = headView.findViewById(R.id.iv_user);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUESTCODE_LOGIN);
            }
        });
        //加载地图fragment
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mapFragment == null)
            mapFragment = MapFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapFragment, R.id.contentFrame);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra(KEY_USERNAME);
                    if (ActivityUtils.checkNull(username))
                        return;
                    tvTitle.setText(username);
                    tvLogin.setText(R.string.nav_header_title);
                }
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }
}
