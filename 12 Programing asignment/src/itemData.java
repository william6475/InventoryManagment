//Inventory managment system

import java.io.*;//used to read and write text files
import java.time.DateTimeException;
import java.util.Scanner;//used to take user input
import java.time.LocalDate;//used to get the current date

/**
 * Contains the main method
 */
public class itemData {
    /**
     * Used as a menu for when the program is started/after a user has completed an action and to create the items.txt and transactions.txt if not already created
     * @param args
     */
    public static void main(String args[]) {

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

        System.out.println("\n-----------------------------------------------");
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

            if(userInput == 6){System.out.println("\n\n Thanks for using this program...!");}

            while(userInput !=6)//while user input isn't 6(exit)
            {
                if (userInput > 6 || userInput < 1) {//filters out invalid inputs
                    System.out.println("This doesn't appear to be a valid option...!");
                    String[] itemDataArgument = {""};//creates a string array to parse to main method
                    itemData.main(itemDataArgument);//returns to main method
                    break;
                }
                if (userInput == 1) {//calls updateQuntity if selected by the user
                    System.out.println("Update Quntity");
                    callInventory.updateQuntity();
                    break;
                } else if (userInput == 2) {//calls Search if selected by the user
                    System.out.println("Search");
                    callInventory.search();
                    break;
                } else if (userInput == 3) {//calls dailyReport if selected by the user
                    System.out.println("Daily report");
                    callTranInfo.dailyReport();
                    break;
                } else if (userInput == 4) {//calls addItem if selected by the user
                    System.out.println("Add item");
                    callInventory.addItem();
                    break;
                } else if (userInput == 5) {//calls removeItem if selected by the user
                    System.out.println("Remove item");
                    callInventory.removeItem();
                    break;
                }
            }
        }
        catch(java.util.InputMismatchException inputMismatchException){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter a number from 1-6");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }
    }
}

/**
 * //Includes the methods which perform operations based around items.txt(update quntity, search, add item, remove item)
 */
