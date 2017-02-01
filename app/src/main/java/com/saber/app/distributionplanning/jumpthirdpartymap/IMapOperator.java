package com.saber.app.distributionplanning.jumpthirdpartymap;

import android.app.Activity;

/**
 * Class description
 *
 * @author ex-huxibing552
 * @date 2017-01-18 16:18
 */
public abstract class IMapOperator {

    protected OnFailListener mOnFailListener;

    abstract void location(Activity activity);

    abstract void navigation(Activity activity,NavParamEntity entity);

    public void setOnFailListener(OnFailListener listener) {
        mOnFailListener = listener;
    }

    public interface OnFailListener {
        void onFail();
    }

}
