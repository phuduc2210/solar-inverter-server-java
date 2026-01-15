package solar.server.models;

import org.springframework.data.annotation.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//import m1.coregps.utils.DeviceHelper;
//import m1.coregps.utils.StaticData;

import org.springframework.data.annotation.Id;

public class ValueMesument implements Serializable {
//	@Id
//	private String [] ReadParser;
	  String E_today ;
	  String P_in ;
	  String P_out ;
	  String P_maxtoday ;
	  String V_grid ;
	  String I_grid ;
	  String F_grid ;
	  String power_factor ;
	  String V_PV1 ;
	  String I_PV1 ;
	  String V_PV2 ;
	  String I_PV2 ;
	  String I_leakage ;
	  String Temp ;
	  String SLLK ;
	  String TTGHD ;
	  String time ;
	  String Errcode ;
	  
public ValueMesument() {

		}
	  
public ValueMesument(String E_today, String P_in,String P_out ,String P_maxtoday ,String V_grid ,String I_grid ,String F_grid ,String power_factor,String V_PV1 ,String I_PV1 ,String V_PV2 ,String I_PV2 ,String I_leakage, String Temp,String SLLK,String TTGHD ,String time , String Errcode ){  
		   this.E_today=E_today;  
		   this.P_in=P_in;  
		   this.P_out= P_out;  
		   this.P_maxtoday=P_maxtoday;  
		   this.V_grid= V_grid;  
		   this.I_grid= I_grid;  
		   this.F_grid=F_grid;  
		   this.power_factor= power_factor;  
		   this.V_PV1 = V_PV1;
		   this.I_PV1 = I_PV1;
		   this.V_PV2 = V_PV2;
		   this.I_PV2 = I_PV2;
		   this.I_leakage = I_leakage;
		   this.Temp = Temp;
		   this.SLLK = SLLK;
		   this.TTGHD = TTGHD;
		   this.time = time;
		   this.Errcode = Errcode;
		  }  

		public String getErrcode() {
			return Errcode;
		}
		public void setErrcode(String Errcode) {
			this.Errcode = Errcode;
		}
		
		public String gettime() {
			return time;
		}
		public void settime(String time) {
			this.time = time;
		}

		public String getTTGHD() {
			return TTGHD;
		}
		public void setTTGHD(String TTGHD) {
			this.TTGHD = TTGHD;
		}

		public String getSLLK() {
			return SLLK;
		}
		public void setSLLK(String SLLK) {
			this.SLLK = SLLK;
		}

		public String getTemp() {
			return Temp;
		}
		public void setTemp(String Temp) {
			this.Temp = Temp;
		}
		
		public String getI_leakage() {
			return I_leakage;
		}
		public void setI_leakage(String I_leakage) {
			this.I_leakage = I_leakage;
		}

		public String getI_PV2() {
			return I_PV2;
		}
		public void setI_PV2(String I_PV2) {
			this.I_PV2 = I_PV2;
		}

		public String getV_PV2() {
			return V_PV2;
		}
		public void setV_PV2(String V_PV2) {
			this.V_PV2 = V_PV2;
		}
		
		public String getI_PV1() {
			return I_PV1;
		}
		public void setI_PV1(String I_PV1) {
			this.I_PV1 = I_PV1;
		}


		public String getV_PV1() {
			return V_PV1;
		}
		public void setV_PV1(String V_PV1) {
			this.V_PV1 = V_PV1;
		}

		public String getpower_factor() {
			return power_factor;
		}
		public void setpower_factor(String power_factor) {
			this.power_factor = power_factor;
		}


		public String getF_grid() {
			return F_grid;
		}
		public void setF_grid(String F_grid) {
			this.F_grid = F_grid;
		}


		public String getI_grid() {
			return I_grid;
		}
		public void setI_grid(String I_grid) {
			this.I_grid = I_grid;
		}

		public String getV_grid() {
			return V_grid;
		}
		public void setV_grid(String V_grid) {
			this.V_grid = V_grid;
		}
		public String getP_maxtoday() {
			return P_maxtoday;
		}
		public void setP_maxtoday(String P_maxtoday) {
			this.P_maxtoday = P_maxtoday;
		}

		public String getE_today() {
			return E_today;
		}
		public void setE_today(String E_today) {
			this.E_today = E_today;
		}
		public String getP_in() {
			return P_in;
		}
		public void setP_in(String P_in) {
			this.P_in = P_in;
		}
		public String getP_out() {
			return P_out;
		}
		public void setP_out(String P_out) {
			this.P_out = P_out;
		}
}
