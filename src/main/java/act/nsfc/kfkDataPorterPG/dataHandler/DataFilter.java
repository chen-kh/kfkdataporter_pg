package act.nsfc.kfkDataPorterPG.dataHandler;

import java.util.Hashtable;

import act.nsfc.kfkDataPorterPG.bean.GPS;
import act.nsfc.kfkDataPorterPG.bean.OBD;

public class DataFilter {
	public static DataFilter instance = new DataFilter();
	private Hashtable<String, OBD> obds = new Hashtable<String, OBD>();
	private Hashtable<String, GPS> gpss = new Hashtable<String, GPS>();

	private DataFilter() {
	}

	/**
	 * filt data,return true when the data needs to be discared.
	 * 
	 * @param o
	 * @return
	 */
	public boolean needFilt(OBD o) {

		if (isSameOBD(o)) {
			return true;
		}
		if (o.total_fuel == -1 && o.total_mileage == -1) {
			return true;
		}
		return false;
	}

	public boolean needFilt(GPS g) {
		if (isSameGPS(g)) {
			return true;
		}
		return false;
	}

	private boolean isSameGPS(GPS g) {
		if (!gpss.containsKey(g.devicesn)) {
			gpss.put(g.devicesn, g);
			return false;
		}
		GPS tmp = gpss.get(g.devicesn);

		if (gpstimeEqual(tmp, g)) {
			return true;
		} else {
			updateGPS(g);
			return false;
		}
	}

	private boolean isSameOBD(OBD o) {
		if (!obds.containsKey(o.devicesn)) {
			obds.put(o.devicesn, o);
			return false;
		}
		OBD tmp = obds.get(o.devicesn);

		if (gpstimeEqual(tmp, o)) {
			return true;
		} else {
			return false;
		}
	}

	private void updateGPS(GPS g) {
		gpss.put(g.devicesn, g);
	}

	private boolean gpstimeEqual(GPS one, GPS two) {
		if (one.gpstime != two.gpstime) {
			return false;
		}
//		if (one.latitude != two.latitude)
//			return false;
//		if (one.longitude != two.longitude)
//			return false;
//		if (one.speed != two.speed)
//			return false;
//		if (one.direction != two.direction)
//			return false;
		return true;
	}

	private boolean gpstimeEqual(OBD one, OBD two) {
		if (one.gpstime != two.gpstime) {
			return false;
		}
//		 if (one.mileage != two.mileage)
//		 return false;
//		 if (one.engine_speed != two.engine_speed)
//		 return false;
//		 if (one.total_fuel != two.total_fuel)
//		 return false;
//		 if (one.total_mileage != two.total_mileage)
//		 return false;
//		 if (one.speed != two.speed)
//		 return false;
		return true;
	}

	public OBD refillOBDData(OBD o) {
		OBD last = obds.get(o.devicesn);
		if (o.total_fuel == -1) {
			o.total_fuel = last.total_fuel;
		} else {
			last.total_fuel = o.total_fuel;
		}
		if (o.total_mileage == -1) {
			o.total_mileage = last.total_mileage;
		} else {
			last.total_mileage = o.total_mileage;
		}
		if (o.mileage == -1) {
			o.mileage = last.mileage;
		} else {
			last.mileage = o.mileage;
		}
		if (o.engine_speed == -1) {
			o.engine_speed = last.engine_speed;
		} else {
			last.engine_speed = o.engine_speed;
		}
		if (o.speed == -1) {
			o.speed = last.speed;
		} else {
			last.speed = o.speed;
		}
		updateOBD(o);
		return o;
	}

	private void updateOBD(OBD o) {
		obds.put(o.devicesn, o);
	}

}
