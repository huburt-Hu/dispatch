package com.saber.app.distributionplanning.base_adapter;

import android.view.View;


public class ViewHolderFactory implements IViewHolderFactory {

    @Override
    public BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> clazz, View itemView) {
        BaseViewHolder viewHolder = null;
        Class[] parameterTypes = {View.class};
        try {
            java.lang.reflect.Constructor constructor = clazz.getConstructor(parameterTypes);
            Object[] parameters = {itemView};
            viewHolder = (BaseViewHolder) constructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }
}
