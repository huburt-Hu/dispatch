package com.saber.app.distributionplanning.base_adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public abstract class BaseViewHolder<T extends Visitable> extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    protected CommonAdapter.OnClickListener mListener;
    protected CommonAdapter.OnLongClickListener mLongClickListener;
    private T model;
    private int position;

    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    protected <V extends View> V getView(int resID) {
        View view = views.get(resID);
        if (view == null) {
            view = itemView.findViewById(resID);
            views.put(resID, view);
        }
        return (V) view;
    }

    public CommonAdapter.OnClickListener getListener() {
        return mListener;
    }

    public void setListener(CommonAdapter.OnClickListener listener) {
        mListener = listener;
    }

    public void onBind(T model, final int position, CommonAdapter adapter,
                       CommonAdapter.OnClickListener listener, CommonAdapter.OnLongClickListener longClickListener) {
        mListener = listener;
        mLongClickListener = longClickListener;
        this.model = model;
        this.position = position;
        onBindView(model, position, adapter);
    }

    protected void setClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onViewClick(v, position, model);
                }
            }
        });
    }

    protected void setLongClick(View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mLongClickListener != null && mLongClickListener.onViewLongClick(v, position, model);
            }
        });
    }

    protected void removeClick(View view) {
        view.setClickable(false);
    }

    public abstract void onBindView(T model, int position, CommonAdapter adapter);

}
