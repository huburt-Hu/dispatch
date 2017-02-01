package com.saber.app.distributionplanning.base_adapter;

import android.view.View;

public interface IViewHolderFactory {
    BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> clazz, View itemView);
}
