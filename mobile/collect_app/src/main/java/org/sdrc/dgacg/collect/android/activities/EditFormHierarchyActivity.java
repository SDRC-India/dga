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


package org.sdrc.dgacg.collect.android.activities;


import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import org.javarosa.core.model.FormIndex;
import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.adapters.HierarchyListAdapter;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.exception.JavaRosaException;
import org.sdrc.dgacg.collect.android.logic.FormController;
import org.sdrc.dgacg.collect.android.logic.HierarchyElement;

import java.util.ArrayList;

import timber.log.Timber;

public class EditFormHierarchyActivity extends FormHierarchyActivity {

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        HierarchyElement h = (HierarchyElement) listView.getItemAtPosition(position);
        FormIndex index = h.getFormIndex();
        if (index == null) {
            goUpLevel();
            return;
        }

        switch (h.getType()) {
            case EXPANDED:
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "onListItemClick",
                        "COLLAPSED", h.getFormIndex());
                h.setType(COLLAPSED);
                ArrayList<HierarchyElement> children = h.getChildren();
                for (int i = 0; i < children.size(); i++) {
                    formList.remove(position + 1);
                }
                h.setIcon(ContextCompat.getDrawable(this, R.drawable.expander_ic_minimized));
                break;
            case COLLAPSED:
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "onListItemClick",
                        "EXPANDED", h.getFormIndex());
                h.setType(EXPANDED);
                ArrayList<HierarchyElement> children1 = h.getChildren();
                for (int i = 0; i < children1.size(); i++) {
                    Timber.i("adding child: %s", children1.get(i).getFormIndex());
                    formList.add(position + 1 + i, children1.get(i));

                }
                h.setIcon(ContextCompat.getDrawable(this, R.drawable.expander_ic_maximized));
                break;
            case QUESTION:
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "onListItemClick",
                        "QUESTION-JUMP", index);
                Collect.getInstance().getFormController().jumpToIndex(index);
                if (Collect.getInstance().getFormController().indexIsInFieldList()) {
                    try {
                        Collect.getInstance().getFormController().stepToPreviousScreenEvent();
                    } catch (JavaRosaException e) {
                        Timber.d(e);
                        createErrorDialog(e.getCause().getMessage());
                        return;
                    }
                }
                setResult(RESULT_OK);
                finish();
                return;
            case CHILD:
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "onListItemClick",
                        "REPEAT-JUMP", h.getFormIndex());
                Collect.getInstance().getFormController().jumpToIndex(h.getFormIndex());
                setResult(RESULT_OK);
                refreshView();
                return;
        }

        // Should only get here if we've expanded or collapsed a group
        HierarchyListAdapter itla = new HierarchyListAdapter(this);
        itla.setListItems(formList);
        listView.setAdapter(itla);
        listView.setSelection(position);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "onKeyDown",
                        "KEYCODE_BACK.JUMP", startIndex);
                FormController fc = Collect.getInstance().getFormController();
                if (fc != null) {
                    fc.getTimerLogger().exitView();
                    fc.jumpToIndex(startIndex);
                }
        }
        return super.onKeyDown(keyCode, event);
    }
}
