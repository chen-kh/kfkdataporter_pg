package act.nsfc.kfkDataPorterPG.manager;

import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.kafkaPorter.KFKEVENTConsumer;
import act.nsfc.kfkDataPorterPG.kafkaPorter.KFKGPSConsumer;
import act.nsfc.kfkDataPorterPG.kafkaPorter.KFKOBDConsumer;

public class Manage {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Need argument: OBD or GPS or EVENT");
			System.exit(0);
		}

		if (args[0].equals("OBD")) {
			new Thread(new KFKOBDConsumer(), "kfk_obd").start();

			Daemon deamon = new Daemon(DataType.OBD);
			deamon.setAppID(CommonConfig.KfkGroupId_pg);
			new Thread(deamon, "deamon").start();
		} else if (args[0].equals("GPS")) {
			new Thread(new KFKGPSConsumer(), "kfk_gps").start();

			Daemon deamon = new Daemon(DataType.GPS);
			deamon.setAppID(CommonConfig.KfkGroupId_pg);
			new Thread(deamon, "deamon").start();
		} else if (args[0].equals("EVENT")) {
			new Thread(new KFKEVENTConsumer(true)).start();// 故障码事件

			KFKEVENTConsumer kfkevent = new KFKEVENTConsumer(false);// 普通事件
			Thread threadgps = new Thread(kfkevent, "kfk_event");
			threadgps.start();

			Daemon deamon = new Daemon(DataType.EVENT);
			deamon.setAppID(CommonConfig.KfkGroupId_pg);
			new Thread(deamon, "deamon").start();
			
		} else if (args[0].equals("MC")){
			
		}else {
			System.err.println("Unknown order");
			System.exit(0);
		}
	}
}
