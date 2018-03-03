package act.nsfc.kfkDataPorterPG.bean;

import com.google.gson.Gson;

public class GPS {
	public String devicesn = "";

	public long gpstime;

	public double longitude = -1;

	public double latitude = -1;

	public double speed = -1;

	public int direction = -1;
	public GPS(String devicesn, long time, double longitude, double latitude,double speed, int direction) {
		this.devicesn = devicesn;
		this.gpstime = time;
		this.latitude = latitude;
		this.longitude = longitude;
		this.speed = speed;
		this.direction = direction;
	}
	public GPS() {
	}
	public String getDevicesn(){
		return this.devicesn;
	}
	public long getGpstime(){
		return this.gpstime;
	}
	public double getLongitude(){
		return this.longitude;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public double getSpeed(){
		return this.speed;
	}
	public int getDirection(){
		return this.direction;
	}
 	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
