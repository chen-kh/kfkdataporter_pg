package act.nsfc.kfkDataPorterPG.kafkaPorter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.dataHandler.EVENTHandler;
import act.nsfc.kfkDataPorterPG.mcService.MCController;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class KFKEVENTConsumer implements Runnable {
	private Logger logger = LoggerRepository.instance.getEventLogger();
	// private Logger log = Logger.getLogger(KFKEVENTConsumer.class);
	private String topic;

	public KFKEVENTConsumer(boolean isErrorCodeEvent) {
		this.topic = isErrorCodeEvent ? CommonConfig.KfkErrorCodeEventTopic : CommonConfig.KfkObdEventTopic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", CommonConfig.ZKAdd);
		props.put("group.id", CommonConfig.KfkGroupId_pg);
		props.put("zookeeper.session.timeout.ms", "10000");
		props.put("auto.commit.enable", "true");
		props.put("auto.offset.reset", "largest");
		props.put("auto.commit.interval.ms", "60000");
		return new ConsumerConfig(props);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());
		Map<String, Integer> topicMap = new HashMap<String, Integer>();

		topicMap.put(this.topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topicMap);
		KafkaStream<byte[], byte[]> stream = streamMap.get(this.topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		byte[] bs = null;
		EVENTHandler eventHandler = new EVENTHandler();
		updateDataCount();
		try {
			while (true) {
				try {
					if (it.hasNext()) {
						MessageAndMetadata<byte[], byte[]> item = it.next();
						bs = item.message();
						eventHandler.handle(topic, bs);
					}
				} catch (Exception ee) {
					logger.error(ee);
					Thread.sleep(10);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			stream.clear();
			streamMap.clear();
			consumer.shutdown();
		}
	}

	private void updateDataCount() {
		try {
			DataInfo.EVENTCOUNT = Long
					.parseLong(MCController.instance.getCountByKey(CommonConfig.KfkGroupId_pg + "_event"));
			logger.info("event count: " + DataInfo.EVENTCOUNT);
		} catch (Exception e) {
			logger.error(e);
			// log.error(e.getStackTrace().toString());
		}
	}
}
