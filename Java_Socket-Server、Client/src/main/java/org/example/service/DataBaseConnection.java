package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//  https://tw.gitbook.net/jdbc/jdbc-insert-records.html
//  https://henryho85.pixnet.net/blog/post/223375477
public class DataBaseConnection {

    //连接本地数据库
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    // 连接服务器数据库
    static final String DB_URL = "jdbc:mariadb://localhost:3306/spring";

    //服务器数据库账号
    static final String USER = "root";
    static final String PASS = "sa";

    private static Connection conn = null;
    private static Statement stmt = null;

    //静态代码块只在类第一次被加载时执行一次
    static {

        try {

            //  載入目標資料庫專用之JDBC驅動程式
            Class.forName("org.mariadb.jdbc.Driver");

            //  與資料庫取得連線
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //  Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }//end try


    public static Connection getConnection() {
        return conn;
    }
}
