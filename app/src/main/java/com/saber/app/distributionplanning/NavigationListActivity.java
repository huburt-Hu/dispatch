package com.saber.app.distributionplanning;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.saber.app.distributionplanning.base_adapter.CommonAdapter;
import com.saber.app.distributionplanning.base_adapter.RecyclerViewDivider;
import com.saber.app.distributionplanning.base_adapter.Visitable;
import com.saber.app.distributionplanning.jumpthirdpartymap.Location;
import com.saber.app.distributionplanning.jumpthirdpartymap.LocationUtils;
import com.saber.app.distributionplanning.jumpthirdpartymap.NavParamEntity;
import com.saber.app.distributionplanning.jumpthirdpartymap.ThirdPartyMapManager;
import com.saber.app.distributionplanning.runtime_permission.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NavigationListActivity extends BaseActivity implements CommonAdapter.OnClickListener, CommonAdapter.OnLongClickListener {

    public static final int EDIT_CODE = 0x123;

    private List<DistributionBean> mData;
    private boolean isReady;
    private CommonAdapter mAdapter;
    private MenuItem mItem;
    private LocationClient mLocationClient;
    private MenuItem mItem_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initLocation();
        init();
    }

    private void initLocation() {
        mLocationClient = App.getLocationClient();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("tag", "sort-run");
                        mLocationClient.stop();
                        Location l = new Location(bdLocation.getLongitude(), bdLocation.getLatitude());
                        DistributionBean[] array = mData.toArray(new DistributionBean[mData.size() - 1]);
                        int size = array.length - 1;
                        DistributionBean temp;
                        for (int i = 0; i < size; i++) {
                            for (int j = i + 1; j < size; j++) {
                                double d1 = LocationUtils.getDistance(l.getLatitude(), l.getLongitude(),
                                        array[i].getDetail().getLocation().getLat(), array[i].getDetail().getLocation().getLng());
                                double d2 = LocationUtils.getDistance(l.getLatitude(), l.getLongitude(),
                                        array[j].getDetail().getLocation().getLat(), array[j].getDetail().getLocation().getLng());
                                if (d2 > d1) {
                                    temp = array[i];
                                    array[i] = array[j];
                                    array[j] = temp;
                                }
                            }
                            PlaceSuggestionBean.ResultBean.LocationBean last = array[i].getDetail().getLocation();
                            l.setLatitude(last.getLat());
                            l.setLongitude(last.getLng());
                        }
                        mData.clear();
                        Collections.addAll(mData, array);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("tag", "sort-ui");
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(NavigationListActivity.this, "排序成功!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

        mData = App.getBeanList();
        if (mData == null) {
            mData = new ArrayList<>();
        }
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, RecyclerViewDivider.VERTICAL_LIST));
        mAdapter = new CommonAdapter(mData, new Mapping());
        mAdapter.setOnClickListener(this);
        mAdapter.setOnLongClickListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CODE && resultCode == RESULT_OK) {
            Log.i("tag", "onActivityResult");
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sortList() {
        mLocationClient.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation_list, menu);
        mItem = menu.findItem(R.id.action_done);
        mItem_add = menu.findItem(R.id.add_more);
        if (mData.size() > 0 && mData.get(0).isReady()) {
            mItem.setTitle("完成配送");
            mItem_add.setVisible(false);
            mItem_add.setEnabled(false);
            getSupportActionBar().setTitle("规划结果");
        } else {
            getSupportActionBar().setTitle("配单查看");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_more:
                Intent intent = new Intent(NavigationListActivity.this, AddMissionActivity.class);
                intent.putExtra(AddMissionFragment.PARAM_POSITION, -2);
                startActivityForResult(intent, EDIT_CODE);
                return true;
            case R.id.action_done:
                if (isReady) {
                    new AlertDialog.Builder(this)
                            .setMessage("完成配送")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    App.getBeanList().clear();
                                    Intent intent = new Intent(NavigationListActivity.this, AddMissionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                } else {
                    isReady = true;
                    mItem.setTitle("完成配送");
                    mItem_add.setVisible(false);
                    mItem_add.setEnabled(false);
                    getSupportActionBar().setTitle("规划结果");
                    for (DistributionBean bean : mData) {
                        bean.setReady(true);
                    }
                    sortList();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewClick(View view, int position, Visitable data) {
        final DistributionBean bean = (DistributionBean) data;
        switch (view.getId()) {
            case R.id.tv_edit:
                if (bean.isReady()) {
                    PlaceSuggestionBean.ResultBean.LocationBean l = bean.getDetail().getLocation();
                    NavParamEntity entity = new NavParamEntity(new Location(l.getLng(), l.getLat()));
                    new ThirdPartyMapManager(this, entity).navigation();
                } else {
                    Intent intent = new Intent(NavigationListActivity.this, AddMissionActivity.class);
                    intent.putExtra(AddMissionFragment.PARAM_POSITION, position);
                    startActivityForResult(intent, EDIT_CODE);
                }
                break;
            case R.id.tv_phone:
                requestRuntimePermissions(new String[]{"android.permission.CALL_PHONE"}, new OnRuntimePermissionResultListener() {
                    @Override
                    public void onDenied(String[] permission) {
                        //do nothing
                    }

                    @Override
                    public void onGranted() {
                        tel(bean.getPhone());
                    }
                });
                break;
        }
    }

    @Override
    public boolean onViewLongClick(View view, int position, final Visitable data) {
        new AlertDialog.Builder(this)
                .setMessage("是否删除该条？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getBeanList().remove(data);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .show();
        return false;
    }

    public void tel(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        String tel = "tel:";
        phone = phone.replace(" ", "");
        if (phone.contains("-")) {
            tel += phone.replace("-", ",");
        } else if (phone.contains("转")) {
            tel += phone.replace("转", ",");
        } else {
            tel += phone;
        }
        intent.setData(Uri.parse(tel));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
