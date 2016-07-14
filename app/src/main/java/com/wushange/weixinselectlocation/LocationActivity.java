package com.wushange.weixinselectlocation;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;
import com.wushange.weixinselectlocation.widget.AppTitle;
import com.wushange.weixinselectlocation.widget.CenterIcon;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

/**
 * Created by wushange on 2016/07/13.
 */
public class LocationActivity extends AppCompatActivity implements
        TencentLocationListener, TencentMap.OnMapCameraChangeListener {
    @ViewInject(R.id.apptitle)
    AppTitle appTitle;
    @ViewInject(R.id.ll_sel_location)
    LinearLayout ll_sel_location;
    @ViewInject(R.id.mapview)
    private MapView mapView;
    @ViewInject(R.id.poi_list)
    private EasyRecyclerView mRecyclerView;
    LocationListAdapter mAdapter;
    private Marker mLocationMarker;
    private Marker mCenterMarker;
    private TencentMap tencentMap;
    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;
    private boolean isFirstEnter = true;//第一次进入动画效果移动到定位点
    private boolean isActiveMove = false;//是不是主动拖动屏幕
    TencentSearch tencentSearch = new TencentSearch(this);
    private CenterIcon centerIcon = null;


    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_main_view);
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(true);
        x.view().inject(this);
        context = this;
        initMap();
        initView();
        iniRecycerView();
    }


    public void initView() {

        appTitle.setCenterTitle("位置").setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setRightText("发送").setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMap().getScreenShot(new TencentMap.OnScreenShotListener() {

                    @Override
                    public void onMapScreenShot(Bitmap arg0) {
                        // TODO Auto-generated method stub

                        Toast toast = Toast.makeText(context, "截屏成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        ImageView imageView = new ImageView(context);
                        imageView.setImageBitmap(arg0);
                        LinearLayout toastImage = (LinearLayout) toast.getView();
                        toastImage.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        toast.show();
                    }
                });
            }
        });

    }


    private void initMap() {
        // 初始化屏幕中心
        centerIcon = new CenterIcon(this, mapView);
        getWindow().addContentView(
                centerIcon,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        tencentMap = mapView.getMap();
        tencentMap.setZoom(16);
        UiSettings uiSettings = mapView.getUiSettings();
        uiSettings.setAnimationEnabled(true);
        mLocationManager = TencentLocationManager.getInstance(this);
        tencentMap.setOnMapCameraChangeListener(this);
    }

    private void iniRecycerView() {
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#E5E5E5"), dp2px(context, 0.5f), dp2px(context, 0), 0);//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new ScaleInBottomAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setEmptyView(R.layout.loading_view_layout);
        mRecyclerView.setScrollBarStyle(-1);
        mRecyclerView.setAdapterWithProgress(mAdapter = new LocationListAdapter(context));
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, mAdapter.getItem(position).title, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        LogUtil.e("----onLocationChanged---" + "----" + error + "---" + reason + tencentLocation.toString());
        if (error == TencentLocation.ERROR_OK) {
            mLocation = tencentLocation;
            LatLng latLngLocation = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            // 更新 location Marker
            if (mLocationMarker == null) {
                mLocationMarker =
                        tencentMap.addMarker(new MarkerOptions().
                                position(latLngLocation).
                                icon(BitmapDescriptorFactory.fromResource(R.mipmap.a5z)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
            }
            if (isFirstEnter) {
                tencentMap.animateTo(latLngLocation);
                isActiveMove = false;
                Location location = new Location((float) tencentLocation.getLatitude(), (float) tencentLocation.getLongitude());
                searchPoi(location);
                isFirstEnter = false;
            } else {

            }

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        LogUtil.e("---onStatusUpdate---" + i + "---" + s + "---" + s1);
    }

    private void startLocation() {
        LogUtil.e("开始定位" + mLocationManager);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(50000);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO);
        int error = mLocationManager.requestLocationUpdates(request, this);
        LogUtil.e("定位错误" + error);

    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {


    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Location location = new Location(((float) cameraPosition.getTarget().getLatitude()), ((float) cameraPosition.getTarget().getLongitude()));
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0, 0, -100, 0F);  //同一个x轴 (开始结束都是50f,所以x轴保存不变)  y轴开始点50f  y轴结束点80f
        alphaAnimation2.setDuration(500);  //设置时间
        alphaAnimation2.setRepeatCount(Animation.ABSOLUTE);  //为重复执行的次数。如果设置为n，则动画将执行n+1次。INFINITE为无限制播放
        alphaAnimation2.setRepeatMode(Animation.RESTART);  //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
        alphaAnimation2.setInterpolator(new LinearInterpolator());
        centerIcon.setAnimation(alphaAnimation2);
        alphaAnimation2.start();


        if (mCenterMarker == null) {
            mCenterMarker =
                    tencentMap.addMarker(new MarkerOptions().
                            position(cameraPosition.getTarget()).
                            icon(BitmapDescriptorFactory.fromResource(R.mipmap.aud)));
        } else {
            mCenterMarker.setPosition(cameraPosition.getTarget());
        }

        LogUtil.e("移动后坐标地址为" + cameraPosition.toString());
        if (isActiveMove == false) {

        } else {

            searchPoi(location);

        }
        isActiveMove = true;
    }


    /**
     * 搜索当前坐标点的poilist
     *
     * @param location
     */
    private void searchPoi(Location location) {
        mAdapter.clear();

        Geo2AddressParam param = new Geo2AddressParam().location(location).get_poi(true);
        tencentSearch.geo2address(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, BaseObject object) {
                if (object != null) {
                    Geo2AddressResultObject oj = (Geo2AddressResultObject) object;
                    Geo2AddressResultObject.ReverseAddressResult re = oj.result;

                    if (re.pois != null) {

                        mAdapter.addAll(re.pois);
                    }
                    for (Geo2AddressResultObject.ReverseAddressResult.Poi poi : re.pois) {
                        LogUtil.e("移动屏幕后检索当前位置信息--" + poi.title + "----" + poi.address);
                    }


                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, String s, Throwable throwable) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission(context, new AcpListener() {
            @Override
            public void onGranted() {
                startLocation();
            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocation();
    }


    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }


    /**
     * 权限检测
     *
     * @param context
     * @param listener
     * @param permissions
     */
    public void checkPermission(Context context, AcpListener listener, String... permissions) {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(permissions).build(), listener);
    }

    public int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }
}
