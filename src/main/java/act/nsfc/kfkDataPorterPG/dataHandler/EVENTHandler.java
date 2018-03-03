package act.nsfc.kfkDataPorterPG.dataHandler;

import org.apache.logging.log4j.Logger;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.protocol.TCompactProtocol;

import act.nsfc.kfkDataPorterPG.bean.DataInfo;
import act.nsfc.kfkDataPorterPG.bean.DataType;
import act.nsfc.kfkDataPorterPG.bean.LoggerRepository;
import act.nsfc.kfkDataPorterPG.bean.WarnEvent;
import act.nsfc.kfkDataPorterPG.config.CommonConfig;
import act.nsfc.kfkDataPorterPG.thrift.ThriftObdError;
import act.nsfc.kfkDataPorterPG.thrift.ThriftObdEvent;

public class EVENTHandler {
	private static DataHelper dataHelper = new DataHelper(DataType.EVENT);
	private TDeserializer tDeserializer = new TDeserializer(new TCompactProtocol.Factory());
	private ThriftObdError thriftObdError = null;
	private ThriftObdEvent thriftObdEvent = null;
	private WarnEvent even = null;
	private Logger logger = LoggerRepository.instance.getEventLogger();
	public void handle(String topic, byte[] bs) {
		if (topic.equals(CommonConfig.KfkErrorCodeEventTopic)) {// 错误码事件
			thriftObdError = new ThriftObdError();
			try {
				tDeserializer.deserialize(thriftObdError, bs);
				even = new WarnEvent();
				even.setDevicesn(thriftObdError.getSn());
				even.setGpstime(thriftObdError.getGpstime());
				even.setErrorcode(thriftObdError.getFaultCode());
				dataHelper.addEventData(topic, even);
				// DataHelper.instance.addEventData(topic,even);
				// process(topic, even);
				DataInfo.EVENTCOUNT++;
			} catch (Exception e) {
				logger.error(e);
			}
		} else {// 普通事件
			thriftObdEvent = new ThriftObdEvent();
			try {
				tDeserializer.deserialize(thriftObdEvent, bs);
				even = new WarnEvent();
				even.setDevicesn(thriftObdEvent.getSn());
				even.setGpstime(thriftObdEvent.getGpstime());
				even.setType(thriftObdEvent.getType());
				even.setLatitude(thriftObdEvent.getLat());
				even.setLongitude(thriftObdEvent.getLon());
				even.setDetail(thriftObdEvent.getOldVin() + "," + thriftObdEvent.getNewVin());
				dataHelper.addEventData(topic, even);
				// process(topic, even);
				DataInfo.EVENTCOUNT++;
			} catch (Exception e) {
				logger.error(e);
			}

		}
	}
	/*
	 * private void process(String topic , WarnEvent warnEvent) throws
	 * InterruptedException { try { if
	 * (topic.equals(CommonConfig.KfkErrorCodeEventTopic)) {// errorcode
	 * addErrorEvent(warnEvent); } else if (warnEvent.getType() == 72) {// vin
	 * change event addVinModifyRecords(warnEvent); } else {// other event
	 * addCommonEvent(warnEvent); } } catch (Exception e) { //
	 * log.error(e.toString()); e.printStackTrace(); } }
	 * 
	 * private void addErrorEvent(WarnEvent warnEvent) { try { prepSt =
	 * connection.prepareStatement(
	 * "insert into errorevent(devicesn,errorcode,gpstime,longitude,latitude) values (?,?,?,?,?)"
	 * ); prepSt.setString(1, warnEvent.getDevicesn()); prepSt.setString(2,
	 * warnEvent.getErrorcode()); prepSt.setLong(3, warnEvent.getGpstime());
	 * prepSt.setDouble(4, warnEvent.getLongitude()); prepSt.setDouble(5,
	 * warnEvent.getLatitude()); prepSt.executeUpdate(); //
	 * System.out.println("add an error event ........ error event error //
	 * error errorerrorerrorerrorerrorerrorerror"); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * private void addCommonEvent(WarnEvent warnEvent) { try { prepSt =
	 * connection.prepareStatement(
	 * "insert into common_events(devicesn,type_id,gpstime,longitude,latitude,detail) values(?,?,?,?,?,?)"
	 * ); prepSt.setString(1, warnEvent.getDevicesn()); prepSt.setInt(2,
	 * warnEvent.getType()); prepSt.setLong(3, warnEvent.getGpstime());
	 * prepSt.setDouble(4, warnEvent.getLongitude()); prepSt.setDouble(5,
	 * warnEvent.getLatitude()); prepSt.setString(6, warnEvent.getDetail());
	 * prepSt.executeUpdate(); // System.out.println("add a common event .......
	 * common common // common common common common common common common "); }
	 * catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * private void addVinModifyRecords(WarnEvent warnEvent) { String
	 * original_vin = ""; String new_vin = ""; String detail =
	 * warnEvent.getDetail(); String[] vinDetail = detail.split(","); if
	 * (vinDetail.length == 2) {// added for new data format,in // order to //
	 * store the vin_change_event // despite the lack of detail original_vin =
	 * vinDetail[0]; new_vin = vinDetail[1]; } try { prepSt =
	 * connection.prepareStatement(
	 * "insert into vin_modify_records (devicesn,gpstime,original_vin,new_vin,longitude,latitude) values (?,?,?,?,?,?)"
	 * ); prepSt.setString(1, warnEvent.getDevicesn()); prepSt.setLong(2,
	 * warnEvent.getGpstime()); prepSt.setString(3, original_vin);
	 * prepSt.setString(4, new_vin); prepSt.setDouble(5,
	 * warnEvent.getLongitude()); prepSt.setDouble(6, warnEvent.getLatitude());
	 * prepSt.executeUpdate(); // System.out.println("add vin modify records
	 * ..... vin modify vin // modify vin modify vin modify vin modify vin
	 * modify vin modify "); } catch (SQLException e) { e.printStackTrace(); } }
	 */
}
