package act.nsfc.kfkDataPorterPG.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonConfig {
	private static Logger logger = LogManager.getLogger(CommonConfig.class);
	// buaa 以下各变量的值从配置文件中读取
	public static String DB;// = "192.168.6.253";
	public static int DBPort;// = 5432;
	public static String DBName;// = "nsfc";
	public static String DBUser;// = "postgres";
	public static String DBPsd;// = "123456";

	public static String MC1Add;// = "192.168.6.120:11211";
	public static String MC2Add;// = "192.168.6.115:11211";
	public static String ZKAdd;// =
								// "192.168.6.128:2181,192.168.6.129:2181,192.168.6.130:2181";

	public static String KfkGroupId_pg;// = "dataporter_pg";
	public static String KfkGroupId_mc;// = "dataporter_mc";
	public static String KfkObdTopic;// ="ThriftObdDs";
	public static String KfkGpsTopic;// ="ThriftObdGps";
	public static String KfkObdEventTopic;// ="ThriftObdEvent";
	public static String KfkErrorCodeEventTopic;// ="ThriftObdError";
	public static int PGfhSleepInterval = 10;

	public static String getRealPath() {
		String rootPath = CommonConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		rootPath = rootPath.substring(0, rootPath.lastIndexOf("/") + 1);
		System.out.println(rootPath);
		return rootPath;
	}

	// 从配置文件读取上述未初始化的参数
	static {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(getRealPath() + "//conf.prop"));
			props.load(in);
			CommonConfig.DB = props.getProperty("DBHost");
			CommonConfig.DBPort = Integer.parseInt(props.getProperty("DBPort"));
			CommonConfig.DBName = props.getProperty("DBname");
			CommonConfig.DBUser = props.getProperty("DBUser");
			CommonConfig.DBPsd = props.getProperty("DBPsd");

			CommonConfig.MC1Add = props.getProperty("MC1Add");
			CommonConfig.MC2Add = props.getProperty("MC2Add");
			CommonConfig.ZKAdd = props.getProperty("ZKAdd");

			CommonConfig.KfkGroupId_pg = props.getProperty("KfkGroupId_pg");
			CommonConfig.KfkGroupId_mc = props.getProperty("KfkGroupId_mc");

			CommonConfig.KfkObdTopic = props.getProperty("KfkObdTopic");
			CommonConfig.KfkGpsTopic = props.getProperty("KfkGpsTopic");
			CommonConfig.KfkObdEventTopic = props.getProperty("KfkObdEventTopic");
			CommonConfig.KfkErrorCodeEventTopic = props.getProperty("KfkErrorCodeEventTopic");

			in.close();
			logger.info(Thread.currentThread().getName() + " load CommonConfig file: " + props.toString());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
