package com.saber.app.distributionplanning;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AddMissionFragment extends Fragment implements View.OnClickListener {

    public static final int SUGGEST_RESULT = 1;

    public static final String PARAM_POSITION = "position";//-1表示未传值，-2表示添加，>=0表示修改

    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtId;
    private TextView mTvCount;
    private AutoCompleteTextView mActvAddress;
    private LinearLayout mLlExtra;
    private TextView mTvExtra;

    private int position = -1; //位置标识，辨识是否是修改数据

    private boolean isShowExtra;//是否显示选填输入框

    private String lastStr = "";//前一次地址记录，用于必选重复place suggestion 请求
    private long lastTime;

    private ArrayAdapter<String> mAdapter;
    private List<PlaceSuggestionBean.ResultBean> mResultList;
    private PlaceSuggestionBean.ResultBean choosed;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isVisible()) {
                return;
            }
            if (msg.what == SUGGEST_RESULT) {
                mAdapter.clear();
                mResultList.clear();
                String s = mActvAddress.getText().toString();
                PlaceSuggestionBean bean = (PlaceSuggestionBean) msg.obj;
                List<PlaceSuggestionBean.ResultBean> result = bean.getResult();
                if (result != null) {
                    for (PlaceSuggestionBean.ResultBean resultBean : result) {
                        if (resultBean != null) {
                            String hint = resultBean.getName() + " " + resultBean.getCity() + " " + resultBean.getDistrict();
                            if (hint.equals(lastStr)) {
                                return;
                            }
                            mAdapter.add(hint);
                            mResultList.add(resultBean);
                        }
                    }
                }
                mActvAddress.setText(s);
                mActvAddress.setSelection(s.length());
            }
        }
    };


    public static AddMissionFragment newInstance() {

        Bundle args = new Bundle();

        AddMissionFragment fragment = new AddMissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt(PARAM_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_mission, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        ImageView ivDesc = (ImageView) view.findViewById(R.id.iv_extra_desc);
        mTvExtra = (TextView) view.findViewById(R.id.tv_open_extra);
        mTvCount = (TextView) view.findViewById(R.id.tv_count);
        mActvAddress = (AutoCompleteTextView) view.findViewById(R.id.actv);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtId = (EditText) view.findViewById(R.id.et_id);
        mLlExtra = (LinearLayout) view.findViewById(R.id.ll_extra);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete_address);

        ivDelete.setOnClickListener(this);
        fab.setOnClickListener(this);
        mTvExtra.setOnClickListener(this);
        ivDesc.setOnClickListener(this);

        init();

        if (position >= 0) {
            DistributionBean distributionBean = App.getBeanList().get(position);
            if (distributionBean != null) {
                mEtName.setText(distributionBean.getName());
                mEtId.setText(distributionBean.getTracking());
                mEtPhone.setText(distributionBean.getPhone());
                mActvAddress.setText(distributionBean.getAddress());
                fab.setVisibility(View.GONE);
                choosed = distributionBean.getDetail();

            }
        }

        if (App.getBeanList().size() > 0) {
            mTvCount.setVisibility(View.VISIBLE);
            mTvCount.setText(getString(R.string.add_count, App.getBeanList().size()));
        }
    }

    private void init() {
        mResultList = new ArrayList<>();
        List<String> hints = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, hints);
        mActvAddress.setAdapter(mAdapter);
        mActvAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String string = s.toString();
