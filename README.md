# Monthly Expenses Tracker
An Android application that keeps track of your monthly expenses. 

Monthly Expenses Tracker can be [downloaded from the Google Play Store](https://play.google.com/store/apps/details?id=com.github.LRE323).

This application is written in Java and uses Room.

A video demonstration can be found [here](https://streamable.com/7d5nzz). 

This demonstration showcases:

* Creating and saving an new expense to the Room database
* Handling of the back button
* Updating an out of date expense
* Deleting an expense
* Sorting the expenses by date


### Home Screen

This activity displays the following items:

* The total sum of all the saved expenses
* A RecyclerView list of all the saved expenses along with relevant information such as expense name, amount, and date. 

The items in the RecyclerView list are sorted before being displayed and out of date expenses are updated when this activity is resumed.

The user can long click and delete an expense in the RecyclerView.

![Image of MainActivity](https://i.imgur.com/FjAobBl.jpg) 


### Creating and Saving a New Expense

This activity allows the user to input the necessary information required to create a new expense and save it to the Room database.

Input validation is executed before attempting to create a new expense.

![Image of AddExpense](https://i.imgur.com/fHznd3l.jpg)
