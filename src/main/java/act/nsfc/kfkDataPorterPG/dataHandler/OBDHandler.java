package act.nsfc.kfkDataPorterPG.dataHandler;

import org.apache.logging.log4j.Logger;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.bean.OBD;
import act.nsfc.kfkDataPorterPG.thrift.ThriftObdDs;
import net.sf.json.JSONObject;

public class OBDHandler {
	private String KeyFuel = "42";
	private String KeyTotalMile = "65";
	private String KeyMile = "40";
	private String KeySpeed = "51";
	private String KeyEngineSpeed = "52";

	private String KeyVIN = "100";
	private String KeyMileage_sup = "101";
	private String KeyFuel_sup = "102";
	private static TDeserializer tDeserializer = new TDeserializer(new TCompactProtocol.Factory());;
	private static ThriftObdDs thriftObd = new ThriftObdDs();
	private static DataHelper dataHelper = new DataHelper(DataType.OBD);
	private Logger logger = LoggerRepository.instance.getObdLogger();
	public void handle(byte[] bs) {
		try {
			tDeserializer.deserialize(thriftObd, bs);
		} catch (TException e1) {
			logger.error(e1);
		}
		JSONObject res = JSONObject.fromObject(thriftObd.getRes());
		if (res == null || !res.containsKey("devicesn")) {
			return;
		}
		OBD obd = getRealOBD(res);

		try {
			if (DataFilter.instance.needFilt(obd)) {
//				System.out.println("filted data : " + obd.toString());
				return;
			}
			obd = DataFilter.instance.refillOBDData(obd);

			dataHelper.addObdData(obd.devicesn, obd);
			DataInfo.OBDCOUNT++;
		} catch (Exception e) {
			StringBuffer stringBuffer = new StringBuffer();
			logger.error(e);
			logger.error("catch exception at data : " + obd.toString());
			stringBuffer.append("[");
			for (byte b : bs) {
				stringBuffer.append(b + ",");
			}
			stringBuffer.append("]");
			logger.error(stringBuffer);
		}

	}

	private OBD getRealOBD(JSONObject res) {
		OBD o = new OBD();
		o.devicesn = res.getString("devicesn");
		o.gpstime = res.getLong("gpstime");
		if (res.containsKey(KeyFuel))
			o.total_fuel = res.getDouble(KeyFuel);
		if (res.containsKey(KeyTotalMile))
			o.total_mileage = res.getDouble(KeyTotalMile);
		if (res.containsKey(KeyMile))
			o.mileage = res.getDouble(KeyMile);
		if (res.containsKey(KeySpeed))
			o.speed = res.getDouble(KeySpeed);
		if (res.containsKey(KeyEngineSpeed))
			o.engine_speed = res.getDouble(KeyEngineSpeed);
		if (res.containsKey(KeyVIN))
			o.VIN = res.getString(KeyVIN);
		if (res.containsKey(KeyMileage_sup))
			o.mileage_sup = res.getDouble(KeyMileage_sup);
		if (res.containsKey(KeyFuel_sup))
			o.fuel_sup = res.getDouble(KeyFuel_sup);
		return o;
	}

	private void PrintDataErr() {
		// log.error("OBD data error");
	}

	
}