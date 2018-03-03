package act.nsfc.kfkDataPorterPG.bean;

import com.google.gson.Gson;

public class OBD {

	public String devicesn = "";
	public long gpstime;
	public double total_mileage = -1;
	public double total_fuel = -1;
	public double mileage = -1;
	public double speed = -1;
	public double engine_speed = -1;
	// public long processtime = -1;
	public String VIN = "";
	public double mileage_sup = -1;
	public double fuel_sup = -1;

	public OBD(String devicesn, long time, double t_mil, double t_fuel, double mil, double speed, double e_speed,
			String VIN, double mileage_sup, double fuel_sup) {
		this.devicesn = devicesn;
		this.gpstime = time;
		this.total_fuel = t_fuel;
		this.total_mileage = t_mil;
		this.mileage = mil;
		this.speed = speed;
		this.engine_speed = e_speed;
		this.VIN = VIN;
		this.mileage_sup = mileage_sup;
		this.fuel_sup = fuel_sup;
	}

	public OBD() {
	}

	public String getDevicesn() {
		return this.devicesn;
	}

	public long getGpstime() {
		return this.gpstime;
	}

	public double getTotalMileage() {
		return this.total_mileage;
	}

	public double getTotalFuel() {
		return this.total_fuel;
	}

	public double getMileage() {
		return this.mileage;
	}

	public double getSpeed() {
		return this.speed;
	}

	public double getEngineSpeed() {
		return this.engine_speed;
	}

	public String getVIN() {
		return this.VIN;
	}

	public double getMileageSup() {
		return this.mileage_sup;
	}

	public double getFuelSup() {
		return this.fuel_sup;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
