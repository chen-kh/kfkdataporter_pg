package act.nsfc.datasimu;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import act.nsfc.kfkDataPorterPG.pgDao.PGClient;

public class Cars {
	private static Connection connection = null;
	private static Statement stat = null;

	public static void main(String[] args) {
		connection = PGClient.instance.blockUtilGetConnection(3, 30);
		while (connection == null) {
			connection = PGClient.instance.blockUtilGetConnection(3, 30);
		}
		File file = new File("E://车联网//cars_total.txt");
		try {
			stat = connection.createStatement();
			FileWriter fileWriter = new FileWriter(file);
			String sql = "select devicesn from cars;";
			ResultSet set = stat.executeQuery(sql);
			StringBuffer sBuffer = new StringBuffer();
			while (set.next()) {
				sBuffer.append(set.getString("devicesn") + "|");
			}
			fileWriter.write(sBuffer.substring(0, sBuffer.lastIndexOf("|")) + "\n");
			String[] cars = sBuffer.toString().split("\\|");
			for (String car : cars) {
				if (car != null && car.length() != 0) {
					fileWriter.write(car + "\n");
				}
			}
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
