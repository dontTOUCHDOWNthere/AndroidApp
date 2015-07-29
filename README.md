# ShoppingList
My first try at making an android app.

### Description
Making an app that will let you add item food names and prices. It will save that info to a database with SQLite. Using that database, you can then select foods and add them to your list where the app will keep track of the total price so that you can know about how much money you are going to spend before you even get to the store.

### Checklist

1. create area for adding for to the grocery list
   * :white_check_mark: user types in a add food box
        * :white_check_mark: if no input, alert user

   * :white_check_mark: when user presses "add" button
        * :white_check_mark: if new food
           * :white_check_mark: calculate price for one food item (price / quantity)
           * :white_check_mark: add to main table AND grocery list

        * :white_check_mark: if not new food
           * :white_check_mark: get price from main table, multiply by quantity, and add to grocery list
           
    * :white_check_mark: calculate total for grocery list
        * :white_check_mark: insertion
        * :white_check_mark: deletion of an individual item


2. add profile picture capability
   * :white_check_mark: upload picture from gallery
   * :bulb: save picture

3. use Google Maps API to show nearby grocery store
   * :white_check_mark: get current location
   * :bulb: find nearby grocery stores

4. open Dropbox folder to MealFit plan from Home fragment
   * :white_check_mark: launches Dropbox app when button clicked
   * :x: view Dropbox folder directly from the ShoppingList app

# Demo
* coming soon
