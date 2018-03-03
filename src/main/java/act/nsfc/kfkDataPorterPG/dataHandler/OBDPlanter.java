package act.nsfc.kfkDataPorterPG.dataHandler;

import java.sql.BatchUpdateException;

/**
 * 
 * Real-time data fast insert helper
 * @author zhangmm
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.bean.OBD;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.config.DataConfig;
import act.nsfc.kfkDataPorterPG.pgDao.PGClient;

public class OBDPlanter extends Thread {

	private int blockSize = DataConfig.blockSize_obd;
	private final LinkedBlockingQueue<OBD> obdq;
	private Connection connection = null;
	private PreparedStatement prepSt = null;
	private int index;
	private Logger logger = LoggerRepository.instance.getObdLogger();
	public OBDPlanter(int index) {
		this.index = index;
		obdq = new LinkedBlockingQueue<OBD>(DataConfig.max_queueLength_obd);
	}

	public void tryAddOBD(OBD obd) {
		try {
			obdq.put(obd);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	// private boolean newConnection() {
	// // 鍒涘缓涓�涓暟鎹簱杩炴帴
	// try {
	// this.conTrail = DriverManager.getConnection(url, CommonConfig.DBUser,
	// CommonConfig.DBPsd);
	// this.stTrail = conTrail.createStatement();
	// System.out.println("new PG Connection");
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }

	@Override
	public void run() {
		OBD temp = null;
		logger.info("PG thread " + index + " connecting");
		connection = PGClient.instance.blockUtilGetConnection(3, 30);
		while (connection == null) {
			logger.info("PG thread " + index + " get null connection, retry to connect postgresql!");
			connection = PGClient.instance.blockUtilGetConnection(3, 30);
		}
		logger.info("PG thread " + index + " connected");
		try {
			connection.setAutoCommit(false);
			while (true) {
				try {
					if (needRest()) {
						Thread.sleep(CommonConfig.PGfhSleepInterval);
						continue;
					}
					if (obdq.size() > blockSize) {
						prepSt = connection.prepareStatement("insert into obd_" + index
								+ " (devicesn,gpstime,total_mileage,total_fuel,mileage,speed,engine_speed,processtime) values (?,?,?,?,?,?,?,?)");
						for (int i = 0; i < blockSize; i++) {
							temp = obdq.poll();
							if (temp == null)
								break;
							prepSt.setString(1, temp.getDevicesn());
							prepSt.setLong(2, temp.getGpstime());
							prepSt.setDouble(3, temp.getTotalMileage());
							prepSt.setDouble(4, temp.getTotalFuel());
							prepSt.setDouble(5, temp.getMileage());
							prepSt.setDouble(6, temp.getSpeed());
							prepSt.setDouble(7, temp.getEngineSpeed());
							prepSt.setLong(8, (new Date().getTime() / 1000L));
							prepSt.addBatch();
						}
						prepSt.executeBatch();
						connection.commit();
					}
				} catch (BatchUpdateException exception) {
					logger.error(exception);
					connection.rollback();
				} catch (Exception ee) {
					logger.error(ee);
					if (ee.getMessage().contains("close")) {
						logger.info((new Date()).toString() + ee.getMessage() + "  Re-Connecting");
						connection = PGClient.instance.blockUtilGetConnection(3, 30);
						int i = 0;
						while (connection == null) {
							i++;
							logger.info("obd retry times = " + i + " to connect postgresql !");
							PGClient.instance.blockUtilGetConnection(3, 30);
						}
						logger.info((new Date()).toString() + "   Re-Connected!");
					}
				}
			} 
		} catch (Exception eee) {
			logger.error(eee);
		}
	}

	private boolean needRest() {
		int size = obdq.size();
		DataInfo.obdQueueLength[index] = size;
		if (size == 0) {
			return true;
		}
		return false;
	}

}
