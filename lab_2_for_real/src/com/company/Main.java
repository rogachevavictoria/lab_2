package com.company;

import java.sql.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Connection con = getConnection();
        String select = "SELECT * FROM books;";
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        try {
            Statement s = con.createStatement();
            ResultSet rows = s.executeQuery(select);
            output(rows);
            while(!exit) {
                System.out.println("Press: add - 1, edit - 2, delete - 3, exit - 4:");
                int n = scan.nextInt();
                switch(n) {
                    case 1: s.addBatch(add()); break;
                    case 2: s.addBatch(edit()); break;
                    case 3: s.addBatch(delete()); break;
                    case 4: exit = true; System.exit(0);;
                }
                s.executeBatch();
                rows = s.executeQuery(select);
                output(rows);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //---------------------------------------------------------------------------------------------
    private static Connection getConnection()
    {	//get a connection to database
        Connection con = null;
        String url = "jdbc:mysql://localhost/books?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; //"jdbc:mysql://localhost:3306/books";
        String user = "root";
        String pw = "npass";
        try {
            con = DriverManager.getConnection(url, user, pw);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    //---------------------------------------------------------------------------------------------
    static void output(ResultSet rows) {
        try {
            System.out.format("%-3s|%-20s|%-20s|%-10s", "ID", "book_name", "author", "price");
            System.out.println("\n+--+--------------------+--------------------+----------");
            while (rows.next()) {
                System.out.format("%-3d|%-20s|%-20s|%-10s", rows.getInt("id"),
                        rows.getString("bname"),
                        rows.getString("author"),
                         rows.getDouble("price"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //---------------------------------------------------------------------------------------------
    static String add() {
        Scanner scan = new Scanner(System.in);
        LinkedHashMap<String, String> rows = new LinkedHashMap<String, String>(4);
        String column;
        boolean check = false;
        rows.put("bname", " ");
        rows.put("author", " ");
        rows.put("price", "0");
        Set set = rows.entrySet();
        while(!check) {
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                column = (String)me.getKey();
                System.out.print("New "+column + ": ");
                rows.put(column, scan.nextLine());
            }
            check = check(rows);
        }
        String values = "\""+rows.get("bname")+"\""+ ", " +"\""+ rows.get("author")+"\""+ ", " +"\""+ rows.get("price")+"\"";
        String add = "INSERT INTO books (bname, author, price)"
                + "VALUES" + "(" + values + ")" +";";
        return add;
    }
    //---------------------------------------------------------------------------------------------
    static String edit() {
        LinkedHashMap<String, String> rows = new LinkedHashMap<String, String>(4);
        Scanner scan = new Scanner(System.in);
        String update = "";
        int id = 0;
        System.out.println("Please enter the number of the row, column you want to change and the new value: ");
        String column = "", value;
        boolean check = false;
        rows.put("bname", " ");
        rows.put("author", "0");
        rows.put("price", "0");
        Set set = rows.entrySet();
        while(!check){
            System.out.println("# of the row to edit: ");
            id = scan.nextInt();
            System.out.println("Column to edit: ");
            scan.nextLine();
            column = scan.nextLine();
            System.out.println("New value: ");
            value = scan.nextLine();
            rows.put(column, value);
            check = check(rows);
        }
        update = "UPDATE books SET "+ column +" = "
                +"\""+ rows.get(column) +"\""+" WHERE id = "
                + id + ";";
        return update;
    }
    //---------------------------------------------------------------------------------------------
    static String delete() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of the row you want to delete: ");

        int id = scan.nextInt();

        String delete = "DELETE FROM books WHERE id  = \"" +id+"\";";

        return delete;
    }
    //---------------------------------------------------------------------------------------------
    static boolean check(LinkedHashMap<String,String> lhm) {
        boolean check = true;
        for(String key : lhm.keySet()) {
            switch(key) {
                case "price":
                    try {
                        double d = Double.parseDouble(lhm.get(key));
                    } catch(NumberFormatException e) {
                        System.out.println("Please enter a valid price value.");
                        check = false;
                    }
                    break;
            }
        }
        return check;
    }
}