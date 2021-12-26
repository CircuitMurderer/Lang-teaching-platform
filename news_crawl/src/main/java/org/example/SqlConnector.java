package org.example;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlConnector {
    static final String USERNAME = "root";
    static final String PASSWORD = "2333";

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL_ENC = "jdbc:mysql://localhost:3306/news?&characterEncoding=";

    final String TODAY = this.getToday();

    public SqlConnector() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    public void insertNews(String tableName, String title,
                           String time, String text, String encode) {
        String query = "INSERT INTO %s VALUES ('%s', '%s', '%s', '%s')";
        query = String.format(query, tableName, title, time, text, TODAY);

        String select = "SELECT * FROM %s WHERE title='%s'";
        select = String.format(select, tableName, title);

        Connection conn;
        Statement stmt;
        try {
            conn = DriverManager.getConnection(DB_URL_ENC + encode, USERNAME, PASSWORD);
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(select);
            if (rs.next()) {
                System.out.println("This item exists: " + title);
            } else {
                stmt.execute(query);
                System.out.println("Insertion complete: " + title);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertManyNews(String tableName, List<String> title,
                               List<String> time, List<String> text, String encode) {
        assert title.size() == time.size();
        assert title.size() == text.size();

        String insertBase = "INSERT INTO %s VALUES ('%s', '%s', '%s', '%s')";
        String selectBase = "SELECT * FROM %s WHERE title='%s'";

        Connection conn;
        Statement stmt;

        try {
            System.out.println("Inserting data...");
            conn = DriverManager.getConnection(DB_URL_ENC + encode, USERNAME, PASSWORD);
            stmt = conn.createStatement();

            for (int i = 0; i < title.size(); i++) {
                String titleNow = title.get(i);
                String timeNow = time.get(i);
                String textNow = text.get(i);

                String select = String.format(selectBase, tableName, titleNow);
                ResultSet rs = stmt.executeQuery(select);

                if (rs.next()) {
                    System.out.println("This item exists: " + titleNow);
                } else {
                    String insert = String.format(insertBase,
                            tableName, titleNow, timeNow,
                            textNow, TODAY);
                    stmt.execute(insert);
                    System.out.println("Insertion complete: " + titleNow);
                }
                rs.close();
            }

            stmt.close();
            conn.close();
            System.out.println("All done.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectNews(String tableName, String encode) {
        Connection conn;
        Statement stmt;

        try {
            System.out.println("Collecting...");
            conn = DriverManager.getConnection(DB_URL_ENC + encode, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT * FROM " + tableName;

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String title = rs.getString("title");
                String time = rs.getString("time");
                String text = rs.getString("text");
                String uTime = rs.getString("utime");

                System.out.println("title: " + title);
                System.out.println("time: " + time);
                System.out.println("text: " + text);
                System.out.println("update time: " + uTime);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