class inventory{
    /**
     * updates the quntity of an item
     */
    public void updateQuntity(){
        String productUpdatedescription = "";
        String increaseOrDecrease = "";
        int magnitudeOfChange = 34;
        try {//take user input
            Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
            System.out.println("Please enter the description of the product to update the quntity of:");
            productUpdatedescription = input.nextLine();//takes the input of the description of the product that the user wants to update the quntity of

            System.out.println("Do you want to increase or decrease the quntity in stock(i/d):");
            increaseOrDecrease = input.nextLine();//takes user input as to weather they want to increase or deacrease stock level

            System.out.println("How much do you want to increase/decrease the quntity in stock by:");
            magnitudeOfChange = input.nextInt();//takes input of how much the user wants to change the stock by
        }
        catch(java.util.InputMismatchException inputMismatchException){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter the amount you want to change the quntity by as a number");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }
        if(magnitudeOfChange < 1){//makes sure that the user hasn't entered a number smaller than 1 into magnitude of change
            System.out.println("Please enter a number that is bigger than 0\n" +
                    "");
            inventory restart = new inventory();//creates an object to restart updateQuntity
            restart.updateQuntity();//restarts updateQuntity
        }

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            int lineNum = 1;//will store the line number of the line that will be updated
            while (lineContents != null)//while line is not empty
            {//goes through  each line of items.txt searching for the string that the user entered until an empty line is hit or the string is found
                if (lineContents.contains("," + productUpdatedescription + ",")) {
                    wasFound = true;
                    break;
                }
                lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
                lineNum++;//updates the line number to the new line
            }
            if (wasFound == true) {//if the descriptione was found then atempt to update the item quntity
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
                String updatedQuntity = values[0] + "," + values[1] + "," + values[2] + "," + quntityString + "," + values[4];//put all fields of the updated line back into one string
                FileReader readTwo = new FileReader("items.txt");
                BufferedReader buffReadTwo = new BufferedReader(readTwo);

                String lineContentsTwo = buffReadTwo.readLine();//create a variable to store each lines contents
                int lineNumTwo = 1;//create a variable to hold the number of the line being examined
                String newFileContents = "";//create a variable to hold the updated file contents
                while (lineContentsTwo != null) {//while lines with text are still being read
                    if (lineNumTwo != lineNum & lineContentsTwo != null)//if the current line being examined isn't the one that we updated
                    {
                        if(lineNumTwo!= 1){newFileContents += "\n";}//starts a new line if it is not the first line with content
                        newFileContents += lineContentsTwo;//add the current line being examined to newFileContents
                    } else if (lineNumTwo == lineNum & lineContentsTwo != null) {//if the line is the one with the updates quntity
                        if(lineNumTwo!= 1){newFileContents += "\n";}//starts a new line if it is not the first line with content
                        newFileContents += updatedQuntity;//adds the updated line to newFileContents
                    }
                    lineContentsTwo = buffReadTwo.readLine();//sets the string LineContents to the contents of the next line
                    lineNumTwo++;
                }

                read.close();
                readTwo.close();
                PrintWriter updateFile = new PrintWriter("items.txt");
                updateFile.print(newFileContents);//Replaces the pre-existing contents of the file with the contents of the file minus the line selected by the user
                updateFile.close();
                System.out.println("Item quntity updated succesfully");

                String fullIdAdd = "";
                try (LineNumberReader lineNumReadTransactions = new LineNumberReader(new FileReader(new File("transactions.txt")))) {//used to find the amount of lines in the text file so that the apropraite transaction ID can be generated
                    int idAdd;
                    lineNumReadTransactions.skip(Long.MAX_VALUE);//skips to the last line in the text file
                    idAdd = lineNumReadTransactions.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
                    if (idAdd == 0) {
                        idAdd++;
                    }//if it's the first item to be added to transactions.txt set its ID to 1

                    String[] numZeros = {"", "0000", "000","00", "0"};//Creates an array which adds the corresponding number of 0s depending on the ID length
                    String stringIdAdd = String.valueOf(idAdd);//Converts idAdd to a string so the 0s can be added
                    fullIdAdd = numZeros[stringIdAdd.length()] + idAdd;//Adds the 0s to the id ad stores it in the varibale fullIdAdd
                    LocalDate date = LocalDate.now();//gets the current date

                    FileWriter writeTransactions = new FileWriter("transactions.txt", true);//creates a FileWriter to append to transactions.txt
                    writeTransactions.write(System.getProperty("line.separator") + fullIdAdd + "," + values[1] + "," + plusOrMinus + magnitudeOfChange + "," + quntityString + "," + "QuntityUpdate" + "," + date);//add the log to transactions.txt
                    writeTransactions.close();//closes transactions.txt
                } catch (FileNotFoundException fileNotFound) {
                    System.out.println("Couldn't find items.txt/transactions.txt");
                }//Catches exception for if a file can not be found
                catch (SecurityException SecException) {
                    System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");
                }//catches exception for if items.txt or transactions.txt can not be writen to
                catch(DateTimeException dateTimeExcetpion){System.out.println("Error retriving data");}
                catch (Exception unknownException) {
                    System.out.println("An unknown error has occured");
                }

            }
            else {
                System.out.print("item not found");
                String[] itemDataArgument = {""};//creates a string array to parse to main method
                itemData.main(itemDataArgument);//returns to main method
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

        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method


    }

    /**
     * searches for and displays stored inforamtion about an item
     */
    public void search() {
        Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
        System.out.println("Please enter the description of the item that you want to search for:");
        String productDescriptionSearch = input.nextLine();//takes input from the user as to what item they want to search for

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            while (lineContents != null)//while line is not empty
            {//goes through  each line of items.txt searching for the string that the user entered untill an empty line is hit or the string is found
                if (lineContents.contains("," + productDescriptionSearch + ",")) {
                    wasFound = true;
                    break;
                }
                lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
            }
            read.close();
            if(wasFound == true){//if the string was found then print the contents of the line that it was found in
                System.out.println("\nitem ID, Desctiption, Price(£), Stock, Total price(£)");
                System.out.println(lineContents);
            }
            else{System.out.print("item not found");}

        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }

    /**
     * adds an item type to items.txt
     */
    public void addItem() {
        String descriptionAdd = "";
        float priceAdd = 1f;
        int stockAdd = 1;
        try {//takes user input
            Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
            System.out.println("Please enter the description of the item that you want to add:");
            descriptionAdd = input.nextLine();//takes input on the description of the item to add

            System.out.println("please enter the price of the item that you want to add:");
            priceAdd = input.nextFloat();//takes input on the price of the item to add

            System.out.println("What is the current stocklevel of the item that you are adding:");
            stockAdd = input.nextInt();//takes input on the current stocklevel of the item to add

            if(descriptionAdd.contains(",")) {//makes sure that an item description containing a comma cant be added to tiems.txt
                System.out.println("Item descrptions can't include commas\n");
                inventory restart = new inventory();//creates an object to restart addItem
                restart.addItem();//restarts add item
            }
            if(priceAdd < 0) {//makes sure the user hasnt entered a negitive price
                System.out.println("price can not be negitive\n");
                inventory restart2 = new inventory();//creates an object to restart addItem
                restart2.addItem();//restarts add item
            }
            if(stockAdd < 0){//makes sure that the stock level the user inputed isnt negitive
                System.out.println("stock level can not be negitive\n");
                inventory restart3 = new inventory();//creates an object to restart addItem
                restart3.addItem();//restarts add item
            }


        }
        catch(java.util.InputMismatchException inputMismatchException){//If the user inputs the wrong datatype
            System.out.println("The entered information is was not valid. Please enter the amount you want to change the quntity by as a number");
            String[] itemDataArgument = {""};//creates a string array to parse to main method
            itemData.main(itemDataArgument);//returns to main method
        }

        Float totalPriceAdd = priceAdd * stockAdd;//Works out the total price of the item to add

        int idAdd = 0;
        String fullIdAdd = "";
        try (LineNumberReader lineNumRead = new LineNumberReader(new FileReader(new File("items.txt")))) {//used to find the amount of lines in the text file so that the apropraite item ID can be generated
            lineNumRead.skip(Long.MAX_VALUE);//skips to the last line in the text file
            idAdd = lineNumRead.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
            if(idAdd == 0){idAdd++;}//if its the first item to be added to items.txt set its ID to 1
            String[] numZeros = {"", "0000", "000","00", "0"};//Creates an array which adds the corresponding number of 0s depending on the ID length
            String stringIdAdd = String.valueOf(idAdd);//Converts idAdd to a string so the 0s can be added
            fullIdAdd = numZeros[stringIdAdd.length()] + idAdd;//Adds the 0s to the id ad stores it in the varibale fullIdAdd
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        try{//appends the new item to items.txt and the transaction record to transactions.txt
            FileWriter write = new FileWriter("items.txt", true);
            write.write(System.getProperty( "line.separator" ) + fullIdAdd + "," + descriptionAdd + "," + priceAdd + "," + stockAdd + "," + totalPriceAdd);//add the new item to items.txt
            write.close();//closes items.txt

            String fullIdAdd2 = "";
            try (LineNumberReader lineNumReadTransactions = new LineNumberReader(new FileReader(new File("transactions.txt")))) {//adds the event log to transactions.txt
                int idAdd2;
                lineNumReadTransactions.skip(Long.MAX_VALUE);//skips to the last line in the text file
                idAdd2 = lineNumReadTransactions.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
                if(idAdd2 == 0){idAdd2++;}//if its the first item to be added to transactions.txt set its ID to 1

                String[] numZeros = {"", "0000", "000","00", "0"};//Creates an array which adds the corresponding number of 0s depending on the ID length
                String stringIdAdd = String.valueOf(idAdd2);//Converts idAdd to a string so the 0s can be added
                fullIdAdd2 = numZeros[stringIdAdd.length()] + idAdd2;//Adds the 0s to the id ad stores it in the varibale fullIdAdd
                LocalDate date = LocalDate.now();//gets the current date

                FileWriter writeTransactions = new FileWriter("transactions.txt", true);//creates a FileWriter to append to transactions.txt
                writeTransactions.write(System.getProperty( "line.separator" ) + fullIdAdd2 + "," + descriptionAdd + "," + "Null" + "," + stockAdd + "," + "AddItem" + "," + date);//add the log to transactions.txt
                writeTransactions.close();//closes transactions.txt
                System.out.println("item added");
            }
            catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
            catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
            catch(DateTimeException dateTimeExcetpion){System.out.println("Error retriving data");}
            catch(Exception unknownException){System.out.println("An unknown error has occured");}
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }

    /**
     * removes an item catagory from items.txt
     */
    public void removeItem() {
        Scanner input = new Scanner(System.in);//creates a scanner object to take user input with
        System.out.println("Please enter the description of the item that you wish to remove:");
        String descriptionRemove = input.nextLine();//takes the user input of the description of the item to remove

        try {//Searches items.txt line by line for the string that the user inputed
            FileReader read = new FileReader("items.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineContents = buffRead.readLine();//creates LineContents which stores the contents of each line
            boolean wasFound = false;//will store weather the string was found
            int lineNum = 0;//will store the number of the line which is currently being looked through / the string was found in
            while (lineContents != null)//while line is not empty
            {//goes through  each line of items.txt searching for the string that the user entered untill an empty line is hit or the string is found
                if (lineContents.contains("," + descriptionRemove + ",")) {
                    wasFound = true;
                    break;
                }
                lineContents = buffRead.readLine();//sets LineContents to the contents of the next line
                lineNum++;//increament LineNum to the current line being examined
            }
            if(wasFound == true){//if the string was found rewrite the file without the line that the user wants to remove
                FileReader read2 = new FileReader("items.txt");
                BufferedReader buffRead2 = new BufferedReader(read2);
                String lineContents2 = buffRead2.readLine();//sets the string LineContents2 to the contents of the line
                String newFileContents = "";//String used to hold the file contents - the line that is being removed
                int lineNum2 = 1;
                while (lineContents2 != null) {//while lines with text are still being read
                    lineContents2 = buffRead2.readLine();//sets the string LineContents2 to the contents of the line
                    if(lineNum != lineNum2 & lineContents2 != null)//if the current line being examined isn't the one to remove add it to NewFileContents
                    {
                        newFileContents +=  "\n" + lineContents2;//add the current line being examined to NewFileContents
                    }
                    else if(lineNum == lineNum2){newFileContents += "\nRemoved";}//adds Null to where the removed item was to prevent false IDs being generated when adding items
                    lineNum2++;
                }
                read.close();
                read2.close();
                PrintWriter updateFile = new PrintWriter("items.txt");
                updateFile.print(newFileContents);//Replaces the pre-existing contents of the file with the contents of the file minus the line selected by the user
                updateFile.close();
                System.out.println("item removed");

                String fullIdAdd = "";
                try (LineNumberReader lineNumReadTransactions = new LineNumberReader(new FileReader(new File("transactions.txt")))) {//adds the event log to transactions.txt
                    int idAdd;
                    lineNumReadTransactions.skip(Long.MAX_VALUE);//skips to the last line in the text file
                    idAdd = lineNumReadTransactions.getLineNumber();//find the number of the last line then increamenet it by one to find the next line number
                    if(idAdd == 0){idAdd++;}//if its the first item to be added to transactions.txt set its ID to 00001

                    String[] numZeros = {"", "0000", "000","00", "0"};//Creates an array which adds the corresponding number of 0s depending on the ID length
                    String stringIdAdd = String.valueOf(idAdd);//Converts idAdd to a string so the 0s can be added
                    fullIdAdd = numZeros[stringIdAdd.length()] + idAdd;//Adds the 0s to the id ad stores it in the varibale fullIdAdd
                    LocalDate date = LocalDate.now();//gets the current date

                    FileWriter writeTransactions = new FileWriter("transactions.txt", true);//creates a FileWriter to append to transactions.txt
                    writeTransactions.write(System.getProperty( "line.separator" ) + fullIdAdd + "," + descriptionRemove + "," + "Null" + "," + "Null" + "," + "RemoveItem" + "," + date);//add the log to transactions.txt
                    writeTransactions.close();//closes transactions.txt
                }
                catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
                catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
                catch(DateTimeException dateTimeExcetpion){System.out.println("Error retriving data");}
                catch(Exception unknownException){System.out.println("An unknown error has occured");}
            }
            else{System.out.println("item not found");}

        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(SecurityException SecException){System.out.println("An error has occured. Check that this program has the correct permisions to write to items.txt and transactions.txt");}//catches exception for if items.txt or transactions.txt can not be writen to
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
}

/**
 * contans the methods which are based around transactions.txt(dailyReport)
 */
class tranInfo{
    /**
     * Finds and displays transactions for the current day
     */
    public void dailyReport() {
        try {//Prints this days transactions to the user from transactions.txt
            FileReader read = new FileReader("transactions.txt");
            BufferedReader buffRead = new BufferedReader(read);
            System.out.println("ID, Description, QuntityChange, StockRemaining, TransactionType, Date");
            String lineContents = buffRead.readLine();//creates a variable called LineContents to store the contents of the current line untill the end of the test is hit
            lineContents = buffRead.readLine();//Skips the first line of transactions.txt as this will be empty
            while (lineContents != null) {//while we are still proccesing lines with text
                String[] values = lineContents.split("\\s*,\\s*");//splits the current line being examined into a string array
                LocalDate date = LocalDate.now();//gets the current date
                String strDate = String.valueOf(date);//converts the date into a string
                if(values[5].equals(strDate)){//if the record is for the current date
                    System.out.println(lineContents);//Prints the contents of the line
                }
                lineContents = buffRead.readLine();//set lineContents value to the contents of the next line to be examined
            }
        }
        catch(FileNotFoundException fileNotFound){System.out.println("Couldn't find items.txt/transactions.txt");}//Catches exception for if a file can not be found
        catch(DateTimeException dateTimeExcetpion){System.out.println("Error retriving data");}
        catch(Exception unknownException){System.out.println("An unknown error has occured");}

        String[] itemDataArgument = {""};//creates a string array to parse to main method
        itemData.main(itemDataArgument);//returns to main method
    }
}