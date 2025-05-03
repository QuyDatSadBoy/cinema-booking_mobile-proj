package com.example.cinema_booking_mobile.others;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state
    ) {
        int position = parent.getChildAdapterPosition(view);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();
        int column = position % spanCount;

        outRect.top = (position >= spanCount) ? space : 0;
        outRect.bottom = 0;

        if (column == 0) { // Cột đầu
            outRect.left = 0;
            outRect.right = space / 2;
        } else { // Cột thứ hai
            outRect.left = space / 2;
            outRect.right = 0;
        }
    }
}

