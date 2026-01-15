package solar.server.socket.handlers;
import java.util.ArrayList;
import java.util.Date;

import solar.server.dao.DeviceSolarDAO;
import solar.server.models.DeviceSolar;

public class messrq2 {
//	private String messrq2;
	ArrayList<String> E_today_temp = new ArrayList<String>();
//	private DeviceSolarDAO DeviceSolarDAO;
	private CommandParser CommandParser;
	private DeviceSolarDAO deviceSolarDAO = new DeviceSolarDAO();
	/* Print the messrq2 details */
	public void printmessrq2(String messrq2){
		System.out.println("messrq2:"+ messrq2 );
	}
	/* Check RQ2 */
	public boolean checkrq2(String messrq2){
		boolean result_2= messrq2.contains("RQ2");
		return result_2;
		}
	public boolean checkrq1(String messrq2){
		boolean result_2= messrq2.contains("RQ1");
		return result_2;
		}
	/* Tach RQ2 */
	public String[] parsermessrq2(String messrq2){
//		 System.out.println("mess"+  messrq2);
		 String[] readParser = messrq2.split("[-= ,&{}\n]+"); 
		 for(int i=0; i< readParser.length; i++)
	     {
	         System.out.println("messrq["+i+"] : "+readParser[i]);
	     }
		return readParser;
	}
//	/* process CRC*/
	public void procesCRCmessrq2(String[] readParser) throws Exception {
		/* Create CRC*/
		String[] String_Document = CommandParser.Au_CRC(readParser[1]); 
		String tem_crc = String_Document[4]+","+readParser[2]+","+String_Document[8]+","+String_Document[10]+","+readParser[3]+","+readParser[4];
		System.out.println("String create CRC mongo:"+tem_crc);
		String Au_CRC= CommandParser.getCRC16ARC(tem_crc);
		System.out.println("CRC mongodb:"+Au_CRC);
		System.out.println("CRC Mess Reques:"+readParser[5]);
	 /* Compare CRC receiver and CRC created form request*/
	 boolean Au_re = readParser[5].equals(Au_CRC);
	 System.out.println("Ket qua so sanh CRC:"+Au_re);
	}
//	
	public String procesCRCmessrq1(String[] readParser) throws Exception {
		/* Create CRC*/
		String[] String_Document = CommandParser.Au_CRC(readParser[1]); 
		String tem_crc = String_Document[4]+","+readParser[2]+","+String_Document[8]+","+String_Document[10]+","+readParser[3]+","+readParser[4];
		System.out.println("String create CRC mongo:"+tem_crc);
		String Au_CRC= CommandParser.getCRC16ARC(tem_crc);
		System.out.println("CRC mongodb:"+Au_CRC);
		System.out.println("CRC Mess Reques:"+readParser[5]);
		/* Compare CRC receiver and CRC created form request*/
		 boolean Au_re = readParser[5].equals(Au_CRC);
		 System.out.println("Ket qua so sanh CRC:"+Au_re);
			/* Creat temp1 to CRC for RP */
		 String temp1=String_Document[4]+","+readParser[2]+","+String_Document[8]+","+String_Document[10]+","+CommandParser.getCurrentDate();
		 System.out.println("Creat temp1 to CRC for RP:"+temp1);
		 /* Content RP */
		 String Content_RP ="RP1"+","+String_Document[4]+","+CommandParser.getCurrentDate()+","+CommandParser.getCRC16ARC(temp1);
		 System.out.println("Content_RP:"+Content_RP);
		 return Content_RP;
	}

	public boolean checkdate(String date_mess, String date_current){
		boolean check_date= date_mess.contains(date_current);
		return check_date;
		}
	/* Save data to arrylist if same date*/
	public void temp(ArrayList<String> E_today_temp ,int n, String data, String date_mess, String date_current) {
	
		boolean result_date= date_mess.contains(date_current);
		if (result_date){
			E_today_temp.add(n,data);
			} else {
				System.out.println(" date no");
					}
		System.out.println(" date ok");
	}
	
	public boolean checkday(String date_mess, String date_current){
		boolean result_date= date_mess.contains(date_current);
		if (result_date){
				System.out.println(" date ok");
			} else {
				System.out.println(" date no");
					}
		return result_date;
	}
}
