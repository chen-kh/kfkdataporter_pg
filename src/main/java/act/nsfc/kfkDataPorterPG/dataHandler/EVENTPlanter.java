package act.nsfc.kfkDataPorterPG.dataHandler;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.bean.WarnEvent;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.config.DataConfig;
import act.nsfc.kfkDataPorterPG.pgDao.PGClient;

public class EVENTPlanter extends Thread {
	private int blockSize = DataConfig.blockSize_event;
	private final LinkedBlockingQueue<WarnEvent> eventsq;
	private Connection connection = null;
	private PreparedStatement prepSt = null;
	private int index;
	// private String topic;
	private Logger logger = LoggerRepository.instance.getEventLogger();

	public EVENTPlanter(int index) {
		this.index = index;
		this.eventsq = new LinkedBlockingQueue<WarnEvent>(DataConfig.max_queueLength_event);
	}

	@Override
	public void run() {
		WarnEvent temp = null;
		// 阻塞连接postgresql
		logger.info("PG thread " + index + " connecting");
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
					if (index == 0) {
						blockSize = DataConfig.blockSize_error_event;
					}
					if (eventsq.size() > blockSize) {
						for (int i = 0; i < blockSize; i++) {
							temp = eventsq.poll();
							if (temp == null)
								break;
							process(index, temp);
						}
						prepSt.executeBatch();
						connection.commit();
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

	public void tryAddEvent(WarnEvent event) {
		try {
			eventsq.put(event);
		} catch (InterruptedException e) {
			logger.error(e);
		}

	}

	private void process(int index, WarnEvent warnEvent) throws InterruptedException {
		try {
			if (index == 0) {// errorcode
				addErrorEvent(warnEvent);
			}
			// else if (warnEvent.getType() == 72) {// vin change event
			// addVinModifyRecords(warnEvent);
			// }
			else {// other event
				addCommonEvent(warnEvent);
			}
		} catch (Exception e) {
			// log.error(e.toString());
			logger.error(e);
		}
	}

	private void addErrorEvent(WarnEvent warnEvent) {
		try {
			// prepSt = connection.prepareStatement(
			// "insert into
			// errorevent(devicesn,errorcode,gpstime,longitude,latitude) values
			// (?,?,?,?,?)");
			// prepSt.setString(1, warnEvent.getDevicesn());
			// prepSt.setString(2, warnEvent.getErrorcode());
			// prepSt.setLong(3, warnEvent.getGpstime());
			// prepSt.setDouble(4, warnEvent.getLongitude());
			// prepSt.setDouble(5, warnEvent.getLatitude());

			// errorevent表里面的errorcode对应events表里面的detail
			prepSt = connection
					.prepareStatement("insert into events(devicesn,detail,gpstime,type_id) values (?,?,?,?)");
			prepSt.setString(1, warnEvent.getDevicesn());
			prepSt.setString(2, warnEvent.getErrorcode());
			prepSt.setLong(3, warnEvent.getGpstime());
			prepSt.setInt(4, (int) 100);
			// prepSt.executeUpdate();
			// connection.commit();
			prepSt.addBatch();
		} catch (BatchUpdateException exception) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				logger.error(e);
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private void addCommonEvent(WarnEvent warnEvent) {
		try {
			// prepSt = connection.prepareStatement(
			// "insert into
			// common_events(devicesn,type_id,gpstime,longitude,latitude,detail)
			// values(?,?,?,?,?,?)");
			// prepSt.setString(1, warnEvent.getDevicesn());
			// prepSt.setInt(2, warnEvent.getType());
			// prepSt.setLong(3, warnEvent.getGpstime());
			// prepSt.setDouble(4, warnEvent.getLongitude());
			// prepSt.setDouble(5, warnEvent.getLatitude());
			// prepSt.setDouble(6, warnEvent.getDetail());
			prepSt = connection.prepareStatement(
					"insert into events(devicesn,type_id,gpstime,longitude,latitude,detail,match_gpstime) values(?,?,?,?,?,?,?)");
			prepSt.setString(1, warnEvent.getDevicesn());
			prepSt.setInt(2, warnEvent.getType());
			prepSt.setLong(3, warnEvent.getGpstime());
			prepSt.setDouble(4, warnEvent.getLongitude());
			prepSt.setDouble(5, warnEvent.getLatitude());
			prepSt.setString(6, warnEvent.getDetail());
			prepSt.setLong(7, warnEvent.getGpstime());
			if (warnEvent.getType() != 72) {
				warnEvent.setDetail("");
			}
			prepSt.setString(6, warnEvent.getDetail());
			prepSt.addBatch();
			// System.out.println(
			// "add a common event ....... common common common common common
			// common common common common ");
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private void addVinModifyRecords(WarnEvent warnEvent) {
		String original_vin = "";
		String new_vin = "";
		String detail = warnEvent.getDetail();
		String[] vinDetail = detail.split(",");
		if (vinDetail.length == 2) {// added for new data format,in
									// order to
									// store the vin_change_event
									// despite the lack of detail
			original_vin = vinDetail[0];
			new_vin = vinDetail[1];
		}
		try {
			prepSt = connection.prepareStatement(
					"insert into vin_modify_records (devicesn,gpstime,original_vin,new_vin,longitude,latitude) values (?,?,?,?,?,?)");
			prepSt.setString(1, warnEvent.getDevicesn());
			prepSt.setLong(2, warnEvent.getGpstime());
			prepSt.setString(3, original_vin);
			prepSt.setString(4, new_vin);
			prepSt.setDouble(5, warnEvent.getLongitude());
			prepSt.setDouble(6, warnEvent.getLatitude());
			prepSt.addBatch();
			// System.out.println(
			// "add vin modify records ..... vin modify vin modify vin modify
			// vin modify vin modify vin modify vin modify ");
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private boolean needRest() {
		long size = eventsq.size();
		if (size == 0) {
			return true;
		}
		return false;
	}

}
