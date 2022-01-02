package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class account {

    static String[] answerDBAcc = new String[4];
    static Scanner scanner = new Scanner(System.in);

    public static void accountMenu(String[] answerDB) throws SQLException, ClassNotFoundException {
        answerDBAcc = answerDB;
        System.out.println("\n" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
        while (true) {
            switch (new Scanner(System.in).nextInt()) {
                case 1: printBalance();
                case 2: addIncome();
                case 3: addTransfer();
                case 4: deleteAccount();
                case 5: loginOut();
                default: exitProgram();
            }
        }
    }

    private static void exitProgram() throws SQLException, ClassNotFoundException {
        System.out.println("Bye!");
        conn.CloseDB();
        System.exit(1);
    }

    private static void loginOut() throws SQLException, ClassNotFoundException {
        System.out.println("\n" +
                "You have successfully logged out!");
        CreditCard.mainMenu();
    }

    private static void deleteAccount() throws SQLException, ClassNotFoundException {
        String deleteAccSQL = "DELETE FROM card WHERE number="+ answerDBAcc[1] +";";
        conn.WriteDB(deleteAccSQL);
        CreditCard.mainMenu();
    }

    private static void addTransfer() throws SQLException, ClassNotFoundException {
        System.out.println("\n" +
                "Transfer\n" +
                "Enter card number:");
            String numberTransferCard = scanner.next();
        if (numberTransferCard.isEmpty()) {
            accountMenu(answerDBAcc);
        }
        if (checkNumberCardAlgLuna(numberTransferCard) == 0) {
            System.out.println("\n" +
                    "Probably you made a mistake in the card number. Please try again!");
            accountMenu(answerDBAcc);
        } else {
            if (validationNumberCard(numberTransferCard) == 1) {
                System.out.println("\n" +
                        "Enter how much money you want to transfer:");
                int sumTransfer = scanner.nextInt();
                if (Integer.parseInt(answerDBAcc[3]) < sumTransfer) {
                    System.out.println("\n" +
                            "Not enough money!");
                    accountMenu(answerDBAcc);
                } else {
                    String transferInSQL = "UPDATE card SET balance= balance+" +sumTransfer
                            + " WHERE number="+ numberTransferCard +";";
                    conn.WriteDB(transferInSQL);
                    String transferOutSQL = "UPDATE card SET balance= balance-" +sumTransfer
                            + " WHERE number="+ answerDBAcc[1] +";";
                    conn.WriteDB(transferOutSQL);
                    String refreshSql = "SELECT id, number, pin, balance FROM card WHERE number='"+answerDBAcc[1]+"';";
                    answerDBAcc = conn.ReadDB(refreshSql);
                    System.out.println("Success!");
                    accountMenu(answerDBAcc);
                }
            }
        }
    }

    private static int validationNumberCard(String numberTransferCard) throws SQLException, ClassNotFoundException {
        String validCardsql = "SELECT id, number, pin, balance FROM card WHERE number='"+numberTransferCard+"';";
        String[] answerDB = conn.ReadDB(validCardsql);
        if (answerDB[1] == null) {
            System.out.println("\n" +
                    "Such a card does not exist.");
            accountMenu(answerDBAcc);
        } else {
            return 1;
        }
        return 0;
    }

    private static int checkNumberCardAlgLuna(String numberTransferCard) {
        String[] numberTransferCardArr = numberTransferCard.split("");
        int sum = 0;
        for (int i = 0; i < numberTransferCardArr.length; i++) {
            int element = Integer.parseInt(numberTransferCardArr[i]);
            int count = element * 2;
            if (i % 2 == 0) {
                element = (count > 9) ? count - 9 : count;
            }
            sum += element;
        }
        return (sum % 10 == 0) ? 1 : 0;
    }

    private static void addIncome() throws SQLException, ClassNotFoundException {
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        String incomingSQL = "UPDATE card SET balance= balance+" +income+ " WHERE number='"+ answerDBAcc[1] +"';";
        conn.WriteDB(incomingSQL);
        System.out.println("Income was added!");
        String refreshSql = "SELECT id, number, pin, balance FROM card WHERE number='"+answerDBAcc[1]+"';";
        answerDBAcc = conn.ReadDB(refreshSql);
        accountMenu(answerDBAcc);
    }

    public static void printBalance() throws SQLException, ClassNotFoundException {
        String balanceSql = "SELECT id, number, pin, balance FROM card WHERE number='"+answerDBAcc[1]+"';";
        conn.ReadDB(balanceSql);
        System.out.println(answerDBAcc[3]);
        accountMenu(answerDBAcc);
    }
}
