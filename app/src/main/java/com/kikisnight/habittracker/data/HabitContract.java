package com.kikisnight.habittracker.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Habits Track app.
 */
public final class HabitContract {

    /*
    * An empty private constructor makes sure that the class is not going to be initialised.
    */
    private HabitContract() {}

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a habit track.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of database table for habits */
        public final static String TABLE_NAME = "habits";

        /**
         * Name of the habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_NAME ="name";

        /**
         * Duration of the habit.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_DURATION = "duration";

        /**
         * Scale in the time of the habit.
         *
         * The only possible values are {@link #SCALE_UNKNOWN}, {@link #SCALE_MINUTS},
         * or {@link #SCALE_HOURS}.
         *
         * Type: INTEGER
         */

        public final static String COLUMN_HABIT_SCALE = "scale";

        /**
         * Possible values for the time scale of the habits.
         */
        public static final int SCALE_UNKNOWN = 0;
        public static final int SCALE_MINUTS = 1;
        public static final int SCALE_HOURS = 2;
    }

}
