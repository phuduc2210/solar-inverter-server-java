package solar.server.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import solar.server.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DeviceSolar")
public class DeviceSolar implements Serializable {
    private static final long serialVersionUID = 02L;
    @Id
    private String imei;
    private String imeidate;
    private String userPhone;
    private String userAcount;
    private String devicePhone;
    private String pass;
    private String token;
    private String sessionId;
    private String secretkey;
    private Date dateTracking;
    private Date dateHistory;
    
    ArrayList<ValueMesument> valueMesument = new ArrayList<ValueMesument>();
   
	public List<ValueMesument> getValueMesument() {
		return valueMesument;
	}
	public void setValueMesument( ValueMesument V1) {
		this.valueMesument.add(V1);
}
	public String getInfomation() {
		return Infomation;
	}

	public void setInfomation(String infomation) {
		Infomation = infomation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
//    private int Err_code;
    private String Infomation;
    // WEB config (set by WEB)
    private long intervalConnect = 0;
    private boolean updateConfig = false;
    // current DEVICE config
    private Date dateDeviceConfig;
    private long updateTimeDevice = 0;
    private String typeConnectDevice;
    private String addressConnectDevice;
    private String portConnectDevice;
    private int trackingType=0; // 0: tiet kiem; 1:SOS; -1:lien tuc
    // activation
    private String swVersion;
    private String hwVersion;
    private Date dateActivation;
    private Date dateToken;
    // SSID date
    private Date dateSSID;
    private Date logoutDate;
    private int logoutCause = -1; // -1 default not logout

    public Date getDateSSID() {
        return dateSSID;
    }

    public void setDateSSID(Date dateSSID) {
        this.dateSSID = dateSSID;
    }

    public DeviceSolar() {
        super();
    }
    // For active Command
    public DeviceSolar(String imei, String userPhone,String devicePhone,String pass, Date now, ValueMesument V1) 
    {
        this.imei = imei;
        this.dateActivation = now;
        this.dateToken = now;
        this.dateDeviceConfig = now;
        this.dateHistory = now;
        this.userPhone	 = userPhone;
        this.devicePhone = devicePhone;
        this.pass		 = pass;
        this.token		 = "123456";
        this.valueMesument.add(V1);
    }
    public DeviceSolar(String imei)
    {
    	 this.imei = imei;
    	 this.token		 = "123456";
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImeidate() {
        return imeidate;
    }

    public void setImeidate(String imei) {
        this.imeidate = imei;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "123456";
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public Date getDateTracking() {
        return dateTracking;
    }

    public void setDateTracking(Date dateTracking) {
        this.dateTracking = dateTracking;
    }

    public Date getDateHistory() {
        return dateHistory;
    }

    public void setDateHistory(Date dateHistory) {
        this.dateHistory = dateHistory;
    }

    public long getIntervalConnect() {
        return intervalConnect;
    }

    public void setIntervalConnect(long intervalConnect) {
        this.intervalConnect = intervalConnect;
    }

    public boolean isUpdateConfig() {
        return updateConfig;
    }

    public void setUpdateConfig(boolean updateConfig) {
        this.updateConfig = updateConfig;
    }

    public Date getDateDeviceConfig() {
        return dateDeviceConfig;
    }

    public void setDateDeviceConfig(Date dateDeviceConfig) {
        this.dateDeviceConfig = dateDeviceConfig;
    }

    public long getUpdateTimeDevice() {
        return updateTimeDevice;
    }

    public void setUpdateTimeDevice(long updateTimeDevice) {
        this.updateTimeDevice = updateTimeDevice;
    }

    public String getTypeConnectDevice() {
        return typeConnectDevice;
    }

    public void setTypeConnectDevice(String typeConnectDevice) {
        this.typeConnectDevice = typeConnectDevice;
    }

    public String getAddressConnectDevice() {
        return addressConnectDevice;
    }

    public void setAddressConnectDevice(String addressConnectDevice) {
        this.addressConnectDevice = addressConnectDevice;
    }

    public String getPortConnectDevice() {
        return portConnectDevice;
    }

    public void setPortConnectDevice(String portConnectDevice) {
        this.portConnectDevice = portConnectDevice;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public Date getDateActivation() {
        return dateActivation;
    }

    public void setDateActivation(Date dateActivation) {
        this.dateActivation = dateActivation;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public int getLogoutCause() {
        return logoutCause;
    }

    public void setLogoutCause(int logoutCause) {
        this.logoutCause = logoutCause;
    }

    public int getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(int trackingType) {
        this.trackingType = trackingType;
    }

	public Date getDateToken() {
		return dateToken;
	}

	public void setDateToken(Date dateToken) {
		this.dateToken = dateToken;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getDevicePhone() {
		return devicePhone;
	}

	public void setDevicePhone(String devicePhone) {
		this.devicePhone = devicePhone;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}
