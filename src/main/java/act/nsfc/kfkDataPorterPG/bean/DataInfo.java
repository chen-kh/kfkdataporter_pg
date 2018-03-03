package act.nsfc.kfkDataPorterPG.bean;

import act.nsfc.kfkDataPorterPG.config.DataConfig;

public class DataInfo {
	public static long GPSCOUNT = 0;
	public static long OBDCOUNT = 0;
	public static long EVENTCOUNT = 0;
	public static int gpsQueueLength[] = new int[DataConfig.gps_thread_num];
	public static int obdQueueLength[] = new int[DataConfig.obd_thread_num];
	public static int gpsQueueLength() {
		int length = 0;
		for (int i = 0; i < gpsQueueLength.length; i++) {
			length += gpsQueueLength[i];
		}
		return length;
	}

	public static int obdQueueLength() {
		int length = 0;
		for (int i = 0; i < obdQueueLength.length; i++) {
			length += obdQueueLength[i];
		}
		return length;
	}
}
