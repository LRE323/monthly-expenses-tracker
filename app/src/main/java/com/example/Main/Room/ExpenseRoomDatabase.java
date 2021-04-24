package com.example.Main.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
You annotate the class to be a Room database with @Database and use the annotation parameters to
declare the entities that belong in the database and set the version number. Each entity corresponds
to a table that will be created in the database. Database migrations are beyond the scope of this
codelab, so we set the exportSchema to false here to avoid a build warning. In a real app, you
should consider setting a directory for Room to use to export the schema so you can check the current
schema into your version control system.
*/
@Database(entities = {Expense.class}, version = 1, exportSchema = false)

// The database class for Room must be abstract and extend RoomDatabase.
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
    // using Room's database builder to create a RoomDatabase object in the application context
    // from the ExpenseRoomDatabase class and names it "expense_database".
    static ExpenseRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (ExpenseRoomDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseRoomDatabase.class, "expense_database")
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

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ExpenseDao dao = INSTANCE.expenseDao();

                /*Expense expense1 = new Expense("Spotify", 9.99, "April 1, 2021");
                Expense expense2 = new Expense("Hulu", 11.99, "April 12, 2021");

                dao.insert(expense1);
                dao.insert(expense2);*/
            });
        }
    };
}
