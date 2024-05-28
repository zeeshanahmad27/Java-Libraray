

import java.io.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

class BookScanner {
    public static HashMap<String, Object> inputScan() {
        Scanner myObj = new Scanner(System.in);
        HashMap<String, Object> bookDetails = new HashMap<>();
        // Store the creation time
        LocalDateTime creationTime = LocalDateTime.now();
        bookDetails.put("CreationTime", creationTime);

        //Loop for Title, Author and Publisher attributes to take input from the user: mandatory Inputs
        String[] bookAttributesMandatory = {"Title", "Author", "Publisher"};
        for (String a : bookAttributesMandatory) {
            do {
                System.out.print("Enter the " + a + " of the book: ");
                bookDetails.put(a, myObj.nextLine());
                if (bookDetails.get(a).toString().isEmpty()) {
                    System.out.println("Please add the value of mandatory attribute " + a);
                }
            } while (bookDetails.get(a).toString().isEmpty());
        }

        //Loop in which Entered year input is taken by user and check the valid input year from 1 to Current year
        do {
            System.out.print("Enter the PublishingYear* of the book: ");
            try {
                int PublishingYear = Integer.parseInt(myObj.nextLine());
                int year = Year.now().getValue();

                if (PublishingYear <= 1 || PublishingYear > year) {
                    throw new NumberFormatException();
                }
                bookDetails.put("PublishingYear", PublishingYear);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer for PublishingYear* (1-2024)");
            }
        } while (!bookDetails.containsKey("PublishingYear"));

        //Loop that make sure that the ISBN is correct and must be 10 or 13 digits
        do {
            System.out.print("Enter the ISBN of the book (must be a 10 or 13 digit number): ");
            String isbn = myObj.nextLine();
            if (isbn.matches("[0-9]{10}") || isbn.matches("[0-9]{13}")) {
                bookDetails.put("ISBN", isbn);
            } else {
                System.out.println("Please enter a valid ISBN (10 or 13 digit number)");
            }
        } while (!bookDetails.containsKey("ISBN"));

        //Loop for Subtitle and Description attributes to take input from the user: Non-mandatory inputs
        String[] bookAttributesNonMandatory = {"Subtitle", "Description"};
        for (String a : bookAttributesNonMandatory) {
            System.out.print("Enter the " + a + " of the book: ");
            bookDetails.put(a, myObj.nextLine());
        }

        // Store formatted creation time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = creationTime.format(formatter);
        bookDetails.put("Time of Book Creation", formattedDate);
        bookDetails.put("EditTime", "");

        return bookDetails;
    }

