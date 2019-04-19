import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database extends Thread{

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://192.168.1.11/anime";
	static final String USER = "Lays";
	static final String PASS = "Lays";
	static Connection conn = null;
	static Statement stmt = null;
	static PreparedStatement pstmt = null;
	static ArrayList<String> aList;

	public Database() {
			getAnimeList();
	}

	public static void openConnection() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//System.out.println("Database: Connected");
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
		}
	}

	public static void closeConnection() {
		try {
			conn.close();
			//System.out.println("Database: Disconnected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getAnimeDirectory(String title) {
		String result = null;
		try {
			openConnection ();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM anime_list WHERE title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getString("directory");
			}
			return result;
			
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
		
	}
	
	public static int getAnimeID(AnimeFile input) {
		
		try {
			openConnection ();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM anime_list WHERE title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, input.animeTitle);
			ResultSet rs = pstmt.executeQuery();
			int result = 0;
			while(rs.next()) {
				result = rs.getInt("id");
			}
			return result;
			
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return 0;
		
	}
	
	public void getAnimeList() {
		aList = new ArrayList<String>();
		try {
			openConnection ();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM anime_list";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				aList.add(rs.getString("title"));
			}
			

		} catch (SQLException e) {		
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
	}
		
	public static void insertNewEpisode(AnimeFile input) {
		
		try {
			int anime_id = getAnimeID(input);
			openConnection ();
			
			stmt = conn.createStatement();
			String sql = "INSERT INTO episode_index (anime_id, anime_title, anime_episode, file_directory) " 
							+ "VALUES(?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, anime_id);
			pstmt.setString(2, input.animeTitle);
			pstmt.setInt(3, Integer.parseInt(input.animeEpisode));
			pstmt.setString(4, input.directory + "\\" + input.animePath);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
	}

}
