package net.javaguides.fileupload.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileUploadDao {
		
//	Class.forName("com.mysql.jdbc.Driver");
//	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/updown_db","root","");
//	stmt = conn.createStatement();
	
	  private static final String url = "jdbc:mysql://localhost:3306/updown_db?useSSL=false";
	  private static final String user = "root";
	  private static final String password = "";

	  private static final String sql = "INSERT INTO users (first_name, last_name, value) values (?, ?, ?)";
      
    public int uploadFile(String firstName, String lastName, String value) {
        int row = 0;
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        try (Connection connection = DriverManager
            .getConnection(url, user, password);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection
            .prepareStatement(sql)) {
        	
        	preparedStatement.setString(1, firstName);
        	preparedStatement.setString(2, lastName);
        	preparedStatement.setString(3, value);
        	
            // sends the statement to the database server
            row = preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
        return row;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
