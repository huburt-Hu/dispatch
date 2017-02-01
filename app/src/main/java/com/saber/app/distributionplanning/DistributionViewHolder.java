package com.saber.app.distributionplanning;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saber.app.distributionplanning.base_adapter.BaseViewHolder;
import com.saber.app.distributionplanning.base_adapter.CommonAdapter;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-24 17:00
 */
public class DistributionViewHolder extends BaseViewHolder<DistributionBean> {


    public DistributionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(DistributionBean model, int position, CommonAdapter adapter) {
        TextView tvAddress = getView(R.id.tv_address);
        TextView tv_num = getView(R.id.tv_num);
        TextView tv_name = getView(R.id.tv_name);
        TextView tv_phone = getView(R.id.tv_phone);
        TextView tv_edit = getView(R.id.tv_edit);
        LinearLayout ll_root = getView(R.id.ll_root);

        tvAddress.setText(App.getContext().getString(R.string.address, model.getAddress()));
        tv_num.setText(App.getContext().getString(R.string.num, model.getTracking()));
        tv_name.setText(App.getContext().getString(R.string.name, model.getName()));

        String phone = model.getPhone();
        if (!App.getContext().getString(R.string.no_data).equals(phone)) {
            SpannableString spannableString = new SpannableString(App.getContext().getString(R.string.phone, phone));
            spannableString.setSpan(new UnderlineSpan(), 3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(App.getContext().getResources().getColor(R.color.colorPrimary)),
                    3, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_phone.setText(spannableString);
            setClick(tv_phone);
        } else {
            tv_phone.setText(App.getContext().getString(R.string.phone, phone));
            removeClick(tv_phone);
        }

        if (model.isReady()) {
            tv_edit.setText("导航");
        } else {
            tv_edit.setText("编辑");
        }

        setClick(tv_edit);
        setLongClick(ll_root);
    }
}
