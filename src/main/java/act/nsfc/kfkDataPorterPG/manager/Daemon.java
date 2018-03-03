package act.nsfc.kfkDataPorterPG.manager;

import org.apache.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.mcService.MCController;

public class Daemon implements Runnable{
	public long Interval = 1000 * 5;
	public int LogInterval = 60;
	public String appID;
	Logger log = Logger.getLogger(Daemon.class);

	final DataType type;

	public Daemon(DataType type) {
		this.type = type;
	}
	public void setAppID(String appID){
		this.appID = appID;
	}
	public void run() {
		while (true) {
			try {
				Thread.sleep(Interval);
				switch (type) {
				case GPS:
					MCController.instance.setDataCount(this.appID + "_gps", DataInfo.GPSCOUNT + "");
					MCController.instance.setDataCount(this.appID + "_queuelength", DataInfo.gpsQueueLength() + "");
					break;
				case OBD:
					MCController.instance.setDataCount(this.appID + "_obd", DataInfo.OBDCOUNT + "");
					MCController.instance.setDataCount(this.appID + "_queuelength", DataInfo.obdQueueLength() + "");
					break;
				case EVENT:
					MCController.instance.setDataCount(this.appID + "_event", DataInfo.EVENTCOUNT + "");
					break;
				default:
					break;
				}
			} catch (InterruptedException e) {
				log.error(e.getStackTrace().toString());
			}
		}
	}
}
