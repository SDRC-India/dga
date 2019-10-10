package org.sdrc.dgacg.collect.android.activities;

import java.util.ArrayList;
import java.util.HashMap;



import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;

/**
 * This class is responsible for showing the program to the user. The user will
 * choose the programs of which they want to download the forms.
 *
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 *
 */
public class ProgramsActivity extends ListActivity {

    private static final String PROGRAMNAME = "programname";
    private static final String PROGRAMID = "programid";

    private SimpleAdapter mFormListAdapter;
    private ArrayList<HashMap<String, String>> mProgramList;
    private Button mDownloadButton;
    private Button mToggleButton;

    private boolean mToggled = false;

    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programs_layout);
        db = openOrCreateDatabase("SDRCCollectDB", MODE_PRIVATE, null);

        mDownloadButton = (Button) findViewById(R.id.get_selected_button);
        mDownloadButton.setEnabled(selectedItemCount() > 0);
        mDownloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadSelectedFiles();
                mToggled = false;
                clearChoices();

            }
        });

        mToggleButton = (Button) findViewById(R.id.toggle_button_for_programs);
        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // toggle selections of items to all or none
                ListView ls = getListView();
                mToggled = !mToggled;

                Collect.getInstance().getActivityLogger().logAction(this, "toggleFormCheckbox",
                        Boolean.toString(mToggled));

                for (int pos = 0; pos < ls.getCount(); pos++) {
                    ls.setItemChecked(pos, mToggled);
                }

                mDownloadButton.setEnabled(!(selectedItemCount() == 0));
            }
        });

        mProgramList = getProgramListFromDatabase();
        String[] data = new String[] { PROGRAMNAME, PROGRAMID, PROGRAMNAME };
        int[] view = new int[] { R.id.text1, R.id.text2 };

        mFormListAdapter = new SimpleAdapter(this, mProgramList, R.layout.two_item_multiple_choice, data, view);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setItemsCanFocus(false);
        setListAdapter(mFormListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView ls = getListView();
        for (int pos = 0; pos < ls.getCount(); pos++)
            ls.setItemChecked(pos, false);
    }

    private void clearChoices() {
        ProgramsActivity.this.getListView().clearChoices();
        mDownloadButton.setEnabled(false);
    }

    protected void downloadSelectedFiles() {
        // // TODO Auto-generated method stub

        // deselect all the program
        db.execSQL("UPDATE " + getString(R.string.program_table_name) + " set selected = 0;");
        SparseBooleanArray sba = getListView().getCheckedItemPositions();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (sba.get(i, false)) {
                HashMap<String, String> item = (HashMap<String, String>) getListAdapter().getItem(i);
                int id = Integer.parseInt((item.get(PROGRAMID)).split(" ")[1]);
                db.execSQL("UPDATE " + getString(R.string.program_table_name) + " set selected = 1 where id = " + id
                        + ";");
            }
        }

        Intent i = new Intent(getApplicationContext(), FormDownloadList.class);
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mDownloadButton.setEnabled(!(selectedItemCount() == 0));
    }

    /**
     * returns the number of items currently selected in the list.
     *
     * @return
     */
    private int selectedItemCount() {
        int count = 0;
        SparseBooleanArray sba = getListView().getCheckedItemPositions();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (sba.get(i, false)) {
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @return mProgramList which will get pushed to the program lists.
     */
    private ArrayList<HashMap<String, String>> getProgramListFromDatabase() {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String, String>> mProgramList = new ArrayList<HashMap<String, String>>();

        Cursor resultSet = db.rawQuery("Select * from " + getString(R.string.program_table_name), null);
        if (resultSet.moveToFirst()) {
            do {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(PROGRAMNAME, resultSet.getString(1));
                item.put(PROGRAMID, "ID: " + String.valueOf(resultSet.getInt(0)));
                item.put(PROGRAMNAME, resultSet.getString(1));
                mProgramList.add(item);

            } while (resultSet.moveToNext());
        }

        return mProgramList;
    }
}
