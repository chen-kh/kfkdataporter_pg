package act.nsfc.kfkDataPorterPG.pgDao;

/**
 * 
 * Real-time data fast insert helper
 * @author zhangmm
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.WarnEvent;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;

public class PGClient extends Thread {
	private Logger logger = LogManager.getLogger(PGClient.class);
	public Connection con;
	public PreparedStatement preparedStatement = null;
	private String url = "jdbc:postgresql://" + CommonConfig.DB + ":" + CommonConfig.DBPort + "/" + CommonConfig.DBName;
	public static PGClient instance = new PGClient();

	private PGClient() {
		newConnection();
	}

	public boolean newConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			this.con = DriverManager.getConnection(url, CommonConfig.DBUser, CommonConfig.DBPsd);
			return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, CommonConfig.DBUser, CommonConfig.DBPsd);
			return connection;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 阻塞直到得到一个数据库连接。 {@code sleepIntervalS} 表示重连的时间间隔秒数，{@code timeOutS}
	 * 表示超时秒数，即超过这么长时间的时候认为阻塞连接失败
	 * 
	 * @param connection
	 * @param sleepIntervalSq
	 * @param timeOutS
	 * @return connection
	 */
	public Connection blockUtilGetConnection(int sleepIntervalS, int timeOutS) {
		Connection connection = getConnection();
		int sleepTimes = 0;
		try {
			while (connection == null) {
				Thread.sleep(sleepIntervalS);
				sleepTimes += 1;
				connection = getConnection();
				if (sleepIntervalS * sleepTimes > timeOutS) {
					logger.info("connect failed in " + timeOutS + " seconds");
					return null;
				}
			}
			logger.info(Thread.currentThread().getName() + " connected");
			return connection;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	public void addEvent(WarnEvent warnEvent) {

	}
}
