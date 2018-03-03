package act.nsfc.kfkDataPorterPG.dataHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.bean.GPS;
import act.nsfc.kfkDataPorterPG.bean.OBD;
import act.nsfc.kfkDataPorterPG.bean.WarnEvent;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.config.DataConfig;

public class DataHelper {
	private List<OBDPlanter> obdThreadList = new ArrayList<OBDPlanter>(DataConfig.obd_thread_num);
	private List<GPSPlanter> gpsThreadList = new ArrayList<GPSPlanter>(DataConfig.gps_thread_num);
	private List<EVENTPlanter> eventThreadList = new ArrayList<EVENTPlanter>(DataConfig.event_thread_num);
	// public static DataHelper instance = new DataHelper(DataType.ALL);
	private Logger logger = LogManager.getLogger(DataHelper.class);

	public DataHelper(DataType dataType) {
		try {
			OBDPlanter obdPlanter;
			GPSPlanter gpsPlanter;
			EVENTPlanter eventPlanter;
			switch (dataType) {
			case OBD:
				for (int i = 0; i < DataConfig.obd_thread_num; i++) {
					obdPlanter = new OBDPlanter(i);
					obdPlanter.setName("obdPlanter_thread_" + i);
					logger.info(obdPlanter.getName() + " starting ...");
					obdPlanter.start();
					obdThreadList.add(obdPlanter);
				}
				break;
			case GPS:
				for (int i = 0; i < DataConfig.gps_thread_num; i++) {
					gpsPlanter = new GPSPlanter(i);
					gpsPlanter.setName("gpsPlanter_thread_" + i);
					logger.info(gpsPlanter.getName() + " starting ...");
					gpsPlanter.start();
					gpsThreadList.add(gpsPlanter);
				}
				break;
			case EVENT:
				for (int i = 0; i < DataConfig.event_thread_num; i++) {
					eventPlanter = new EVENTPlanter(i);
					eventPlanter.setName("eventPlanter_thread_" + i);
					logger.info(eventPlanter.getName() + " starting ...");
					eventPlanter.start();
					eventThreadList.add(eventPlanter);
				}
				break;
			case ALL:
				startAllThread();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void addGpsData(String devicesn, GPS gps) {// GPS鏍规嵁devicesn鍒嗕负16寮犺〃銆傛瘡涓嚎绋嬪啓鎰忓紶琛ㄣ�傛牴鎹甦evicesn鍙戦�佺粰涓嶅悓鐨勭嚎绋嬪幓鍐�
		int index = Math.abs(devicesn.hashCode()) % 16;
		gpsThreadList.get(index).tryAddGPS(gps);
	}

	public void addObdData(String devicesn, OBD obd) {
		int index = Math.abs(devicesn.hashCode()) % 16;
		obdThreadList.get(index).tryAddOBD(obd);
	}

	public void addEventData(String topic, WarnEvent event) {
		int index;
		if (topic.equals(CommonConfig.KfkErrorCodeEventTopic)) {
			index = 0;
		} else {
			index = 1;
		}
		eventThreadList.get(index).tryAddEvent(event);

	}

	private void startAllThread() {
		OBDPlanter obdPlanter;
		GPSPlanter gpsPlanter;
		EVENTPlanter eventPlanter;
		for (int i = 0; i < DataConfig.obd_thread_num; i++) {
			obdPlanter = new OBDPlanter(i);
			obdPlanter.setName("obdPlanter_thread_" + i);
			logger.info(obdPlanter.getName() + " starting ...");
			obdPlanter.start();
			obdThreadList.add(obdPlanter);
		}
		for (int i = 0; i < DataConfig.gps_thread_num; i++) {
			gpsPlanter = new GPSPlanter(i);
			gpsPlanter.setName("gpsPlanter_thread_" + i);
			logger.info(gpsPlanter.getName() + " starting ...");
			gpsPlanter.start();
			gpsThreadList.add(gpsPlanter);
		}
		for (int i = 0; i < DataConfig.event_thread_num; i++) {
			eventPlanter = new EVENTPlanter(i);
			eventPlanter.setName("eventPlanter_thread_" + i);
			logger.info(eventPlanter.getName() + " starting ...");
			eventPlanter.start();
			eventThreadList.add(eventPlanter);
		}
	}
}
