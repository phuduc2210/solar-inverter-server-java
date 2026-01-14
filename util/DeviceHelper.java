package solar.server.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class DeviceHelper {
	
	private static final Pattern latpattern = Pattern.compile(            
            "(\\d{2})(\\d{2}\\.\\d+)");      
	private static final Pattern lonpattern = Pattern.compile(            
            "(\\d{3})(\\d{2}\\.\\d+)");      
	
	
	public static String generateToken(){
		return Long.toString(StringUtil.generateRandom(6));
	}
	public static String getSessionId() {
		return StringUtil.getTimeUUIDString();
	}
	public static String getHistoryId() {
		return StringUtil.getTimeUUIDString();
	}
	
	public static String generateSecretkey(){
		return StringUtil.getTimeUUIDString();
	}
	/*
	public static boolean checkSessionExpired(Date sessionDate){
		Date now = DateUtil.now();
		long diffMills = DateUtil.diffMillis(now, sessionDate);
		if(diffMills > StaticData.SSID_TIME_EXPIRED){
			return true;
		}
		return false;
	}
	*/
	public static boolean isSaveDeviceHistoryGPS(Date lastedDate){
		return true;
		// cau hinh mac dinh luon tra ve true de luon save history
		// sau nay co the xu ly lai chi save vao history 10 phut 1 lan
		/*if(lastedDate==null){
			return true;
		}
		Date now = DateUtil.now();
		long diffMills = DateUtil.diffMillis(now, lastedDate);
		if(diffMills >= StaticData.INTERVAL_DEVICE_GPS_HISTORY){
			return true;
		}
		return false;*/
	}
	
	public static String keyDailyDeviceHistoryGPS(String imei, Date date){
		String dateStr = DateUtil.getDateByFormat(date, DateUtil.FORMAT_DATE_DEFAULT);
		return imei + "_" + dateStr;
	}
	public static String keyDailyDeviceHistoryGPS(String imei){
		return keyDailyDeviceHistoryGPS(imei, DateUtil.now());
	}
	
	public static String LatPaser(String lat, String ns){
		Matcher parser = latpattern.matcher(lat);
        if (!parser.matches()) {
        	return null;
        }
        Double latitude = Double.valueOf(parser.group(1));
        latitude += Double.valueOf(parser.group(2)) / 60;
        if (ns.compareTo("S") == 0) {
            latitude = -latitude;
        }       
        return Double.toString(latitude);
	}
	public static String LonPaser(String lon, String ew){
		Matcher parser = lonpattern.matcher(lon);
        if (!parser.matches()) {
        	return null;
        }
     // Longitude      
        Double longitude = Double.valueOf(parser.group(1));
        longitude += Double.valueOf(parser.group(2)) / 60;
        if (ew.compareTo("W") == 0) {
        	longitude = -longitude;
        }
		return Double.toString(longitude);
	}
	
	public static boolean isEmptyCell(String lac, String cid){
		if(StringUtils.isEmpty(lac) || StringUtils.isEmpty(cid)
				|| "0".equals(lac) || "0".equals(cid)){
			return true;
		}
		return false;
	}
	
	public static String getIdFromLacCell(String lac, String cellId){
		return lac+"_"+cellId;
	}
	
	public static boolean isEmptyLatLon(String lat, String lon){
		if(StringUtils.isEmpty(lat) || StringUtils.isEmpty(lon)
				|| "0".equals(lat) || "0".equals(lon)){
			return true;
		}
		return false;
	}
	
	public static boolean isEmptyLatLon(double lat, double lon){
		if((0d==lat) || (0d==lon)){
			return true;
		}
		return false;
	}
	
	public static long convertRxlevToRadius(long rxlev){
		long radius = 0l;
		if(rxlev > 58l){
			radius = 150l;
		}
		else if(rxlev > 53l){
			radius = 200l;
		}
		else if(rxlev > 48l){
			radius = 250l;
		}
		else if(rxlev > 43l){
			radius = 300l;
		}
		else if(rxlev > 38l){
			radius = 350l;
		}
		else if(rxlev > 33l){
			radius = 400l;
		}
		else if(rxlev > 28l){
			radius = 450l;
		}
		else if(rxlev > 23l){
			radius = 500l;
		}
		else if(rxlev > 18l){
			radius = 550l;
		}
		else if(rxlev > 13l){
			radius = 600l;
		}
		else if(rxlev > 8l){
			radius = 700l;
		}
		else if(rxlev > 3l){
			radius = 800l;
		}
		else{
			radius = 1000l;
		}
		
		return radius;
	}
	
	public static long convertRxlevToDbm(long rxlev){
		return (-110l + rxlev);
	}
	
	public static long convertDbmToRxlev(long dbm){
		return (dbm+110);
	}
}
