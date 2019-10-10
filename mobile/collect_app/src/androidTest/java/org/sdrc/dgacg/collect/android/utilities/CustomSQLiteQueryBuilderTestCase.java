/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sdrc.dgacg.collect.android.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdrc.dgacg.collect.android.application.Collect;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CustomSQLiteQueryBuilderTestCase {

    private static final String DATABASE_PATH = Collect.METADATA_PATH + "/test.db";
    private static final String TEST_TABLE_NAME = "testTable";
    private static final String TEST_TABLE_NAME_2 = "testTable2";

    private String[] tableColumns = new String[] {"_id", "col1", "col2", "col3"};

    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void setUp() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null, null);
        dropTable(TEST_TABLE_NAME);
        dropTable(TEST_TABLE_NAME_2);
    }

    @Test
    public void dropTableTest() {
        createTestTable();
        assertTrue(tableExists(TEST_TABLE_NAME));
        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .dropIfExists(TEST_TABLE_NAME)
                .end();
        assertFalse(tableExists(TEST_TABLE_NAME));
    }

    @Test
    public void renameTableTest() {
        createTestTable();
        insertExampleData(TEST_TABLE_NAME);
        assertTrue(tableExists(TEST_TABLE_NAME));
        assertFalse(tableExists(TEST_TABLE_NAME_2));
        checkValues(TEST_TABLE_NAME);
        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .renameTable(TEST_TABLE_NAME)
                .to(TEST_TABLE_NAME_2)
                .end();
        assertFalse(tableExists(TEST_TABLE_NAME));
        assertTrue(tableExists(TEST_TABLE_NAME_2));
        checkValues(TEST_TABLE_NAME_2);
    }

    @Test
    public void copyTableTest() {
        createTestTable();
        insertExampleData(TEST_TABLE_NAME);
        assertTrue(tableExists(TEST_TABLE_NAME));
        assertFalse(tableExists(TEST_TABLE_NAME_2));
        checkValues(TEST_TABLE_NAME);

        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .renameTable(TEST_TABLE_NAME)
                .to(TEST_TABLE_NAME_2)
                .end();

        assertFalse(tableExists(TEST_TABLE_NAME));
        assertTrue(tableExists(TEST_TABLE_NAME_2));
        checkValues(TEST_TABLE_NAME_2);

        createTestTable();

        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .insertInto(TEST_TABLE_NAME)
                .columnsForInsert(tableColumns)
                .select()
                .columnsForSelect(tableColumns)
                .from(TEST_TABLE_NAME_2)
                .end();

        checkValues(TEST_TABLE_NAME);

        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .dropIfExists(TEST_TABLE_NAME_2)
                .end();

        assertTrue(tableExists(TEST_TABLE_NAME));
        assertFalse(tableExists(TEST_TABLE_NAME_2));
    }

    @Test
    public void addColumnTest() {
        createTestTable();
        assertTrue(tableExists(TEST_TABLE_NAME));

        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .alter()
                .table(TEST_TABLE_NAME)
                .addColumn("col4", "text")
                .end();

        Cursor cursor = sqLiteDatabase.query(TEST_TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = cursor.getColumnNames();

        assertEquals(5, columnNames.length);
        assertEquals("col1", columnNames[1]);
        assertEquals("col2", columnNames[2]);
        assertEquals("col3", columnNames[3]);
        assertEquals("col4", columnNames[4]);
    }

    private void dropTable(String tableName) {
        CustomSQLiteQueryBuilder
                .begin(sqLiteDatabase)
                .dropIfExists(tableName)
                .end();
        assertFalse(tableExists(tableName));
    }

    private void checkValues(String tableName) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
        int index = 1;
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    assertEquals(String.valueOf(index), cursor.getString(cursor.getColumnIndex("_id")));
                    assertEquals("col" + index + "x1Value", cursor.getString(cursor.getColumnIndex("col1")));
                    assertEquals("col" + index + "x2Value", cursor.getString(cursor.getColumnIndex("col2")));
                    assertEquals("col" + index + "x3Value", cursor.getString(cursor.getColumnIndex("col3")));

                    index++;
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void createTestTable() {
        sqLiteDatabase.execSQL("CREATE TABLE " + TEST_TABLE_NAME + " ("
                + "_id" + " integer primary key, "
                + "col1" + " text not null, "
                + "col2" + " text, "
                + "col3" + " text);");
    }

    private void insertExampleData(String tableName) {
        sqLiteDatabase.execSQL("INSERT INTO " + tableName + " VALUES "
                + "(1, 'col1x1Value', 'col1x2Value', 'col1x3Value'), "
                + "(2, 'col2x1Value', 'col2x2Value', 'col2x3Value'), "
                + "(3, 'col3x1Value', 'col3x2Value', 'col3x3Value');");
    }

    private boolean tableExists(String tableName) {
        boolean isExist = false;
        Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }
}
