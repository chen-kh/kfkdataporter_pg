package act.nsfc.kfkDataPorterPG.bean;

import net.sf.json.JSONObject;
public class WarnEvent {
	private String devicesn;
	private long eventtime;
	private long gpstime;
	private int type;
	private double longitude;
	private double latitude;
	private String detail;
	private String errorcode="";
	private String raw;

	public WarnEvent(){

	}

	public WarnEvent(String devicesn, long eventtime, long gpstime, int type, double longitude, double latitude,
			String detail,String raw) {
		this.devicesn = devicesn;
		this.eventtime = eventtime;
		this.type = type;
		this.detail = detail;
		this.raw = raw;
		this.gpstime = gpstime;
		this.longitude = longitude;
		this.latitude = latitude;

	}

	public WarnEvent(String devicesn, long eventtime, int type, String detail, String errorcode, long gpstime,double longitude, double latitude,String raw) {
		this.devicesn = devicesn;
		this.eventtime = eventtime;
		this.type = type;
		this.detail = detail;
		this.errorcode = errorcode;
		this.raw = raw;
		this.gpstime = gpstime;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String toKFKString() {
		JSONObject res = new JSONObject();
		res.accumulate("devicesn", devicesn);
		res.accumulate("eventtime", eventtime);
		res.accumulate("type", type);
		res.accumulate("detail", detail);
		res.accumulate("gpstime", gpstime);
		res.accumulate("longitude", longitude);
		res.accumulate("latitude", latitude);
		res.accumulate("errorcode", errorcode);
		res.accumulate("raw", raw);
		return res.toString();
	}

	public String getDevicesn() {
		return devicesn;
	}

	public void setDevicesn(String devicesn) {
		this.devicesn = devicesn;
	}

	public long getEventtime() {
		return eventtime;
	}

	public void setEventtime(long eventtime) {
		this.eventtime = eventtime;
	}

	public long getGpstime() {
		return gpstime;
	}

	public void setGpstime(long gpstime) {
		this.gpstime = gpstime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}
	@Override 
	public String toString(){
		
		return "�¼�:\n"+"devicesn:"+devicesn+
				"\neventtime"+eventtime+"\ngpstime"+gpstime+
				"\ntype"+type+"\nlo"+longitude+"\nla"+latitude+"\ndetail"
				+detail+"\nerrorcode"+errorcode+"\nraw"+raw
		;
		
	}

}
