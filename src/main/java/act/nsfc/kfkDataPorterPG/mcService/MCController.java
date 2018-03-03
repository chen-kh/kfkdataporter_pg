package act.nsfc.kfkDataPorterPG.mcService;

import java.util.Date;

import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.sf.json.JSONObject;

public class MCController {
	public static MCController instance = new MCController();
	private Logger logger = LoggerRepository.instance.getMcLogger();
	private MemcachedClientBuilder mcbuilder;

	private MemcachedClient mc1;
	private MemcachedClient mc2;

	short index = 0;

	public int MCThreadCount = 4;
	public int MCPoolSize = 1;
	public boolean MCFailureMode = true;

	// 连接两个memcache
	private MCController() {
		try {
			mcbuilder = new XMemcachedClientBuilder(AddrUtil.getAddresses(CommonConfig.MC1Add));
			mcbuilder.setSessionLocator(new KetamaMemcachedSessionLocator());
			mcbuilder.setCommandFactory(new BinaryCommandFactory());
			mcbuilder.setConnectionPoolSize(MCPoolSize);
			mcbuilder.setFailureMode(MCFailureMode);
			mc1 = mcbuilder.build();
			mc1.setOpTimeout(8000L);
			logger.info("mc1 connected!");

			mcbuilder = new XMemcachedClientBuilder(AddrUtil.getAddresses(CommonConfig.MC2Add));
			mcbuilder.setSessionLocator(new KetamaMemcachedSessionLocator());
			mcbuilder.setCommandFactory(new BinaryCommandFactory());
			mcbuilder.setConnectionPoolSize(MCPoolSize);
			mcbuilder.setFailureMode(MCFailureMode);
			mc2 = mcbuilder.build();
			mc2.setOpTimeout(8000L);
			logger.info("mc2 connected!");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void setDataCount(String key, String value) {
		JSONObject obj = new JSONObject();
		obj.accumulate("type", key);
		obj.accumulate("data", value);
		obj.accumulate("time", (new Date()).getTime() / 1000L);
		try {
			mc1.set(key, 0, obj.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		try {
			mc2.set(key, 0, obj.toString());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private String getValueByKeyFromMC1(String key) {
		try {
			return mc1.get(key);
		} catch (Throwable e) {
			logger.error(e);
			return null;
		}
	}

	private String getValueByKeyFromMC2(String key) {
		try {
			return mc2.get(key);
		} catch (Throwable e) {
			return null;
		}
	}

	public String getCountByKey(String key) {
		String res_a = getValueByKeyFromMC1(key);
		String res_b = getValueByKeyFromMC2(key);
		JSONObject obj_a = JSONObject.fromObject(res_a);
		JSONObject obj_b = JSONObject.fromObject(res_b);
		long time_b = obj_b.getLong("time");
		long time_a = obj_a.getLong("time");

		if (time_a > time_b) {
			return obj_b.getString("data");
		} else {
			return obj_a.getString("data");
		}
	}
}
