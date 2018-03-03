package act.nsfc.kfkDataPorterPG.kafkaPorter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class MultiConsumer {

	private ConsumerConfig config;
	private String topic;
	private int threadsNum;
	private MessageExecutor executor;
	private ConsumerConnector connector;
	private ExecutorService threadPool;
	Thread t1, t2;

	public MultiConsumer(String topic, int threadsNum, MessageExecutor executor) {
		this.config = createConsumerConfig();
		this.topic = topic;
		this.threadsNum = threadsNum;
		this.executor = executor;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", "192.168.7.7:2181");
		props.put("group.id", "test");
		props.put("zookeeper.session.timeout.ms", "10000");
		props.put("auto.commit.enable", "true");
		props.put("auto.offset.reset", "smallest");
		props.put("auto.commit.interval.ms", "60000");

		return new ConsumerConfig(props);
	}

	public void start() throws Exception {
		connector = Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topics = new HashMap<String, Integer>();
		topics.put(topic, threadsNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector
				.createMessageStreams(topics);
		List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);
		threadPool = Executors.newFixedThreadPool(threadsNum);
		for (KafkaStream<byte[], byte[]> partition : partitions) {
			threadPool.execute(new MessageRunner(partition));
		}
	}

	public void close() {
		try {
			threadPool.shutdownNow();
		} catch (Exception e) {
			//
		} finally {
			connector.shutdown();
		}

	}

	class MessageRunner implements Runnable {
		private KafkaStream<byte[], byte[]> partition;

		MessageRunner(KafkaStream<byte[], byte[]> partition) {
			this.partition = partition;
		}

		public void run() {
			ConsumerIterator<byte[], byte[]> it = partition.iterator();

			while (it.hasNext()) {
				System.out.println("partition" + partition.clientId()
						+ " report");
				MessageAndMetadata<byte[], byte[]> item = it.next();
				System.out.println("partiton:" + item.partition());
				// System.out.println("offset:" + item.offset());
				executor.execute(new String(item.message()));//
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MultiConsumer consumer = null;
		try {
			MessageExecutor executor = new MessageExecutor() {

				public void execute(String message) {
					System.out.println(message);
				}
			};
			consumer = new MultiConsumer("OBD1", 2, executor);
			consumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	interface MessageExecutor {

		public void execute(String message);
	}

}
