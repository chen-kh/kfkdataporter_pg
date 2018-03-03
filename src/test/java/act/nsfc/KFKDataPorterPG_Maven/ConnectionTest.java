package act.nsfc.KFKDataPorterPG_Maven;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionTest {
	public static void main(String[] args) {
		Connection connection = SqlTest.instance.connection;
		Connection connection2 = SqlTest.instance.connection;
		PreparedStatement st = null;
		PreparedStatement statement = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection.setAutoCommit(false);
			st = connection.prepareStatement("insert into students(name,id,emailAddress,sex) values (?,?,?,?)");
			int iFrom = 30;
			for (int i = iFrom; i < iFrom + 10; i++) {
				st.setString(1, "linfb"+i);
				st.setString(2, "ZY1624113");
				st.setString(3, "linfb@act.buaa.edu.cn");
				st.setString(4, "male");
				st.addBatch();
			}
			st.executeBatch();
			connection.commit();
			connection2.setAutoCommit(false);
			statement = connection2.prepareStatement("insert into students(name,id,emailAddress,sex) values (?,?,?,?)");
			statement.setString(1, "linfb");
			statement.setString(2, "ZY1624113");
			statement.setString(3, "linfb@act.buaa.edu.cn");
			statement.setString(4, "male");
			statement.executeUpdate();
			connection2.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
