/*
 * Copyright (C) 2009 University of Washington
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

package org.sdrc.dgacg.collect.android.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sdrc.dgacg.collect.android.logic.HierarchyElement;
import org.sdrc.dgacg.collect.android.utilities.TextUtils;
import org.sdrc.dgacg.collect.android.utilities.ViewIds;

public class HierarchyElementView extends RelativeLayout {

    private TextView primaryTextView;
    private TextView secondaryTextView;
    private ImageView icon;

    public HierarchyElementView(Context context) {
        super(context);
    }

    public HierarchyElementView(Context context, HierarchyElement it) {
        super(context);

        icon = new ImageView(context);
        icon.setImageDrawable(it.getIcon());
        icon.setId(ViewIds.generateViewId());
        icon.setPadding(0, 0, dipToPx(4), 0);

        addView(icon, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        primaryTextView = new TextView(context);
        primaryTextView.setTextAppearance(context, android.R.style.TextAppearance_Large);
        setPrimaryText(it.getPrimaryText());
        primaryTextView.setId(ViewIds.generateViewId());
        primaryTextView.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams l =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
        l.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        addView(primaryTextView, l);

        secondaryTextView = new TextView(context);
        secondaryTextView.setText(it.getSecondaryText());
        secondaryTextView.setTextAppearance(context, android.R.style.TextAppearance_Small);
        secondaryTextView.setGravity(Gravity.CENTER_VERTICAL);

        LayoutParams lp =
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, primaryTextView.getId());
        lp.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        addView(secondaryTextView, lp);

        setPadding(dipToPx(8), dipToPx(4), dipToPx(8), dipToPx(8));

    }


    public void setPrimaryText(String text) {
        primaryTextView.setText(TextUtils.textToHtml(text));
    }


    public void setSecondaryText(String text) {
        secondaryTextView.setText(TextUtils.textToHtml(text));
    }


    public void setIcon(Drawable icon) {
        this.icon.setImageDrawable(icon);
    }

    public void showSecondary(boolean bool) {
        if (bool) {
            secondaryTextView.setVisibility(VISIBLE);
            setMinimumHeight(dipToPx(64));

        } else {
            secondaryTextView.setVisibility(GONE);
            setMinimumHeight(dipToPx(32));

        }
    }

    public int dipToPx(int dip) {
        return (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
    }
}
