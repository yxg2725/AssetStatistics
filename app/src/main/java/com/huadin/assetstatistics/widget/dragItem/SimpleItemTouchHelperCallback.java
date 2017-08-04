package com.huadin.assetstatistics.widget.dragItem;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.huadin.assetstatistics.adapter.BaseAdapter;
import com.huadin.assetstatistics.adapter.MyAdapter;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;

/**
 * Created by 华电 on 2017/8/4.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
  private ItemTouchHelperAdapter mAdapter;

  public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
    mAdapter = adapter;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeFlags = ItemTouchHelper.LEFT;
    return makeMovementFlags(dragFlags,swipeFlags);
  }

  @Override
  public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
    mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    return true;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
  }

  @Override
  public boolean isItemViewSwipeEnabled() {
    return true;
  }

  @Override
  public boolean isLongPressDragEnabled() {
    return true;
  }


  //限制ImageView长度所能增加的最大值
  private double ICON_MAX_SIZE = 50;
  //ImageView的初始长宽
  private int fixedWidth = 150;

  @Override
  public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);
    //重置改变，防止由于复用而导致的显示问题
    viewHolder.itemView.setScrollX(0);
    ((BaseAdapter.MyHolder)viewHolder).tv.setText("左滑删除");
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((BaseAdapter.MyHolder) viewHolder).iv.getLayoutParams();
    params.width = 150;
    params.height = 150;
    ((BaseAdapter.MyHolder) viewHolder).iv.setLayoutParams(params);
    ((BaseAdapter.MyHolder) viewHolder).iv.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
    //仅对侧滑状态下的效果做出改变
    if (actionState ==ItemTouchHelper.ACTION_STATE_SWIPE){
      //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
      if (Math.abs(dX) <= getSlideLimitation(viewHolder)){
        viewHolder.itemView.scrollTo(-(int) dX,0);
      }
      //如果dX还未达到能删除的距离，此时慢慢增加“眼睛”的大小，增加的最大值为ICON_MAX_SIZE
      else if (Math.abs(dX) <= recyclerView.getWidth() / 2){
        double distance = (recyclerView.getWidth() / 2 -getSlideLimitation(viewHolder));
        double factor = ICON_MAX_SIZE / distance;
        double diff =  (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
        if (diff >= ICON_MAX_SIZE)
          diff = ICON_MAX_SIZE;
        ((BaseAdapter.MyHolder)viewHolder).tv.setText("");   //把文字去掉
        ((BaseAdapter.MyHolder) viewHolder).iv.setVisibility(View.VISIBLE);  //显示眼睛
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((BaseAdapter.MyHolder) viewHolder).iv.getLayoutParams();
        params.width = (int) (fixedWidth + diff);
        params.height = (int) (fixedWidth + diff);
        ((BaseAdapter.MyHolder) viewHolder).iv.setLayoutParams(params);
      }
    }else {
      //拖拽状态下不做改变，需要调用父类的方法
      super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
    }
  }

  /**
   * 获取删除方块的宽度
   */
  public int getSlideLimitation(RecyclerView.ViewHolder viewHolder){
    ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    return viewGroup.getChildAt(1).getLayoutParams().width;
  }
}
