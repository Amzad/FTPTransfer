import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database extends Thread{

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1/books";
	static final String USER = "Lays";
	static final String PASS = "Lays";
	static Connection conn = null;
	static Statement stmt = null;

	public Database() {
		/**try {
			System.out.println("Database: Connected");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
		}**/
	}

	public void openConnection() {
		try {
			System.out.println("Database: Connected");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
		}
	}

	public void closeConnection() {
		try {
			conn.close();
			System.out.println("Database: Disconnected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateQuery(String keyword, int count, String time) {
		try {	
			openConnection();
			stmt = conn.createStatement();
			String sql = "UPDATE indexqueue SET status = 'Done' WHERE keyword = '" + keyword + "'";
			stmt.executeUpdate(sql);
			sql = "UPDATE indexqueue SET count = '" + count + "' WHERE keyword = '" + keyword + "'";
			stmt.executeUpdate(sql);
			sql = "UPDATE indexqueue SET time = '" + time + "' WHERE keyword = '" + keyword + "'";
			stmt.executeUpdate(sql);
			System.out.println(keyword + " caching completed");
			closeConnection();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}

}
