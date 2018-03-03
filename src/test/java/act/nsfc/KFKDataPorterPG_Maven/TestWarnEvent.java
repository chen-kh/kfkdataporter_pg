package act.nsfc.KFKDataPorterPG_Maven;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TCompactProtocol;

import act.nsfc.kfkDataPorterPG.bean.WarnEvent;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.thrift.ThriftObdEvent;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class TestWarnEvent {
	private static String topic = CommonConfig.KfkObdEventTopic;

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

	public static void main(String[] args) {
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		topicMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topicMap);
		KafkaStream<byte[], byte[]> stream = streamMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		byte[] bs = null;
		TDeserializer tDeserializer = new TDeserializer(new TCompactProtocol.Factory());
		ThriftObdEvent thriftObdEvent = new ThriftObdEvent();
		try {
			while (true) {
				try {
					if (it.hasNext()) {
						MessageAndMetadata<byte[], byte[]> item = it.next();
						bs = item.message();
						try {
							tDeserializer.deserialize(thriftObdEvent, bs);
							WarnEvent even = new WarnEvent();
							even.setDevicesn(thriftObdEvent.getSn());
							even.setGpstime(thriftObdEvent.getGpstime());
							even.setType(thriftObdEvent.getType());
							even.setLatitude(thriftObdEvent.getLat());
							even.setLongitude(thriftObdEvent.getLon());
							even.setEventtime(thriftObdEvent.getEventtime());
							even.setDetail(thriftObdEvent.getOldVin() + "," + thriftObdEvent.getNewVin());
							System.out.println(even.toKFKString() + "\n\t" + thriftObdEvent.getRes());
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception ee) {
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			stream.clear();
			streamMap.clear();
			consumer.shutdown();
		}
	}
}
