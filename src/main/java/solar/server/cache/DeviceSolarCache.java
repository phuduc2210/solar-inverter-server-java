package solar.server.cache;

import java.util.concurrent.TimeUnit;

import solar.server.configs.HazelcastConfig;
import solar.server.models.DeviceSolar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
@Qualifier("DeviceSolarCache")
public class DeviceSolarCache {
	@Autowired
	private HazelcastConfig hazelcastConfig;
	
	private HazelcastInstance hazelcastInstance(){
		return hazelcastConfig.getClient();
	}
	
	private static final String DEVICE_SOLAR_MAP = "DeviceSolarMap";
	private static final String DEVICE_SOLAR_LOGIN_MAP = "DeviceSolarLoginMap";
	private static final String DEVICE_SOLAR_SECRET_MAP = "DeviceSolarSecretMap";
	
	private static final int DEVICE_SOLAR_MAP_TTL = 1 * 24 * 3600; // 1 day
	private static final int DEVICE_SOLAR_LOGIN_MAP_TTL = 1 * 24 * 3600; // 1 day
	private static final int DEVICE_SOLAR_SECRET_MAP_TTL = 1 * 24 * 3600; // 1 day
	
	
	// ---------------------- IMAP ------------------------------
	private static IMap<String, DeviceSolar> DeviceSolarMap;
	private static IMap<String, String> DeviceSolarLoginMap; //SSID --- imei	
	private static IMap<String, String> DeviceSolarSecretMap; //secretkey --- imei
	
	private synchronized IMap<String, DeviceSolar> getDeviceSolarMap() {
		try {
			if (DeviceSolarMap == null) {
				DeviceSolarMap = hazelcastInstance().getMap(
						DEVICE_SOLAR_MAP);
			}
			return DeviceSolarMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private synchronized IMap<String, String> getDeviceSolarLoginMap() {
		try {
			if (DeviceSolarLoginMap == null) {
				DeviceSolarLoginMap = hazelcastInstance().getMap(
						DEVICE_SOLAR_LOGIN_MAP);
			}
			return DeviceSolarLoginMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private synchronized IMap<String, String> getDeviceSolarSecretMap() {
		try {
			if (DeviceSolarSecretMap == null) {
				DeviceSolarSecretMap = hazelcastInstance().getMap(
						DEVICE_SOLAR_SECRET_MAP);
			}
			return DeviceSolarSecretMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// ---------------------- IMAP ------------------------------
	
	// ---------------------- DeviceSolarMap ----------------------
	public void putDeviceSolarMap(DeviceSolar DeviceSolar){
		getDeviceSolarMap().put(DeviceSolar.getImei(), DeviceSolar, DEVICE_SOLAR_MAP_TTL, TimeUnit.SECONDS);
		if(!StringUtils.isEmpty(DeviceSolar.getSessionId())){
			getDeviceSolarLoginMap().put(DeviceSolar.getSessionId(), DeviceSolar.getImei(), DEVICE_SOLAR_LOGIN_MAP_TTL, TimeUnit.SECONDS);
		}
		if(!StringUtils.isEmpty(DeviceSolar.getSecretkey())){
			getDeviceSolarSecretMap().put(DeviceSolar.getSecretkey(), DeviceSolar.getImei(), DEVICE_SOLAR_SECRET_MAP_TTL, TimeUnit.SECONDS);
		}
	}
	public DeviceSolar getDeviceSolarByImei(String imei){
		return getDeviceSolarMap().get(imei);
	}
	
	public void deleteDeviceSolarMapbyImei(String imei)
	{
		getDeviceSolarMap().delete(imei);
	}
	
	
	// ---------------------- DeviceSolarMap ----------------------
	
	// ---------------------- DeviceSolarLoginMap -----------------
	public DeviceSolar getDeviceSolarBySSID(String ssid){
		String imei= getDeviceSolarLoginMap().get(ssid);
		if(!StringUtils.isEmpty(imei)){
			return getDeviceSolarByImei(imei);
		}
		return null;
	}
	
	public void deleteDeviceSolarLoginMapbySSid(String ssid)
	{
		getDeviceSolarLoginMap().delete(ssid);
	}
	// ---------------------- DeviceSolarLoginMap -----------------
	
	// ---------------------- DeviceSolarSecretMap ----------------
	public DeviceSolar getDeviceSolarBySecretkey(String secret){
		String imei= getDeviceSolarSecretMap().get(secret);
		if(!StringUtils.isEmpty(imei)){
			return getDeviceSolarByImei(imei);
		}
		return null;
	}
	
	public void deleteDeviceSolarSecretMapbySecretkey(String secret)
	{
		getDeviceSolarSecretMap().delete(secret);
	}
	// ---------------------- DeviceSolarSecretMap ----------------
	
	// ---------------------- clearCache ------------------------
	public void clearCache(){
		hazelcastConfig.clearMapCache(DEVICE_SOLAR_MAP);
		hazelcastConfig.clearMapCache(DEVICE_SOLAR_LOGIN_MAP);
		hazelcastConfig.clearMapCache(DEVICE_SOLAR_SECRET_MAP);
	}
	// ---------------------- clearCache ------------------------
}
