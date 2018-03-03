package act.nsfc.KFKDataPorterPG_Maven;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationEvent;

public class NormalTest{
	public static String getRealPath() {
		String rootPath = NormalTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(rootPath);
		rootPath = rootPath.substring(0, rootPath.lastIndexOf("/") + 1);
		System.out.println(rootPath);
		return rootPath;
	}
	public static String Bytes2String(byte[] b) {
		char[] c = new char[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i] = (char) b[i];
		}
		return String.valueOf(c);
	}
	public static String Bytes2String2(byte[] b) {
		return new String(b);
	}
	public static void testMapClear(){
		Map<String, Integer> map = new HashMap<String,Integer>();
		map.put("apple", 34);
		map.put("orange", 35);
		map.put("banana", 68);
		map.put("lemon", 45);
		System.out.println(map.size() + "\t" + map.get("apple"));
		map.clear();
		System.out.println(map.size() + "\t" + map.get("apple"));
	}
	public static void son(int a){
		if(a == 0){
			return;
		}
		else{
			System.out.println("program work well!");
			a -= 1;
		}
		
	}
	public static void testHandl(int a){
		while (true){
			son(a);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		getRealPath();
		String string = "string";
		System.out.println(Bytes2String2(string.getBytes()));
		testMapClear();
		testHandl(5);
	}
}