    //Method to write the data on Exported HTML file
    public static void writeToFile(HashMap<String, HashMap<String, Object>> library, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (HashMap<String, Object> book : library.values()) {
                String[] bookAttributes = {"Title", "Author", "Publisher", "PublishingYear", "Subtitle", "ISBN", "Description", "Time of Book Creation", "EditTime"};

                for (String a : bookAttributes) {
                    writer.write(a + ": " + book.get(a));

                }
            }
            System.out.println("Book details exported to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}

public class BookLibrary2 {

    public static HashMap<String, HashMap<String, Object>> library = new HashMap<>();

    public static void main(String[] args) {
        loadLibraray();
        Scanner scanMore = new Scanner(System.in);
        // Adding books
        String moreBooks;
        do {
            HashMap<String, Object> bookDetails = BookScanner.inputScan();
            library.put(bookDetails.get("Title").toString(), bookDetails);
            writeInFile();
            System.out.println("Would you like to add more books? (Yes/No)");
            moreBooks = scanMore.nextLine();
        } while (moreBooks.equalsIgnoreCase("yes"));

        serialization();

        // Choose Function
        System.out.println("Do you want to choose any function? (Yes/No)");
        String useOfFunction = scanMore.nextLine();
        if (useOfFunction.equalsIgnoreCase("yes")) {
            System.out.println("Please Choose");
            System.out.println("1: To Print book details");
            System.out.println("2: To edit Book attribute");
            System.out.println("3: To delete book");
            int programChoose = scanMore.nextInt();
            scanMore.nextLine();

            //Function 1
            if (programChoose == 1) {
                // Print book details
                int i = 0;
                for (HashMap<String, Object> book : library.values()) {
                    i++;
                    System.out.println("Book " + i);
                    String[] bookAttributes = {"Title", "Author", "Publisher", "PublishingYear", "Subtitle", "ISBN", "Description", "Time of Book Creation", "EditTime"};
                    for (String a : bookAttributes) {
                        System.out.println(a + ": " + book.get(a));
                    }
                    System.out.println();
                }

                //Function 2
            } else if (programChoose == 2) {
                // Edit books
                System.out.println("Following books are available to edit:");
                //Loop to Print Title of all Books in the Library
                bookTitles();
                System.out.println("Please enter the book from the list above you want to edit:");
                String bookEntered = scanMore.nextLine();

                if (library.containsKey(bookEntered)) {
                    HashMap<String, Object> bookToEdit = library.get(bookEntered);
                    String moreAttri;
                    do {
                        System.out.println("Enter the attribute you want to edit: Title, Author, Publisher, PublishingYear, Subtitle, ISBN, Description");
                        String attributeConfirm = scanMore.nextLine();

                        if (attributeConfirm.equalsIgnoreCase("Title") ||
                                attributeConfirm.equalsIgnoreCase("Author") ||
                                attributeConfirm.equalsIgnoreCase("Publisher") ||
                                attributeConfirm.equalsIgnoreCase("PublishingYear") ||
                                attributeConfirm.equalsIgnoreCase("Subtitle") ||
                                attributeConfirm.equalsIgnoreCase("ISBN") ||
                                attributeConfirm.equalsIgnoreCase("Description")) {
                            System.out.println("Enter the new value for that attribute:");
                            String newValue = scanMore.nextLine();
                            bookToEdit.put(attributeConfirm, newValue);

                            // Update the edit time
                            LocalDateTime editTime = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            bookToEdit.put("EditTime", editTime.format(formatter));

                            writeInFile();

                            serialization();
                        } else {
                            System.out.println("You entered an invalid attribute.");
                        }

                        System.out.println("Do you want to edit more attributes? (Yes/No)");
                        moreAttri = scanMore.nextLine();
                    } while (moreAttri.equalsIgnoreCase("yes"));

                    System.out.println("Updated book details:");
                    System.out.println(bookToEdit);
                } else {
                    System.out.println("The entered book does not exist.");

                }
            // Function 3
            } else if (programChoose == 3) {

                // Loop to Print Title of all Books in the Library
                bookTitles();
                // Deleting books
                System.out.println("Please enter the book from the list above you want to delete:");
                String bookConfirm = scanMore.nextLine();
                if (library.containsKey(bookConfirm)) {
                    System.out.println("Are you sure you want to delete the book: " + bookConfirm + " from the library? (Yes/No)");
                    String deleteConfirm = scanMore.nextLine();
                    if (deleteConfirm.equalsIgnoreCase("Yes")) {
                        library.remove(bookConfirm);
                        System.out.println("Book: " + bookConfirm + " has been deleted from the library. The list of remaining books:");
                        System.out.println(library);

                        writeInFile();
                        serialization();
                        scanMore.close();
                    }
                } else {
                    System.out.println("Book " + bookConfirm + " not found in the library.");
                }
            }
        } else {
            System.out.println("Program Ends");
        }
    }

    public static void loadLibraray() {
        try {
            FileInputStream fis = new FileInputStream("hashmap.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            library = (HashMap<String, HashMap<String, Object>>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing library found. Starting with an empty library.");
        }
    }
    // For seriialization and deserialization of the library
    public static void serialization() {
        try {
            FileOutputStream fos = new FileOutputStream("hashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(library);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
// To print the title of all avalible books in the library
    public static void bookTitles() {
        int i = 0;
        for (String title : library.keySet()) {
            i++;
            System.out.print(i+ ". ");
            System.out.println(title);
        }
    }
// To write book data in the HTML file
    public static void writeInFile() {
        BookScanner.writeToFile(library, "book_details.html");
    }
}
