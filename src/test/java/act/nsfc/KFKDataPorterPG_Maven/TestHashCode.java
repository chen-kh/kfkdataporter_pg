package act.nsfc.KFKDataPorterPG_Maven;

import java.util.List;

public class TestHashCode {
	public static void main(String[] args) {
//		String devicesn1 = "967790202669";
		String devicesn = "967790145172|967790147267|967790145253|967790221104|967790144421|967790202669|967790135502|967790211096|967790143446|967790143472|967790217008|967790147011|967790136963|967790142111|967790222891";
		System.out.println(devicesn.split("\\|").length);
		for (String dev : devicesn.split("\\|")) {
			System.out.println(dev + " : " + Math.abs(dev.hashCode()) % 16);
		}
	}
}
