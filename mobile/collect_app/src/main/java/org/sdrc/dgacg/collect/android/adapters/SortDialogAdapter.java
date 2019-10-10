/*
 * Copyright (C) 2017 Shobhit
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrc.dgacg.collect.android.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.listeners.RecyclerViewClickListener;
import org.sdrc.dgacg.collect.android.utilities.ApplicationConstants;
import org.sdrc.dgacg.collect.android.utilities.ThemeUtils;

public class SortDialogAdapter extends RecyclerView.Adapter<SortDialogAdapter.ViewHolder> {
    private final RecyclerViewClickListener listener;
    private final int selectedSortingOrder;
    private final RecyclerView recyclerView;
    private final ThemeUtils themeUtils;
    private String[] sortList;

    public SortDialogAdapter(Context context, RecyclerView recyclerView, String[] sortList, int selectedSortingOrder, RecyclerViewClickListener recyclerViewClickListener) {
        themeUtils = new ThemeUtils(context);
        this.recyclerView = recyclerView;
        this.sortList = sortList;
        this.selectedSortingOrder = selectedSortingOrder;
        listener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public SortDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sort_item_layout, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.txtViewTitle.setText(sortList[position]);
        viewHolder.imgViewIcon.setImageResource(ApplicationConstants.getSortLabelToIconMap().get(sortList[position]));
        viewHolder.imgViewIcon.setImageDrawable(DrawableCompat.wrap(viewHolder.imgViewIcon.getDrawable()).mutate());

        int color = position == selectedSortingOrder ? themeUtils.getAccentColor() : themeUtils.getPrimaryTextColor();
        viewHolder.txtViewTitle.setTextColor(color);
        DrawableCompat.setTintList(viewHolder.imgViewIcon.getDrawable(), position == selectedSortingOrder ? ColorStateList.valueOf(color) : null);
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sortList.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewTitle;
        ImageView imgViewIcon;

        ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = itemLayoutView.findViewById(R.id.title);
            imgViewIcon = itemLayoutView.findViewById(R.id.icon);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(ViewHolder.this, getLayoutPosition());
                }
            });
        }

        public void updateItemColor(int selectedSortingOrder) {
            ViewHolder previousHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedSortingOrder);
            previousHolder.txtViewTitle.setTextColor(themeUtils.getPrimaryTextColor());
            DrawableCompat.setTintList(previousHolder.imgViewIcon.getDrawable(), null);

            txtViewTitle.setTextColor(themeUtils.getAccentColor());
            DrawableCompat.setTint(imgViewIcon.getDrawable(), themeUtils.getAccentColor());
        }
    }
}
