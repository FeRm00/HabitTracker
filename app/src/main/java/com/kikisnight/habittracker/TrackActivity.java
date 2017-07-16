package com.kikisnight.habittracker;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.kikisnight.habittracker.data.HabitContract.HabitEntry;
import com.kikisnight.habittracker.data.HabitDbHelper;

/**
 * Allows user to create a new habit.
 */
public class TrackActivity extends AppCompatActivity {

    /** EditText field to enter the habits's name */
    private EditText mNameEditText;

    /** EditText field to enter the habit's duration */
    private EditText mDurationEditText;

    /** EditText field to enter the duration's scale */
    private Spinner mScaleSpinner;

    /**
     * Gender of the habit. The possible valid values are in the HabitContract.java file:
     * {@link HabitEntry#SCALE_UNKNOWN}, {@link HabitEntry#SCALE_MINUTS}, or
     * {@link HabitEntry#SCALE_HOURS}.
     */
    private int mScale = HabitEntry.SCALE_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mDurationEditText = (EditText) findViewById(R.id.edit_habit_duration);
        mScaleSpinner = (Spinner) findViewById(R.id.spinner_scale);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the scale of the habit's duration
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_duration_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mScaleSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mScaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.scale_minutes))) {
                        mScale = HabitEntry.SCALE_MINUTS;
                    } else if (selection.equals(getString(R.string.scale_hours))) {
                        mScale = HabitEntry.SCALE_HOURS;
                    } else {
                        mScale = HabitEntry.SCALE_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mScale = HabitEntry.SCALE_UNKNOWN;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();
        int duration = Integer.parseInt(durationString);
        mScaleSpinner = (Spinner) findViewById(R.id.spinner_scale);


        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habits attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_DURATION, duration);
        values.put(HabitEntry.COLUMN_HABIT_SCALE, mScale);

        // Insert a new row for habit tracker in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving a new habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}