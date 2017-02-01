package com.saber.app.distributionplanning.jumpthirdpartymap;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app.widget.IOSActionSheetDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 调用第三方地图导航
 *
 * @author ex-huxibing552
 * @date 2017-01-19 9:16
 */
public class ThirdPartyMapManager {

    private Activity mActivity;
    private static List<String> sMapList = new ArrayList<>();
    private List<IOSActionSheetDialog.SheetItem> sheetItemList;
    private boolean needShow = true;

    static {
        sMapList.add(BaiduMapOperator.PACKAGE_NAME);
//        sMapList.add("com.example.www");//测试用
        sMapList.add(GaodeMapOperator.PACKAGE_NAME);
        sMapList.add(GoogleMapOperator.PACKAGE_NAME);
    }

    private IOSActionSheetDialog mSheetDialog;
    private NavParamEntity mEntity;

    /**
     *
     * @param activity
     * @param entity 传入火星坐标（GCJ-02）坐标系
     */
    public ThirdPartyMapManager(Activity activity, NavParamEntity entity) {
        mActivity = activity;
        mEntity = entity;
        init();
    }

    private void init() {
        sheetItemList = new ArrayList<>();
        Iterator<String> iterator = sMapList.iterator();
        while (iterator.hasNext()) {
            String packageName = iterator.next();
            if (OtherMapUtil.checkInstalled(mActivity, packageName)) {
                sheetItemList.add(new IOSActionSheetDialog.SheetItem("使用" + OtherMapUtil.getMapName(packageName) + "导航", "#007aff"));
            } else {
                iterator.remove();
            }
        }
        if (sheetItemList.size() == 0) {
            needShow = false;
            return;
        }
        mSheetDialog = new IOSActionSheetDialog.Builder(mActivity)
                .setTitle("请选择地图")
                .setSheetItemList(sheetItemList)
                .setOnItemClickListener(new IOSActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(LinearLayout parent, View view, int position) {
                        String p = sMapList.get(position);
                        Class mapClass = OtherMapUtil.getMapClass(p);
                        IMapOperator operator = MapOperatorFactory.getOperator(mapClass);
                        operator.navigation(mActivity, mEntity);
                    }
                })
                .setTitleHeight(57)//可选项
                .setTitleSize(20)
                .setItemTextSize(20)
                .setItemHeight(57)
                .setItemTextSize(20)
                .setCancelTextSize(20)
                .setCancelHeight(57)
                .setCancelTextColor("#007aff")
                .build();
    }

    public void navigation() {
        if (needShow) {
            mSheetDialog.show();
        } else {
            Toast.makeText(mActivity,"您尚未安装任何地图应用",Toast.LENGTH_SHORT).show();
        }
    }
}
