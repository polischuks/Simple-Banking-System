package banking;

import java.sql.*;

public class conn {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Conn(String fileName) throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:"+ fileName);
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'card' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'number' text, 'pin' text, 'balance' INTEGER DEFAULT 0);");
    }

    public static void WriteDB(String sqlWrite) throws SQLException
    {
        statmt.execute(sqlWrite);
    }

    public static String[] ReadDB(String sqlLogIn) throws ClassNotFoundException, SQLException {
        String[] answerDB = new String[4];
        resSet = statmt.executeQuery(sqlLogIn);

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  number = resSet.getString("number");
            String  pin = resSet.getString("pin");
            int balance = resSet.getInt("balance");
            if (number != null && pin != null) {
                answerDB[0] = String.valueOf(id);
                answerDB[1] = number;
                answerDB[2] = pin;
                answerDB[3] = String.valueOf(balance);
                return answerDB;
            }
            System.out.println( "ID = " + id );
            System.out.println( "number = " + number );
            System.out.println( "pin = " + pin );
            System.out.println( "balance = " + balance );
            System.out.println();
        }
        return answerDB;
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
    }
}