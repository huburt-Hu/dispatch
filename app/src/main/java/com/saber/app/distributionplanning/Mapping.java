package com.saber.app.distributionplanning;

import com.saber.app.distributionplanning.base_adapter.BaseViewHolder;
import com.saber.app.distributionplanning.base_adapter.IMapping;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-24 16:58
 */
public class Mapping implements IMapping {

    public static final int LAYOUT = R.layout.item_view;

    @Override
    public Class<? extends BaseViewHolder> getViewHolder(int type) {
        return DistributionViewHolder.class;
    }
}
