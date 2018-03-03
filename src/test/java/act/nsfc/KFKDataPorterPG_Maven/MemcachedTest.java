package act.nsfc.KFKDataPorterPG_Maven;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemcachedTest {

	public static void main(String[] args) {
		//新建一个创建器,通过AddrUtil获取memcached的ip:port
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("192.168.3.152:11211"));
		//memcached客户端
		MemcachedClient memcachedClient;
		try {
			//建立一个客户连接
			memcachedClient = builder.build();
			//set一个键值对，参数1：key 参数2：expireTimeS,参数3：value
			memcachedClient.set("hello", 0, "Hello,xmemcached");
			//get根据键取值
			String value = memcachedClient.get("hello");
			System.out.println("hello=" + value);
			//delete根据键删去值
			memcachedClient.delete("hello");
			value = memcachedClient.get("hello");
			System.out.println("hello=" + value);
			// close memcached client
			memcachedClient.shutdown();
		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
		} catch (IOException e) {
			System.err.println("Shutdown MemcachedClient fail");
			e.printStackTrace();
		}
	}
}
