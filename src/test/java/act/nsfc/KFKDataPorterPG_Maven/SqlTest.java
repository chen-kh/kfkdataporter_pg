package act.nsfc.KFKDataPorterPG_Maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SqlTest {
	public static Connection connection = null;
	public static String DBHost = "192.168.3.152";//
	public static String DBPort = "5432";
	public static String DBName = "exampledb";
	public static String DBUser = "chenkh";
	public static String DBPsd = "123456";
	public static SqlTest instance = new SqlTest();

	private SqlTest() {
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://" + DBHost + ":" + DBPort + "/" + DBName + "";
			this.connection = DriverManager.getConnection(url, DBUser, DBPsd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// getConnection();
		PreparedStatement st = null;
		// while (true) {
		try {
			Class.forName("org.postgresql.Driver");
			connection.setAutoCommit(false);
//			st = connection.prepareStatement("insert into students(name,id,emailAddress,sex) values (?,?,?,?)");
			
//			int id = 1624113;
//			for (int i = 0; i < 5; i++) {
//				st.setString(1, "linfb");
//				st.setString(2, "ZY" + (id + i) + "");
//				st.setString(3, "linfb@act.buaa.edu.cn");
//				st.setString(4, "male");
//				st.executeUpdate();
//				connection.commit();
//				// st.addBatch();
//				System.out.println("insert into " + (id + i));
//				// st.executeBatch();
//			}
//			System.out.println(connection.getTransactionIsolation());
			// st.executeBatch();
			st = connection.prepareStatement("delete from ?");
			st.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			 try {
			 connection.rollback();
			 System.out.println("data has rollback!");
			 } catch (SQLException e1) {
			 e1.printStackTrace();
			 }
			e.printStackTrace();
		}
		Statement statement;
		try {
			statement = connection.createStatement();
			String selectSql = "select * from students;";
			System.out.println(selectSql);
			ResultSet set = statement.executeQuery(selectSql);
			while(set.next()){
				System.out.println(set.getString(2));
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		// }
	}
}
