/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grabdatafromdb.megamillions;

import java.sql.SQLException;


/**
 *
 * @author Devon
 */
public class GrabDataFromDBMegaMillions {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        ConnectToDB ctd = new ConnectToDB("jdbc:db2://192.168.2.132:50000/TESTDB2","db2admin","db2admin","com.ibm.db2.jcc.DB2Driver");
        ctd.connectToDB();
        ctd.getDataFromDB();
        
    }
    
}
