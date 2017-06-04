/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author tim
 */
public class RetrieveInfo {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/kmeans";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "r00t";
   
   public double[][] data(){
   String filename ="test.csv";
    double[][] dbTable = null;
        try {
            FileWriter fw = new FileWriter(filename);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
           
            String query = "select * from subject";
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            int rowNumb = rs.getRow();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnS = rsmd.getColumnCount();
            rs.beforeFirst();
            dbTable= new double[rowNumb][columnS];
            int i =0;
             while (rs.next() && i <rowNumb) {
                /*fw.append(rs.getString(1));
                fw.append(',');
                fw.append(rs.getString(2));
                fw.append('\n');*/
                for(int j=0;j<columnS;j++)
        {
            dbTable[i][j] = rs.getDouble(j+1);
        
                
               
        }
        i++;
        
                
               }
            //fw.flush();
            //fw.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbTable;
}//end main
   
   
}//end FirstExample  

