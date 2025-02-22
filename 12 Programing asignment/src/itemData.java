//Inventory managment system

import java.io.*;
import java.util.Scanner;//used to take user input


public class itemData
{//contains the main method
    public static void main(String args[])
    {//Used as a menu for when the program is started/after a user has completed an action and to create the items txt file if not already created

        File itemsObj = new File("items");
        try{itemsObj.createNewFile();}//Try to create the items file(if it already exists this will be ignored)
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't create text file to store items. Make sure permissions alloted to program allow this");}//Catches exception for when file cant be created
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        File transactionsObj = new File("transactions.txt");
        try{transactionsObj.createNewFile();}//Try to create the transactions file for the daily report(if it already exists this will be ignored)
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't create text file to store items. Make sure permissions alloted to program allow this");}//Catches exception for when file cant be created
        catch(IOException unknownException){System.out.println("An unknown error has occured");}

        Scanner input = new Scanner(System.in);//creates an object to take user input
        inventory callInventory = new inventory();//creates an object to run the methods inside of the inventory class
        tranInfo callTranInfo = new tranInfo();//create an object to run method(s) inside of the transactionInfo class

        System.out.println("I N V E N T O R Y    M A N A G E M E N T    S Y S T E M");
        System.out.println("-----------------------------------------------");
        System.out.println("1. UPDATE QUANTITY OF EXISTING ITEM");
        System.out.println("2. SEARCH FOR AND VIEW AN ITEM");
        System.out.println("3. DAILY REPORT");
        System.out.println("4. ADD ITEM");
        System.out.println("5. REMOVE ITEM");
        System.out.println("---------------------------------");
        System.out.println("6. Exit");

        try{//Takes user input and finds option user entered
            System.out.print("\n Enter a choice and Press ENTER to continue[1-6]:");
            int userInput = input.nextInt();//takes user input for what option they want to choose


        while(userInput !=6)//while user input isnt 6(exit)
        {
            if (userInput > 6 || userInput < 1) {//filters out invalid inputs
                System.out.println("This doesn't appear to be a valid option...!");
                break;
            }
            if (userInput == 1) {//calls UpdateQuntity if selected by the user
                callInventory.updateQuntity();
                break;
            } else if (userInput == 2) {//calls Search if selected by the user
                System.out.println("Search");
                callInventory.search();
                break;
            } else if (userInput == 3) {//calls DailyReport if selected by the user
                System.out.println("Daily report");
                callTranInfo.dailyReport();
                break;
            } else if (userInput == 4) {//calls AddItem if selected by the user
                System.out.println("Add item");
                callInventory.addItem();
                break;
            } else if (userInput == 5) {//calls RemoveItem if selected by the user
                System.out.println("Remove item");
                callInventory.removeItem();
                break;
            }
        }


            System.out.println("\n\n Thanks for using this program...!");
        }
        catch(java.util.InputMismatchException banana){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter a number from 1-6");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }
    }
}
class inventory{//Includes the methods which perform operations(update quntity, search, daily report, add item, remove item)
    public void updateQuntity(){//updates the quntity of an item
        String productUpdateName = "banana";
        String increaseOrDecrease = "banana";
        int magnitudeOfChange = 34;
        try {//take user input
            Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
            System.out.println("Update quntity");
            System.out.println("Please enter the name of the product to update the quntity of:");
            productUpdateName = input.nextLine();//takes the input of the name of the product that the user wants to update the quntity of

            System.out.println("Do you want to increase or decrease the quntity in stock(i/d):");
            increaseOrDecrease = input.nextLine();//takes user input as to weather they want to increase or deacrease stock level

            System.out.println("How much do you want to increase/decrease the quntity in stock by:");
            magnitudeOfChange = input.nextInt();//takes input of how much the user wants to change the stock by
        }
        catch(java.util.InputMismatchException banana){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter the amount you want to change the quntity by as a number");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            int lineNum = 1;//will store the line number of the line that will be updated
            while (lineContents != null)//while line is not empty
                {//goes through  each line of items.txt searching for the string that the user entered untill an empty line is hit or the string is found
                    if (lineContents.contains("," + productUpdateName + ",")) {
                        wasFound = true;
                        break;
                    }
                    lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
                    lineNum++;//updates the line number to the new line
                }
            if (wasFound == true) {//if the name was found then atempt to update the item quntity
                String[] values = lineContents.split("\\s*,\\s*");//puts each of the lines values into an array
                int quntity = Integer.parseInt(values[3]);//set quntity to the stocklevel in integer form
                String plusOrMinus = "";//will be used to indicate in transactions.txt weather the stock increased or decreased
                switch (increaseOrDecrease) {//checks weather the user wants to increase or decrease the stock
                    case "i":
                        quntity += magnitudeOfChange;//increase the stock by the amount the ser entered
                        plusOrMinus = "StockIncrease: ";
                        break;
                    case "d":
                        quntity -= magnitudeOfChange;//decrease the stock by the amount the ser entered
                        plusOrMinus = "Sold: ";//will be used to indicate in transactions.txt weather the stock increased or decreased
                        break;
                    default:
                        System.out.println("invalid input for increase or decrease; please enter ether i for increase or d for decrease");
                        inventory restart = new inventory();
                        restart.updateQuntity();
                        break;
                }
                String quntityString = String.valueOf(quntity);//convert quntity to a string so that it can be added back into the text file
                String updatedQuntity = values[0] + "," + values[1] + "," + values[2] + "," + quntityString + "," + values[4];//put all of the fields of the updated line back into one string

                FileReader readTwo = new FileReader("items.txt");
                BufferedReader buffReadTwo = new BufferedReader(readTwo);

                String lineContentsTwo = buffReadTwo.readLine();//create a variable to store each lines contents
                int lineNumTwo = 1;//create a variable to hold the number of the line being examined
                String newFileContents = "";//create a variable to hold the updated file contents
                while (lineContentsTwo != null) {//while lines with text are still being read
                    if (lineNumTwo != lineNum & lineContentsTwo != null)//if the current line being examined isn't the one that we updated
                    {
                        newFileContents += lineContentsTwo + "\n";//add the current line being examined to newFileContents
                    } else if (lineNumTwo == lineNum & lineContentsTwo != null) {//if the line is the one with the updates quntity
                        newFileContents += updatedQuntity + "\n";//adds the updated line to newFileContents
                    }
                    lineContentsTwo = buffRead.readLine();//sets the string LineContents to the contents of the next line
                    lineNumTwo++;
                }
                read.close();
                readTwo.close();
                PrintWriter updateFile = new PrintWriter("items.txt");
                updateFile.println(newFileContents);//Replaces the pre-existing contents of the file with the contents of the file minus the line selected by the user
                updateFile.close();

                try (LineNumberReader lineNumReadTransactions = new LineNumberReader(new FileReader(new File("transactions.txt")))) {//used to find the amount of lines in the text file so that the apropraite transaction ID can be generated
                    int idAdd;
                    lineNumReadTransactions.skip(Long.MAX_VALUE);//skips to the last line in the text file
                    idAdd = lineNumReadTransactions.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
                    if (idAdd == 0) {
                        idAdd++;
                    }//if its the first item to be added to transactions.txt set its ID to 1

                    FileWriter writeTransactions = new FileWriter("transactions.txt", true);//creates a FileWriter to append to transactions.txt
                    writeTransactions.write(System.getProperty("line.separator") + idAdd + "," + values[1] + "," + plusOrMinus + magnitudeOfChange + "," + quntityString + "," + "QuntityUpdate");//add the log to transactions.txt
                    writeTransactions.close();//closes transactions.txt
                } catch (FileNotFoundException fileNotFound) {
                    System.out.println("Couldn't find items.txt/transactions.txt");
                }//Catches exception for if a file can not be found
                catch (SecurityException SecException) {
                    System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");
                }//catches exception for if items.txt or transactions.txt can not be writen to
                catch (Exception unknownException) {
                    System.out.println("An unknown error has occured");
                }

            } else {
                System.out.print("item not found");
            }
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Couldn't find items.txt/transactions.txt");
        }//Catches exception for if a file can not be found
        catch (SecurityException SecException) {
            System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");
        }//catches exception for if items.txt or transactions.txt can not be writen to
        catch (Exception unknownException) {
            System.out.println("An unknown error has occured");
        }
        System.out.println("\n-----------------------------------------------");
        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method


    }
    public void search() {//searches for and displays details about an item
        Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
        System.out.println("Please enter the name of the item that you want to search for:");
        String productNameSearch = input.nextLine();//takes input from the user as to what item they want to search for

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            while (lineContents != null)//while line is not empty
            {//goes through  each line of items.txt searching for the string that the user entered untill an empty line is hit or the string is found
                if (lineContents.contains("," + productNameSearch + ",")) {
                    wasFound = true;
                    break;
                }
                lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
            }
            read.close();
            if(wasFound == true){System.out.println(lineContents);}//if the string was found then print the contents of the line that it was found in
            else{System.out.print("item not found");}

        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(Exception unknownException){System.out.println("An unknown error has occured");}


        System.out.println("\n-----------------------------------------------");
        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
    public void addItem() {//adds an item catagory to the database
        String nameAdd = "";
        float priceAdd = 1f;
        int stockAdd = 1;
        try {
            Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
            System.out.println("Please enter the name of the item that you want to add:");
            nameAdd = input.nextLine();//takes input on the name of the item to add

            System.out.println("please enter the price of the item that you want to add:");
            priceAdd = input.nextFloat();//takes input on the price of the item to add

            System.out.println("What is the current stocklevel of the item that you are adding:");
            stockAdd = input.nextInt();//takes input on the current stocklevel of the item to add
        }
        catch(java.util.InputMismatchException banana){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter the amount you want to change the quntity by as a number");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }

        Float totalPriceAdd = priceAdd * stockAdd;//Works out the total price of the item to add

        int IdAdd = 0;
        try (LineNumberReader lineNumRead = new LineNumberReader(new FileReader(new File("items.txt")))) {//used to find the amount of lines in the text file so that the apropraite item ID can be generated
            lineNumRead.skip(Long.MAX_VALUE);//skips to the last line in the text file
            IdAdd = lineNumRead.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
            if(IdAdd == 0){IdAdd++;}//if its the first item to be added to items.txt set its ID to 1
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        try{//appends the new item to items.txt
            FileWriter write = new FileWriter("items.txt", true);
            write.write(System.getProperty( "line.separator" ) + IdAdd + "," + nameAdd + "," + priceAdd + "," + stockAdd + "," + totalPriceAdd);//add the new item to items.txt
            write.close();//closes items.txt
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        try (LineNumberReader lineNumReadTransactions = new LineNumberReader(new FileReader(new File("transactions.txt")))) {//adds the event log to transactions.txt
            int idAdd;
            lineNumReadTransactions.skip(Long.MAX_VALUE);//skips to the last line in the text file
            idAdd = lineNumReadTransactions.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
            if(idAdd == 0){idAdd++;}//if its the first item to be added to transactions.txt set its ID to 1

            FileWriter writeTransactions = new FileWriter("transactions.txt", true);//creates a FileWriter to append to transactions.txt
            writeTransactions.write(System.getProperty( "line.separator" ) + idAdd + "," + nameAdd + "," + "Null" + "," + stockAdd + "," + "AddItem");//add the log to transactions.txt
            writeTransactions.close();//closes transactions.txt
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
        catch(Exception unknownException){System.out.println("An unknown error has occured");}


        System.out.println("\n-----------------------------------------------");
        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
    public void removeItem() {//removes an item catagory from the database
        Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
        System.out.println("Please enter the name of the item that you wish to remove:");
        String nameRemove = input.nextLine();//takes the user input of the name of the item to remove

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            int LineNum = 0;//will store the number of the line which is currently being looked through / the string was found in
            while (lineContents != null)//while line is not empty
            {//goes through  each line of items.txt searching for the string that the user entered untill an empty line is hit or the string is found
                LineNum++;//increament LineNum to the current line being examined
                if (lineContents.contains("," + nameRemove + ",")) {
                    wasFound = true;
                    break;
                }
                lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
            }
            if(wasFound == true){//if the string was found rewrite the file without the line that the user wants to remove
                BufferedReader buffRead2 = new BufferedReader(read);
                String newFileContents = "";//String used to hold the file contents - the line that is being removed
                int lineNum2 = 1;
                while (lineContents != null) {//while lines with text are still being read
                    lineContents = buffRead.readLine();//sets the string LineContents to the contents of the line
                    if(LineNum != lineNum2 & lineContents != null)//if the current line being examined isn't the one to remove add it to NewFileContents
                    {
                        newFileContents += lineContents + "\n";//add the current line being examined to NewFileContents
                    }
                }
                read.close();
                PrintWriter updateFile = new PrintWriter("items.txt");
                updateFile.println(newFileContents);//Replaces the pre-existing contents of the file with the contents of the file minus the line selected by the user
                updateFile.close();
            }
            else{System.out.print("item not found");}

        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        System.out.println("\n-----------------------------------------------");
        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
}
class tranInfo{
    public void dailyReport() {//creates a daily report for the actions which have taken place in the current day
        try {//Prints the contents of the daily transactions report to the user
            FileReader read = new FileReader("transactions.txt");
            BufferedReader buffRead = new BufferedReader(read);
            System.out.println("ID, Description, QuntityChange, StockRemaining, TransactionType");
            String lineContents = buffRead.readLine();//creates a variable called LineContents to store the contents of the current line untill the end of the test is hit
            while (lineContents != null) {//while we are still proccesing lines with text
                System.out.println(lineContents);//creates LineContents which stores the contents of each line
                lineContents = buffRead.readLine();//set lineContents value to the contents of the next line to be examined
            }
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        System.out.println("\n-----------------------------------------------");
        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
}