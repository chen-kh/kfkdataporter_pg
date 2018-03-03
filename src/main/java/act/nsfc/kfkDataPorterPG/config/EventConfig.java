package act.nsfc.kfkDataPorterPG.config;

import java.util.HashMap;
import java.util.Map;

public class EventConfig {
	public static final Map<String, Integer> EVENT_TYPE = new HashMap<String, Integer>();

	static {
		EVENT_TYPE.put("00000001", 66);// 鎬ュ姞閫熸姤璀�
		EVENT_TYPE.put("00000002", 67);// 鎬ュ噺閫熸姤璀�
		EVENT_TYPE.put("00000003", 73);// 鎺ュご鎻掑叆鎶ヨ
		EVENT_TYPE.put("00000004", 72);// VIN鐮佷笂鎶�
		EVENT_TYPE.put("00000005", 0);// 鏀寔鏁版嵁娴両D鍒楄〃涓婃姤
		EVENT_TYPE.put("00000006", 26);// 鍋滆溅鍚庤溅鐏湭鍏虫姤璀�
		EVENT_TYPE.put("00000007", 14);// 鍋滆溅鍚庡熬绠辨湭鍏虫姤璀�
		EVENT_TYPE.put("00000008", 13);// 鍋滆溅鍚庤溅绐楁湭鍏虫姤璀�
		EVENT_TYPE.put("00000009", 12);// 鍋滆溅鍚庤溅闂ㄦ湭閿佹姤璀�
		EVENT_TYPE.put("0000000A", 11);// 鍋滆溅鍚庤溅闂ㄦ湭鍏虫姤璀�
		EVENT_TYPE.put("0000000B", 3);// 鑳庡帇鎶ヨ
		EVENT_TYPE.put("0000000C", 0);// 鍋滆溅鍚庣姸鎬佸垽鏂�
		EVENT_TYPE.put("0000000D", 15);// 椹昏溅闇囧姩鎶ヨ
		EVENT_TYPE.put("0000000E", 2);// 姘存俯杩囬珮鎶ヨ-姘存俯
		EVENT_TYPE.put("0000000F", 27);// 瓒呴�熸姤璀�-閫熷害
		EVENT_TYPE.put("00000010", 22);// 鎬犻�熸姤璀�-鎬犻�熸椂闂�
		EVENT_TYPE.put("00000011", 4);// 鐢靛帇寮傚父鎶ヨ
		EVENT_TYPE.put("00000012", 50);// 杞�熷紓甯�
		EVENT_TYPE.put("00000013", 51);// 鎬ュ姞娌�
		EVENT_TYPE.put("00000014", 52);// 棰勭儹鏃堕棿杩囬暱
		EVENT_TYPE.put("00000015", 53);// 鍐疯溅楂橀�熻椹�
		EVENT_TYPE.put("00000016", 54);// 浣庢补閲忎笅琛岄┒
		EVENT_TYPE.put("00000017", 55);// 澶滈棿涓嶅紑鐏椹�
		EVENT_TYPE.put("00000018", 56);// 鎵嬪埞鏈В闄よ椹�
		EVENT_TYPE.put("00000019", 57);// 绌烘尅婊戣
		EVENT_TYPE.put("0000001A", 58);// 鍋滆溅涓嶆寕P/N妗�
		EVENT_TYPE.put("0000001B", 59);// 鏈郴瀹夊叏甯�
		EVENT_TYPE.put("0000001C", 60);// 杞﹂棬鏈叧琛岄┒
		EVENT_TYPE.put("0000001D", 61);// 杞﹂棬鏈攣琛岄┒
		EVENT_TYPE.put("0000001E", 62);// 灏剧鏈叧琛岄┒
		EVENT_TYPE.put("0000001F", 5);// 娌归噺涓嶈冻
		EVENT_TYPE.put("00000020", 0);// 杞﹁締鍚姩 16
		EVENT_TYPE.put("00000022", 74);// 鎺ュご澶辫仈
		EVENT_TYPE.put("00000025", 25);// 鐤插姵椹鹃┒

	}
}
