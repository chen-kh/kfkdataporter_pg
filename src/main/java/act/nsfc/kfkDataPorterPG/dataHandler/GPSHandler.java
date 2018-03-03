package act.nsfc.kfkDataPorterPG.dataHandler;


import org.apache.logging.log4j.Logger;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.bean.GPS;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.dataHandler.DataFilter;
import act.nsfc.kfkDataPorterPG.thrift.ThriftObdGps;

public class GPSHandler {
	private Logger logger = LoggerRepository.instance.getGpsLogger();
	private static TDeserializer tDeserializer = new TDeserializer(new TCompactProtocol.Factory());
	private static ThriftObdGps thriftObdGps = new ThriftObdGps();;
	private static DataHelper dataHelper = new DataHelper(DataType.GPS);
	public void handle(byte[] bs) {
		try {
			tDeserializer.deserialize(thriftObdGps, bs);
		} catch (TException e) {
			logger.error(e);
		}
		if (null == thriftObdGps.getSn()) {
			logger.error("thriftObdGps sn = 0");
			return;
		}
		
		try {
			String devicesn = thriftObdGps.getSn();
			long gpstime = thriftObdGps.getGpstime();
			double lon = thriftObdGps.getLon();
			double lat = thriftObdGps.getLat();
			double speed = thriftObdGps.getSpeed();
			int dir = thriftObdGps.getDirection();
			GPS gps = new GPS(devicesn, gpstime, lon, lat, speed, dir);
			if (DataFilter.instance.needFilt(gps)) {
//				logger.info("filted data : " + gps.toString());
				return;
			}
			dataHelper.addGpsData(devicesn, gps);
			DataInfo.GPSCOUNT++;
		} catch (Exception e) {
			logger.error(e);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			for (byte b : bs) {
				stringBuffer.append(b + ",");
			}
			stringBuffer.append("]");
			logger.error(stringBuffer);
		}
	}

}
