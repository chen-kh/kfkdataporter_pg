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

import act.nsfc.kfkDataPorterPG.bean.GPS;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.config.DataConfig;
import act.nsfc.kfkDataPorterPG.pgDao.PGClient;

public class GPSPlanter extends Thread {
	private Logger logger = LoggerRepository.instance.getGpsLogger();
	private int blockSize = DataConfig.blockSize_gps;
	private final LinkedBlockingQueue<GPS> gpsq;
	private int index;
	private Connection connection = null;
	PreparedStatement prepSt = null;

	public GPSPlanter(int index) {
		this.index = index;
		gpsq = new LinkedBlockingQueue<GPS>(DataConfig.max_queueLength_gps);
	}

	public void tryAddGPS(GPS gps) {
		try {
			gpsq.put(gps);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	public void run() {
		GPS temp = null;
		logger.info("PG thread " + index + " connecting");
		// 阻塞连接postgresql
		connection = PGClient.instance.blockUtilGetConnection(3, 30);
		while (connection == null) {
			logger.info("PG thread " + index + " get null connection, retry to connect postgresql!");
			connection = PGClient.instance.blockUtilGetConnection(3, 30);
		}
		logger.info("PG thread " + index + " connected!");
		try {
			connection.setAutoCommit(false);
			while (true) {
				try {
					if (needRest()) {
						Thread.sleep(CommonConfig.PGfhSleepInterval);
						continue;
					}
					if (gpsq.size() > blockSize) {
						// System.out.println("thread for gps_" + index + ":" +
						// "queue length : " + gpsq.size());
						prepSt = connection.prepareStatement("insert into gps_" + index
								+ " (devicesn,gpstime,longitude,latitude,speed,direction,processtime) values (?,?,?,?,?,?,?)");
						for (int i = 0; i < blockSize; i++) {
							temp = gpsq.poll();
							if (temp == null)
								break;
							prepSt.setString(1, temp.getDevicesn());
							prepSt.setLong(2, temp.getGpstime());
							prepSt.setDouble(3, temp.getLongitude());
							prepSt.setDouble(4, temp.getLatitude());
							prepSt.setDouble(5, temp.getSpeed());
							prepSt.setInt(6, temp.getDirection());
							prepSt.setLong(7, (new Date().getTime() / 1000L));
							prepSt.addBatch();
						}
						prepSt.executeBatch();
						connection.commit();
						// System.out.println("committed to gps_" + index +
						// "test");
						// Thread.sleep(100*1000);
					}
				} catch (BatchUpdateException e) {
					connection.rollback();
					logger.error(e);
				} catch (Exception e) {
					logger.error(e);
					// ???这是不是判断失去连接的好方法
					if (e.getMessage().contains("close")) {
						logger.info((new Date()).toString() + e.getMessage() + "  Re-Connecting");
						int i = 0;
						connection = PGClient.instance.blockUtilGetConnection(3, 30);
						while (connection == null) {
							i++;
							logger.info("gps retry times = " + i + " to connect postgresql !");
							PGClient.instance.blockUtilGetConnection(3, 30);
						}
						logger.info((new Date()).toString() + "   Re-Connected!");
					}
				}
			} // while(true)
		} catch (Exception e) {
			logger.error(e);
		}
	}// run()

	private boolean needRest() {
		long size = gpsq.size();
		if (size == 0) {
			return true;
		}
		return false;
	}
}
