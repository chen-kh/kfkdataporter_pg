package act.nsfc.KFKDataPorterPG_Maven;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.jute.Index;
import org.apache.zookeeper.data.Stat;

import scala.remote;

public class DeleteTables extends Thread{
	public static Connection connection = null;
	public static String DBHost = "192.168.0.7";//
	public static String DBPort = "5432";
	public static String DBName = "nsfc";
	public static String DBUser = "postgres";
	public static String DBPsd = "123456";
//	public static DeleteTables instance = new DeleteTables();
	public int index;
	public DeleteTables(int index) {
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://" + DBHost + ":" + DBPort + "/" + DBName + "";
			this.connection = DriverManager.getConnection(url, DBUser, DBPsd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		this.index = index;
	}

	public static void main(String[] args) {
		for(int i = 0 ; i <= 15 ; i++){
			DeleteTables deleteTables = new DeleteTables(i);
			deleteTables.start();
		}
	}
	public void run(){
		String table = "gps_" + this.index;
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String sql = "delete from " + table + " where devicesn like '9%';";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread() + "\t" + table + " deleted!");
	}
}
