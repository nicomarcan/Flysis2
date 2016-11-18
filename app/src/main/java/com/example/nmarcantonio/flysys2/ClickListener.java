package com.example.nmarcantonio.flysys2;

import android.view.View;

/**
 * Created by nmarcantonio on 18/11/16.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
