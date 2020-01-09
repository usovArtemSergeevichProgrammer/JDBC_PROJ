package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.JDBCUtils;

public class Test {

	static String DB_USER = "root";
	static String DB_USER_PSW = "";
	static String DB_URL = "jdbc:mysql://localhost:3306/retailer";
	static String SELECT_ALL_SQL = "SELECT * FROM users;";
	static String SELECT_ALL_COUNT_SQL = "SELECT COUNT(*) FROM users;";
	static String SELECT_USER_WHERE_UID = "SELECT * FROM users WHERE uid = ?;";

	static List<User> getUsers() {
		List<User> list = new ArrayList<>();
		Connection conn = JDBCUtils.createConnection();// 1
		Statement stmt = null;// 2
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL);// 3
			while (rs.next()) {
				int uid = rs.getInt(1); // UID
				String email = rs.getString(2); // name rs.getString(2); //
												// email
				String name = rs.getString(3); // name
				String lastName = rs.getString(4); // last name
				list.add(new User(uid, email, name, lastName));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JDBCUtils.release(conn, stmt);

		return list;
	}

	public static void insertUser(User user) {
		Connection conn = JDBCUtils.createConnection();// 1
		Statement stmt = null;// 2
		String sql = "INSERT INTO `test`.`users` (`EMAIL`, `NAME`, `LAST_NAME`) VALUES ('"+user.getEmail()+"', '"+user.getName()+"', '"+user.getLastName()+"')";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("It's OK");
		JDBCUtils.release(conn, stmt);
		
	}
	
	public static User getUser(int uid) {
		Connection conn = JDBCUtils.createConnection();
		Statement stmt = null;
		User user = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_USER_WHERE_UID.replace("?", uid+ ""));

			if (rs.next()) {
				int id = rs.getInt(1); // UID
				String email = rs.getString(2); // name rs.getString(2); //
												// email
				String name = rs.getString(3); // name
				String lastName = rs.getString(4); // last name
				user = new User(id, email, name, lastName);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JDBCUtils.release(conn, stmt);
		return user;

	}
	
	

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JDBCUtils.createConnection();

			if (conn == null) {
				System.out.println(String.format("Can't connect to %s", DB_URL));

			} else {
				System.out.println(String.format("Connected to %s", DB_URL));
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_COUNT_SQL);
				if (rs.next()) {
					int counter = rs.getInt(1);
					System.out.println(String.format("Records counter : %d", counter));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, stmt);
		}

		/*
		 * /
		 * 
		 */
		for (User user : getUsers()) {
			System.out.println(user);
		}

		System.out.println("---->");
		System.out.println(getUser(1));
		
		System.out.println("--->-->");
		User user1 = new User("Dima@inbox.ru","Dimon","D");
		insertUser(user1);
		//INSERT INTO `test`.`users` (`UID`, `EMAIL`, `NAME`, `LAST_NAME`) VALUES (3, 'sdfsdf', 'sdfs', 'sdf')
	}

}
