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

package org.sdrc.dgacg.collect.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.GroupDef;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;
import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.adapters.HierarchyListAdapter;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.logic.FormController;
import org.sdrc.dgacg.collect.android.logic.HierarchyElement;
import org.sdrc.dgacg.collect.android.preferences.PreferencesActivity;
import org.sdrc.dgacg.collect.android.receivers.NetworkReceiver;
import org.sdrc.dgacg.collect.android.utilities.CompatibilityUtils;
import org.sdrc.dgacg.collect.android.utilities.FormEntryPromptUtils;
import org.sdrc.dgacg.collect.android.utilities.InternetTestUtils;
import org.sdrc.dgacg.collect.android.views.ODKView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public abstract class FormHierarchyActivity extends CollectAbstractActivity implements AdapterView.OnItemClickListener {

    protected static final int CHILD = 1;
    protected static final int EXPANDED = 2;
    protected static final int COLLAPSED = 3;
    protected static final int QUESTION = 4;

    private static final String mIndent = "     ";

    List<HierarchyElement> formList;
    TextView path;

    FormIndex startIndex;
    private FormIndex currentIndex;
    protected Button jumpPreviousButton;
    protected Button jumpBeginningButton;
    protected Button jumpEndButton;
    protected ListView listView;
    private static final int MENU_PREFERENCES = Menu.FIRST;
    private static final int GOOGLE_USER_DIALOG = 1;
    private static final int INSTANCE_UPLOADER = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hierarchy_layout);

        listView = findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        TextView emptyView = findViewById(android.R.id.empty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FormController formController = Collect.getInstance().getFormController();
        // https://github.com/opendatakit/collect/issues/998
        if (formController == null) {
            finish();
            return;
        }

        // We use a static FormEntryController to make jumping faster.
        startIndex = formController.getFormIndex();

        setTitle(formController.getFormTitle());

        path = findViewById(R.id.pathtext);

        jumpPreviousButton = findViewById(R.id.jumpPreviousButton);
        jumpPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "goUpLevelButton",
                        "click");
                goUpLevel();
            }
        });

        jumpBeginningButton = findViewById(R.id.jumpBeginningButton);
        jumpBeginningButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "jumpToBeginning",
                        "click");
                FormController fc = Collect.getInstance().getFormController();
                if (fc != null) {
                    fc.getTimerLogger().exitView();
                    fc.jumpToIndex(FormIndex.createBeginningOfFormIndex());
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        jumpEndButton = findViewById(R.id.jumpEndButton);
        jumpEndButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logInstanceAction(this, "jumpToEnd",
                        "click");
                FormController fc = Collect.getInstance().getFormController();
                if (fc != null) {
                    fc.getTimerLogger().exitView();
                    fc.jumpToIndex(FormIndex.createEndOfFormIndex());
                }
                setResult(RESULT_OK);
                finish();
            }
        });


        refreshView();

        // kinda slow, but works.
        // this scrolls to the last question the user was looking at
        if (getListAdapter() != null && listView != null) {
            emptyView.setVisibility(View.GONE);
            listView.post(new Runnable() {
                @Override
                public void run() {
                    int position = 0;
                    for (int i = 0; i < getListAdapter().getCount(); i++) {
                        HierarchyElement he = (HierarchyElement) getListAdapter().getItem(i);
                        if (shouldScrollToTheGivenIndex(he.getFormIndex(), formController)) {
                            position = i;
                            break;
                        }
                    }
                    listView.setSelection(position);
                }
            });
        }
    }

    private boolean shouldScrollToTheGivenIndex(FormIndex formIndex, FormController formController) {
        return startIndex.equals(formIndex)
                || (formController.indexIsInFieldList(startIndex) && formIndex.toString().startsWith(startIndex.toString()));
    }

    private ListAdapter getListAdapter() {
        return listView.getAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Collect.getInstance().getActivityLogger().logOnStart(this);
    }

    @Override
    protected void onStop() {
        Collect.getInstance().getActivityLogger().logOnStop(this);
        super.onStop();
    }

    protected void goUpLevel() {
        Collect.getInstance().getFormController().stepToOuterScreenEvent();

        refreshView();
    }

    private String getCurrentPath() {
        FormController formController = Collect.getInstance().getFormController();
        FormIndex index = formController.getFormIndex();
        // move to enclosing group...
        index = formController.stepIndexOut(index);

        List<FormEntryCaption> groups = new ArrayList<>();
        while (index != null) {
            groups.add(0, formController.getCaptionPrompt(index));
            index = formController.stepIndexOut(index);
        }
        return ODKView.getGroupsPath(groups.toArray(new FormEntryCaption[groups.size()]));
    }

    public void refreshView() {
        try {
            FormController formController = Collect.getInstance().getFormController();
            // Record the current index so we can return to the same place if the user hits 'back'.
            currentIndex = formController.getFormIndex();

            // If we're not at the first level, we're inside a repeated group so we want to only
            // display
            // everything enclosed within that group.
            String contextGroupRef = "";
            formList = new ArrayList<HierarchyElement>();

            // If we're currently at a repeat node, record the name of the node and step to the next
            // node to display.
            if (formController.getEvent() == FormEntryController.EVENT_REPEAT) {
                contextGroupRef =
                        formController.getFormIndex().getReference().toString(true);
                formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
            } else {
                FormIndex startTest = formController.stepIndexOut(currentIndex);
                // If we have a 'group' tag, we want to step back until we hit a repeat or the
                // beginning.
                while (startTest != null
                        && formController.getEvent(startTest) == FormEntryController.EVENT_GROUP) {
                    startTest = formController.stepIndexOut(startTest);
                }
                if (startTest == null) {
                    // check to see if the question is at the first level of the hierarchy. If it
                    // is,
                    // display the root level from the beginning.
                    formController.jumpToIndex(FormIndex
                            .createBeginningOfFormIndex());
                } else {
                    // otherwise we're at a repeated group
                    formController.jumpToIndex(startTest);
                }

                // now test again for repeat. This should be true at this point or we're at the
                // beginning
                if (formController.getEvent() == FormEntryController.EVENT_REPEAT) {
                    contextGroupRef =
                            formController.getFormIndex().getReference().toString(true);
                    formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
                }
            }

            int event = formController.getEvent();
            if (event == FormEntryController.EVENT_BEGINNING_OF_FORM) {
                // The beginning of form has no valid prompt to display.
                formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
                contextGroupRef =
                        formController.getFormIndex().getReference().getParentRef().toString(true);
                path.setVisibility(View.GONE);
                jumpPreviousButton.setEnabled(false);
            } else {
                path.setVisibility(View.VISIBLE);
                path.setText(getCurrentPath());
                jumpPreviousButton.setEnabled(true);
            }

            // Refresh the current event in case we did step forward.
            event = formController.getEvent();

            // Big change from prior implementation:
            //
            // The ref strings now include the instance number designations
            // i.e., [0], [1], etc. of the repeat groups (and also [1] for
            // non-repeat elements).
            //
            // The contextGroupRef is now also valid for the top-level form.
            //
            // The repeatGroupRef is null if we are not skipping a repeat
            // section.
            //
            String repeatGroupRef = null;

            event_search:
            while (event != FormEntryController.EVENT_END_OF_FORM) {

                // get the ref to this element
                String currentRef = formController.getFormIndex().getReference().toString(true);

                // retrieve the current group
                String curGroup = (repeatGroupRef == null) ? contextGroupRef : repeatGroupRef;

                if (!currentRef.startsWith(curGroup)) {
                    // We have left the current group
                    if (repeatGroupRef == null) {
                        // We are done.
                        break;
                    } else {
                        // exit the inner repeat group
                        repeatGroupRef = null;
                    }
                }

                if (repeatGroupRef != null) {
                    // We're in a repeat group within the one we want to list
                    // skip this question/group/repeat and move to the next index.
                    event =
                            formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
                    continue;
                }

                switch (event) {
                    case FormEntryController.EVENT_QUESTION:

                        FormEntryPrompt fp = formController.getQuestionPrompt();
                        String label = getLabel(fp);
                        if (!fp.isReadOnly() || (label != null && label.length() > 0)) {
                            // show the question if it is an editable field.
                            // or if it is read-only and the label is not blank.
                            String answerDisplay = FormEntryPromptUtils.getAnswerText(fp, this, formController);
                            formList.add(
                                    new HierarchyElement(FormEntryPromptUtils.markQuestionIfIsRequired(label, fp.isRequired()), answerDisplay, null,
                                            QUESTION, fp.getIndex()));
                        }
                        break;
                    case FormEntryController.EVENT_GROUP:
                        // ignore group events
                        break;
                    case FormEntryController.EVENT_PROMPT_NEW_REPEAT:
                        // this would display the 'add new repeat' dialog
                        // ignore it.
                        break;
                    case FormEntryController.EVENT_REPEAT:
                        FormEntryCaption fc = formController.getCaptionPrompt();
                        // push this repeat onto the stack.
                        repeatGroupRef = currentRef;
                        // Because of the guard conditions above, we will skip
                        // everything until we exit this repeat.
                        //
                        // Note that currentRef includes the multiplicity of the
                        // repeat (e.g., [0], [1], ...), so every repeat will be
                        // detected as different and reach this case statement.
                        // Only the [0] emits the repeat header.
                        // Every one displays the descend-into action element.

                        if (fc.getMultiplicity() == 0) {
                            // Display the repeat header for the group.
                            HierarchyElement group =
                                    new HierarchyElement(getLabel(fc), null, ContextCompat
                                            .getDrawable(this, R.drawable.expander_ic_minimized),
                                            COLLAPSED, fc.getIndex());
                            formList.add(group);
                        }
                        String repeatLabel = mIndent + getLabel(fc);
                        if (fc.getFormElement().getChildren().size() == 1 && fc.getFormElement().getChild(0) instanceof GroupDef) {
                            formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
                            FormEntryCaption fc2 = formController.getCaptionPrompt();
                            if (getLabel(fc2) != null) {
                                repeatLabel = getLabel(fc2);
                            }
                        }
                        repeatLabel += " (" + (fc.getMultiplicity() + 1) + ")\u200E";
                        // Add this group name to the drop down list for this repeating group.
                        HierarchyElement h = formList.get(formList.size() - 1);
                        h.addChild(new HierarchyElement(repeatLabel, null, null, CHILD, fc.getIndex()));
                        break;
                }
                event =
                        formController.stepToNextEvent(FormController.STEP_INTO_GROUP);
            }

            HierarchyListAdapter itla = new HierarchyListAdapter(this);
            itla.setListItems(formList);
            listView.setAdapter(itla);

            // set the controller back to the current index in case the user hits 'back'
            formController.jumpToIndex(currentIndex);
        } catch (Exception e) {
            Timber.e(e);
            createErrorDialog(e.getMessage());
        }
    }

    /**
     * Creates and displays dialog with the given errorMsg.
     */
    protected void createErrorDialog(String errorMsg) {
        Collect.getInstance()
                .getActivityLogger()
                .logInstanceAction(this, "createErrorDialog", "show.");

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setTitle(getString(R.string.error_occured));
        alertDialog.setMessage(errorMsg);
        DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Collect.getInstance().getActivityLogger()
                                .logInstanceAction(this, "createErrorDialog", "OK");
                        FormController formController = Collect.getInstance().getFormController();
                        formController.jumpToIndex(currentIndex);
                        break;
                }
            }
        };
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.ok), errorListener);
        alertDialog.show();
    }

    private String getLabel(FormEntryCaption formEntryCaption) {
        return formEntryCaption.getShortText() != null && !formEntryCaption.getShortText().isEmpty()
                ? formEntryCaption.getShortText() : formEntryCaption.getLongText();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Ratikanta
            /*
            The following line of code should be activate in "view sent form" only, not in "edit saved form"
             */
        if(Collect.isViewSentForm) {
            Collect.getInstance().getActivityLogger().logAction(this, "onCreateOptionsMenu", "show");
            super.onCreateOptionsMenu(menu);

            CompatibilityUtils.setShowAsAction(
                    menu.add(0, MENU_PREFERENCES, 0, R.string.resend_form).setIcon(R.drawable.ic_menu_preferences),
                    MenuItem.SHOW_AS_ACTION_NEVER);
            Collect.isViewSentForm = false;
            return true;

        }else{
            return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PREFERENCES:
                Collect.getInstance().getActivityLogger().logAction(this, "onMenuItemSelected", "MENU_PREFERENCES");

                if (NetworkReceiver.running == true) {
                    Toast.makeText(getApplicationContext(), "Background send running, please try again shortly",
                            Toast.LENGTH_SHORT).show();
                } else {

                    if (!InternetTestUtils.isConnected(getApplicationContext())) {

                        Collect.getInstance().getActivityLogger().logAction(this, "uploadButton", "noConnection");

                    } else {

                        // if (mSelected.size() > 0) {
                        //
                        // item selected
                        uploadSelectedFiles();
                        // mToggled = false;
                        // mSelected.clear();
                        // InstanceUploaderList.this.getListView().clearChoices();
                        // mUploadButton.setEnabled(false);
                        // System.out.println("Internet is there");
                        // }
                    }
                }
        }

        return super.onOptionsItemSelected(item);
    }
    private void uploadSelectedFiles() {
        // send list of _IDs.

        long[] instanceIDs = new long[1];
        // long l=getIntent().getLongExtra("ID",0);
        instanceIDs[0] = Collect.getInstance().getFormNoChoosedInViewSentForm();
        /*
         * for (int i = 0; i < mSelected.size(); i++) { instanceIDs[i] =
         * mSelected.get(i); }
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String server = prefs.getString(PreferencesActivity.KEY_PROTOCOL, null);
        // Ratikanta
        if (server != null && server.equalsIgnoreCase(getString(R.string.protocol_google_maps_engine))) {
            // if it's maps engine, start the maps-engine uploader
            // first make sure we have a google account selected

            String googleUsername = prefs.getString(PreferencesActivity.KEY_SELECTED_GOOGLE_ACCOUNT, null);
            if (googleUsername == null || googleUsername.equals("")) {
                showDialog(GOOGLE_USER_DIALOG);
                return;
            }
           /* Intent i = new Intent(this, GoogleMapsEngineUploaderActivity.class);
            i.putExtra(FormEntryActivity.KEY_INSTANCES, instanceIDs);
            startActivityForResult(i, INSTANCE_UPLOADER);*/
        } else {
            // otherwise, do the normal agregate/other thing.
            Intent i = new Intent(this, InstanceUploaderActivity.class);
            i.putExtra(FormEntryActivity.KEY_INSTANCES, instanceIDs);
            startActivityForResult(i, INSTANCE_UPLOADER);
        }
    }

}
