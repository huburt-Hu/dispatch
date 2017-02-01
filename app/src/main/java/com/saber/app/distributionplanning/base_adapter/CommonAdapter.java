package com.saber.app.distributionplanning.base_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private IMapping mMapping;
    private List<? extends Visitable> models;
    private IViewHolderFactory mViewHolderFactory;
    private OnClickListener mListener;
    private OnLongClickListener mLongClickListener;

    public CommonAdapter(List<? extends Visitable> models, IMapping mapping) {
        this(models, mapping, null);
    }

    public CommonAdapter(List<? extends Visitable> models, IMapping mapping, IViewHolderFactory viewHolderFactory) {
        this.models = models;
        this.mMapping = mapping;
        if (viewHolderFactory != null) {
            mViewHolderFactory = viewHolderFactory;
        } else {
            mViewHolderFactory = new ViewHolderFactory();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return mViewHolderFactory.createViewHolder(mMapping.getViewHolder(viewType), itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(models.get(position), position, this, mListener, mLongClickListener);
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public int getItemViewType(int position) {
        return models == null ? 0 : models.get(position).type();
    }

    public OnLongClickListener getOnLongClickListener() {
        return mLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public OnClickListener getOnClickListener() {
        return mListener;
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {

        void onViewClick(View view, int position, Visitable data);
    }

    public interface OnLongClickListener {

        boolean onViewLongClick(View view, int position, Visitable data);
    }

}
