package banking;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.abs;

public class CreditCard {

    private static final String BIN = "400000";
    private static final int BIN_LENGTH = 6;
    private static final int LOWER_PIN_NUMBER = 0;
    private static final int UPPER_PIN_NUMBER = 9;
    private static final int PIN_LENGTH = 4;
    private static final int NUMBER_LENGTH = 9;

    private static String cardNumber;
    private static String cardPin;
    private static String accountNumber;
    private static Map<String, String> accountList = new HashMap<>();
    private static int[] accountNumberIntList = new int[NUMBER_LENGTH];
    private static int[] sumListAray = new int[NUMBER_LENGTH + BIN_LENGTH];
    private static int[] binSumList = {4, 0, 0, 0, 0, 0};
    static String[] answerDB = new String[4];
    static Scanner scanner = new Scanner(System.in);

    public static void mainMenu() throws SQLException, ClassNotFoundException {
        while (true) {
            System.out.println("\n" +
                    "1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            switch (scanner.nextInt()) {
                case 1: createAnAccount();
                case 2: logIntoAccount();
                default: {
                    System.out.println("\n" +
                            "Bye!");
                    conn.CloseDB();
                    System.exit(1);
                }
            }
        }
    }

    private static void logIntoAccount() throws SQLException, ClassNotFoundException {
        System.out.println("\n" +
                "Enter your card number:");
        cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        cardPin = scanner.next();
        String sqlLogIn = "SELECT id, number, pin, balance FROM card WHERE number='"+cardNumber+"' AND pin='"+cardPin+"';";
        conn.ReadDB(sqlLogIn);
        answerDB = conn.ReadDB(sqlLogIn);
        if (answerDB[1] != null && answerDB[2] != null) {
            System.out.println("\n" +
                    "You have successfully logged in!");
            account.accountMenu(answerDB);
        } else {
            System.out.println("\n" +
                    "Wrong card number or PIN!");
        }
        mainMenu();
    }

    private static void createAnAccount() throws SQLException, ClassNotFoundException {
        cardPin = "";
        accountNumber = "";
        int inteervalPinNumber = UPPER_PIN_NUMBER - LOWER_PIN_NUMBER + 1;
        Random accountuser = new Random();
        for (int i = 1; i <= PIN_LENGTH; i++) {
            cardPin += String.valueOf(accountuser.nextInt(inteervalPinNumber) + LOWER_PIN_NUMBER);
        }
        for (int i = 1; i <= NUMBER_LENGTH; i++) {
            int count = accountuser.nextInt(inteervalPinNumber) + LOWER_PIN_NUMBER;
            accountNumberIntList[i-1] = count;
            accountNumber += String.valueOf(count);
        }
        cardNumber = BIN + accountNumber + checksumCard();
        accountList.put(cardNumber, cardPin);
        System.out.print("\n" +
                "Your card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                cardPin + "\n");
        String sqlWrite = "INSERT INTO 'card' ('number', 'pin') VALUES ('"+cardNumber+"', '"+cardPin+"');";
        conn.WriteDB(sqlWrite);
        mainMenu();
    }

    private static int checksumCard() {
        System.arraycopy(binSumList, 0, sumListAray, 0, binSumList.length);
        System.arraycopy(accountNumberIntList, 0, sumListAray, binSumList.length, accountNumberIntList.length);
        int sumList = 0;
        for (int i = sumListAray.length - 1; i >= 0; i --) {
            int element = sumListAray[i];
            int count = element * 2;
            if (i % 2 == 0) {
                element = (count > 9) ? count - 9 : count;
            }
            sumList += element;
        }
        return (sumList % 10 == 0) ? sumList / 10 : abs((sumList % 10) - 10);
    }
}