//                Log.i("tag", "string:" + string);
//                Log.i("tag", "lastStr:" + lastStr);
                long current = System.currentTimeMillis();
                Log.i("tag", "current:" + current);
                long d = current - lastTime;
                Log.i("tag", "d:" + d);
                if (!TextUtils.isEmpty(string) && !lastStr.equals(string) && d > 1000) {
                    lastStr = string;
                    lastTime = current;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PlaceSuggestionBean suggestion = Http.getInstance().getPlaceSuggestion(string);
                            if (suggestion != null) {
                                Message msg = mHandler.obtainMessage();
                                msg.obj = suggestion;
                                msg.what = SUGGEST_RESULT;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mActvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastStr = mAdapter.getItem(position);
                choosed = mResultList.get(position);
                mAdapter.clear();
                mResultList.clear();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_extra_desc:
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("包括顾客的单号、手机号和姓名，可以帮助您在配送过程中直接拨打电话，查看单号，可不填")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_open_extra:
                showExtra();
                break;
            case R.id.fab:
                addNew();
                break;
            case R.id.iv_delete_address:
                mActvAddress.getText().clear();
                break;
        }
    }

    private void showExtra() {
        if (isShowExtra) {
            Drawable nav_up = getResources().getDrawable(R.mipmap.arrow_up);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            mTvExtra.setCompoundDrawables(null, null, nav_up, null);
            mTvExtra.setText("收起");
            mLlExtra.setVisibility(View.VISIBLE);
        } else {
            Drawable nav_up = getResources().getDrawable(R.mipmap.arrow_down);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            mTvExtra.setCompoundDrawables(null, null, nav_up, null);
            mTvExtra.setText("展开");
            mLlExtra.setVisibility(View.GONE);
        }
        isShowExtra = !isShowExtra;
    }

    private boolean addNew() {
        String address = mActvAddress.getText().toString();
        String name = mEtName.getText().toString();
        String phone = mEtPhone.getText().toString();
        String tracking = mEtId.getText().toString();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getActivity(), "地址不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (choosed == null) {
            Toast.makeText(getActivity(), "地址有误，请选择提示地址！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            name = getActivity().getString(R.string.no_data);
        }
        if (TextUtils.isEmpty(phone)) {
            phone = getActivity().getString(R.string.no_data);
        }
        if (TextUtils.isEmpty(tracking)) {
            tracking = getActivity().getString(R.string.no_data);
        }

        DistributionBean bean = new DistributionBean(address);
        bean.setName(name);
        bean.setPhone(phone);
        bean.setTracking(tracking);
        bean.setDetail(choosed);
        App.getBeanList().add(bean);

        mTvCount.setVisibility(View.VISIBLE);
        String count = getString(R.string.add_count, App.getBeanList().size());
        SpannableString spannableString = new SpannableString(count);
        mTvCount.setText(spannableString);

        mActvAddress.getText().clear();
        mEtName.getText().clear();
        mEtPhone.getText().clear();
        mEtId.getText().clear();
        choosed = null;
        return true;
    }

    public void onDone(Activity activity) {
        List<DistributionBean> list = App.getBeanList();
        String address = mActvAddress.getText().toString();
        if (list.size() > 0) {
            if (position >= 0) {
                Log.e("tag", "position:" + position);
                DistributionBean bean = App.getBeanList().get(position);
                String name = mEtName.getText().toString();
                String phone = mEtPhone.getText().toString();
                String tracking = mEtId.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (choosed == null) {
                    Toast.makeText(getActivity(), "地址有误，请选择提示地址！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    name = getActivity().getString(R.string.no_data);
                }
                if (TextUtils.isEmpty(phone)) {
                    phone = getActivity().getString(R.string.no_data);
                }
                if (TextUtils.isEmpty(tracking)) {
                    tracking = getActivity().getString(R.string.no_data);
                }
                bean.setDetail(choosed);
                bean.setName(name);
                bean.setPhone(phone);
                bean.setTracking(tracking);
                bean.setAddress(address);
                activity.setResult(Activity.RESULT_OK);
            } else if (position == -2) {
                if (!TextUtils.isEmpty(address)) {
                    addNew();
                }
                activity.setResult(Activity.RESULT_OK);
            } else {
                if (!TextUtils.isEmpty(address)) {
                    addNew();
                }
                Intent intent = new Intent(activity, NavigationListActivity.class);
                activity.startActivity(intent);
            }
        } else {
            if (!addNew()) {
                return;
            }
            Intent intent = new Intent(activity, NavigationListActivity.class);
            activity.startActivity(intent);
        }
        activity.finish();
    }
}
