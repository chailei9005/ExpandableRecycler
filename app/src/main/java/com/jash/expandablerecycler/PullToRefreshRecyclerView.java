package com.jash.expandablerecycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by jash
 * Date: 16-1-5
 * Time: 下午3:56
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    /**
     * 刷新方向
     * @return
     */
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    /**
     * 创建RecyclerView
     * @param context Context to create view with
     * @param attrs AttributeSet from wrapped class. Means that anything you
     *            include in the XML layout declaration will be routed to the
     *            created View
     * @return
     */
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setId(R.id.ptr_recycler);
        return recyclerView;
    }

    /**
     * 是否到顶端
     * @return
     */
    @Override
    protected boolean isReadyForPullStart() {
        RecyclerView recyclerView = getRefreshableView();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        //没有数据时可以下拉刷新
        if (adapter == null || adapter.getItemCount() == 0) {
            return true;
        } else {
            //第一个可显示的控件
            View view = recyclerView.getChildAt(0);
            //第一个可显示的控件在adapter中的位置
            int position = recyclerView.getChildAdapterPosition(view);
            //如果的第一个item
            if (position == 0) {
                //显示完整
                return view.getTop() == 0;
            }
        }
        return false;
    }

    /**
     * 是否到底端
     * @return
     */
    @Override
    protected boolean isReadyForPullEnd() {
        RecyclerView recyclerView = getRefreshableView();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter == null || adapter.getItemCount() == 0) {
            return false;
        } else {

            View view = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            int position = recyclerView.getChildAdapterPosition(view);

            if (position == adapter.getItemCount() - 1){
                return view.getBottom() <= recyclerView.getHeight();
            }
        }
        return false;
    }
    public void setAdapter(RecyclerView.Adapter adapter){
        getRefreshableView().setAdapter(adapter);
    }
    public void setItemAnimator(RecyclerView.ItemAnimator animator){
        getRefreshableView().setItemAnimator(animator);
    }
}
