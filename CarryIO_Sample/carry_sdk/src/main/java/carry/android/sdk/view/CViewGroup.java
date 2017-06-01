package carry.android.sdk.view;

import android.view.ViewGroup;

/**
 * Created by leixun on 17/5/31.
 */

public class CViewGroup {
    /**
     * 具体的View
     */
    private ViewGroup mViewGroup;

    /** 优先使用 viewPath 即viewId */
    private String viewPath;

    /** viewId */
    private String viewId;

    public CViewGroup(ViewGroup viewGroup, String viewPath){
        this.viewPath = viewPath;
        this.mViewGroup = viewGroup;
    }

    public ViewGroup getmViewGroup() {
        return mViewGroup;
    }

    public void setmViewGroup(ViewGroup mViewGroup) {
        this.mViewGroup = mViewGroup;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
