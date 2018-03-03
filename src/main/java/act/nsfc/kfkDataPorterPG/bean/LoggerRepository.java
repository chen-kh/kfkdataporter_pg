package act.nsfc.kfkDataPorterPG.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerRepository {
	private static Logger mclogger = LogManager.getLogger("mcLogger");
	private static Logger gpslogger = LogManager.getLogger("gpsLogger");
	private static Logger obdlogger = LogManager.getLogger("obdLogger");
	private static Logger eventlogger = LogManager.getLogger("eventLogger");
	public static LoggerRepository instance = new LoggerRepository();

	private LoggerRepository() {

	}

	public Logger getObdLogger() {
		return obdlogger;
	}

	public Logger getGpsLogger() {
		return gpslogger;
	}

	public Logger getEventLogger() {
		return eventlogger;
	}

	public Logger getMcLogger() {
		return mclogger;
	}
}
