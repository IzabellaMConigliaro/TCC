package ConexaoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDHandler {
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "tcc";
    private static final String AUTHORIZATION = "root";
    private static final String URL_PATH = "jdbc:mysql://";

    private static Connection connection = null;

	public BDHandler(){
		
	}
	
	public static Connection getMySQLConnection(){
		try {
			Class.forName(DRIVER_NAME);
			String url = URL_PATH + SERVER_NAME + "/" + DATABASE_NAME;
			connection = DriverManager.getConnection(url, AUTHORIZATION, AUTHORIZATION);
			
			if (connection != null){
				//TODO
			}
			
			return connection;
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Erro ao conectar: "+ e.getMessage());
			return null;
		}
	}
	
	public static boolean closeMySQLConnection() {
		try {
			BDHandler.getMySQLConnection().close();
			return true;
		} catch(SQLException e){
			return false;
		}
	}
	
	public static Connection restartMySQLConnection() {
		closeMySQLConnection();
		return BDHandler.getMySQLConnection();
	}
	
}
