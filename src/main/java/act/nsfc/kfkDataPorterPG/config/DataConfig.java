package act.nsfc.kfkDataPorterPG.config;

public class DataConfig {
	public static int gps_thread_num = 16;
	public static int obd_thread_num = 16;
	public static int event_thread_num = 2;
	public static final int max_queuelength = 80 * 1000;
	public static int blockSize_obd = 64;
	public static int blockSize_gps = 64;
	public static int blockSize_event = 64;
	public static int blockSize_error_event = 1;
	public static int max_queueLength_gps = 10 * 1000;
	public static int max_queueLength_obd = 10 * 1000;
	public static int max_queueLength_event = 10 * 1000;
}
