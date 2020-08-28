package com.bulahej.tazweeg.utilties;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.bulahej.tazweeg.adapters.MemberMatchingAdapter;

public class SwipeAndDragHelper extends ItemTouchHelper.Callback {
//    public void setmSwipable(Boolean mSwipable) {
//        this.mSwipable = mSwipable;
//    }
//
//    public void setmMoveable(Boolean mMoveable) {
//        this.mMoveable = mMoveable;
//    }
//
//    private Boolean mSwipable, mMoveable;
    private final ActionCompletionContract contract;
    public SwipeAndDragHelper(ActionCompletionContract adapter) {
        contract = adapter;
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
//        return mMoveable;
//        return false;   //to disable the default drag and drop
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
//        return mSwipable;
//        return false;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;      //to enable drag up and down
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;  //to enable swipe left and right
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        contract.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        contract.onRowSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof MemberMatchingAdapter.CustomViewHolder) {
                MemberMatchingAdapter.CustomViewHolder myViewHolder = (MemberMatchingAdapter.CustomViewHolder) viewHolder;
                contract.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof MemberMatchingAdapter.CustomViewHolder) {
            MemberMatchingAdapter.CustomViewHolder myViewHolder = (MemberMatchingAdapter.CustomViewHolder) viewHolder;
            contract.onRowClear(myViewHolder);
        }
    }

    public interface ActionCompletionContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(MemberMatchingAdapter.CustomViewHolder myViewHolder);
        void onRowClear(MemberMatchingAdapter.CustomViewHolder myViewHolder);
        void onRowSwiped(int position);
    }

//    public interface StartDragListener {
//        void requestDrag(RecyclerView.ViewHolder viewHolder);
//    }
}

