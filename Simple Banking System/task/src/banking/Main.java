package banking;


import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //String fileName = args[1];
        //System.out.println(args[1]);
        String fileName = "db.s3db";
        conn.Conn(fileName);
        conn.CreateDB();
        CreditCard c = new CreditCard();
        c.mainMenu();
    }
}