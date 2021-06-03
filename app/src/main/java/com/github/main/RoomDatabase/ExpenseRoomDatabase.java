package com.github.main.RoomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    version = 1,
    entities = {Expense.class},
    exportSchema = false
)

// The database class for Room must be abstract and extend com.github.main.RoomDatabase.
public abstract class ExpenseRoomDatabase extends RoomDatabase {

    // Declaring an abstract method.
    public abstract ExpenseDao expenseDao();

    // We've defined a singleton, ExpenseRoomDatabase, to prevent having multiple instances of the
    // database open at the same time.
    private static volatile ExpenseRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    // We've created an ExecutorService with a fixed thread pool that you will use to run database
    // operations asynchronously on a background thread.
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // This method returns the singleton. It will create the database the first time it's accessed,
    // using Room's database builder to create a com.github.main.RoomDatabase object in the application context
    // from the ExpenseRoomDatabase class and names it "expense_database".
    static ExpenseRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (ExpenseRoomDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseRoomDatabase.class, "expense_database")
                            //.addMigrations(MIGRATION_1_2)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
