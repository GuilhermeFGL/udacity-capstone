package com.guilhermefgl.rolling.view.breakpoint;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class BreakPointItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private BreakPointItemTouchListener mListener;
 
    public BreakPointItemTouchHelper(int dragDirs, int swipeDirs,
                                     BreakPointItemTouchListener listener) {
        super(dragDirs, swipeDirs);
        this.mListener = listener;
    }
 
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof BreakPointAdapter.ItemViewHolder) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        }
        return 0;
    }
 
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(((BreakPointAdapter.ItemViewHolder) viewHolder)
                    .getViewForeground());
        }
    }
 
    @Override
    public void onChildDrawOver(Canvas canvas, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(canvas, recyclerView,
                ((BreakPointAdapter.ItemViewHolder) viewHolder).getViewForeground(),
                dX, dY, actionState, isCurrentlyActive);
    }
 
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((BreakPointAdapter.ItemViewHolder) viewHolder)
                .getViewForeground());
    }
 
    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(canvas, recyclerView,
                ((BreakPointAdapter.ItemViewHolder) viewHolder).getViewForeground(),
                dX, dY, actionState, isCurrentlyActive);
    }
 
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onBreakPointItemSwiped(viewHolder.getAdapterPosition());
    }
 
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
 
    public interface BreakPointItemTouchListener {
        void onBreakPointItemSwiped(int position);
    }
}