/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grabdatafromdb.megamillions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Devon
 */
public class ConnectToDB {

    private String url;
    private String user;
    private String pw;
    private String jdbcClassName;
    private Connection con;

    public ConnectToDB(String url, String user, String pw, String jdbcClassName) {
        this.url = url;
        this.user = user;
        this.pw = pw;
        this.jdbcClassName = jdbcClassName;

    }

    public void connectToDB() {
        try {
            Class.forName(jdbcClassName);
            con = DriverManager.getConnection(url, user, pw);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ClassNotFoundException");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException");
        } finally {
            if (con != null) {
                System.out.println("Connected Successfully.");
            }
        }
    }

    public void getDataFromDB() throws SQLException {
        int rowcount = 0;
        int count = 1;

        if (con != null) {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("select * from DEVON.MEGAMILLIONS  order by DATE Desc");
            ResultSetMetaData rsm = rs.getMetaData();

            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            System.out.println("Row count: " + rowcount);

            for (int x = 1; x <= rsm.getColumnCount(); x++) {
                System.out.printf("%-10s\t", rsm.getColumnLabel(x).trim());
            }
            System.out.println();
            for (int x = 1; x <= rsm.getColumnCount(); x++) {
                System.out.printf("%-10s\t", "----------");
            }
            System.out.println();
            while (rs.next()) {
                count++;
                for (int x = 1; x <= rsm.getColumnCount(); x++) {
                    System.out.printf("%-10s\t", rs.getString(rsm.getColumnLabel(x)).trim());
                }
                System.out.println();
            }
            con.close();
            System.out.println("Connection Closed.");
        } else {
            con.close();
            System.out.println("No Connection! Connection Closed. ");
        }
    }
}
