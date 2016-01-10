package main.server.threads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ServerAuthenticationThread extends Thread {
	//private Connection dbConnection=null;
	//private Statement statement=null;
	//private ResultSet resultSet=null;
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		//Class.forName("com.mysql.jdbc.Driver");
		Connection dbConnection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		Properties prop = new Properties();
        prop.put("characterEncoding","UTF8");
		prop.put("user", "root");
		prop.put("password", "root");
		
		dbConnection=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbusers",prop);
		
		statement=dbConnection.createStatement();
		String query="SELECT * FROM users WHERE username='user'";
		
		resultSet=statement.executeQuery(query);
		if(resultSet.next()) System.out.println(resultSet.getString("username"));
			
	}
	
	
	//public ServerAuthenticationThread(Socket socket){
		
	//}
}
