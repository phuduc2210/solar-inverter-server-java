package solar.server.socket.handlers;
import static io.netty.buffer.Unpooled.copiedBuffer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
//import io.netty.handler.codec.http.*;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import solar.server.dao.DeviceSolarDAO;
import solar.server.models.DeviceSolar;
import solar.server.models.ValueMesument;
import solar.server.socket.handlers.messrq2;
import solar.server.util.DateUtil;
import solar.server.util.DeviceHelper;
import solar.server.util.StaticData.ErrCode;
import solar.server.util.StaticData.TypeNewsReceive;
import solar.server.util.StringUtil;

//import solar.server.socket.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mongodb.DBCursor;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;

@Component
//@Qualifier("commandParser")
public class CommandParser {
	private static final Logger LOG = LoggerFactory
			.getLogger(CommandParser.class);
	@Autowired
	private DeviceSolarDAO DeviceSolarDAO;
//	private DeviceSolarDAO deviceSolarDAO = new DeviceSolarDAO();
	private messrq2 Messrq2 = new messrq2();
	String VerifyCode;
//	int i_open = 0;
	ArrayList<String> E_today_temp = new ArrayList<String>();
	ArrayList<ValueMesument> DeviceSolarDocument= new ArrayList<ValueMesument>();

	public ArrayList<String> StrMsgToFrame(String msg)
	{  
		ArrayList<String> frame_data = new ArrayList<String>();
		StringTokenizer  tk = new StringTokenizer(msg, "[");
		String tmp;
		while(tk.hasMoreTokens())
		{   
			tmp = tk.nextToken();
			if(tmp.endsWith("]"))
			{
				System.out.println(tmp);
				frame_data.add(tmp.substring(0, tmp.length() -1));
			}	
			else
			{
				return frame_data;
			}	
		}
	
		return frame_data;
	}
	// Chuong trinh test
	public void read_mess(Object message, ChannelHandlerContext ctx) throws Exception 
	{	
		final FullHttpRequest request = (FullHttpRequest) message;
		String mess =  parseJosnRequest(request);
		System.out.println("FullHttpRequest "+ mess);
		Write_respon(message,ctx,"hello word");
	}
	/* Chuong trinh xu ly chinh */
	public void readParser(Object message, ChannelHandlerContext ctx) throws Exception  // Ham doc ban noi dung ban tin
	{	
		/* Khoi tao gia tri ban dau*/
		String tg="0"; //
		boolean Au_re2 = false ;//
		boolean VerifyCode_re = false ;//
		final FullHttpRequest request = (FullHttpRequest) message;
		String mess =  parseJosnRequest(request);// Ham doc noi dung ban tn Josn
		LOG.info(mess);
			System.out.println("FullHttpRequest "+ mess);// In noi dung ban tin
			/*Create CRC of mongdb*/
			    String[] readParser = Messrq2.parsermessrq2(mess);  // [-= ,&{}\n]+ Tac noi dung ban tin
				System.out.println("readParser[1]:"+readParser[1]); // Hien thi noi Imei vi tri 1 cua ban tin
	      		String[] String_Document = DeviceSolarDAO.Read_Document("Imei",readParser[1],"sampleCollection");// Xac nhan CRC tu mongodb voi Imei
	      		String[] String_VersionFw = Read_VersionFw("admin");// String_VersionFw[17]
	      	/*Kiem tra ban tin*/	 
			if (Messrq2.checkrq1(mess)){
				/*Kiem tra do dai cua thoi gian*/
				//int length = readParser[3].length();
				/*Tao lai crc tu mongodb*/
	    			String tem_crc_rq1 = String_Document[4]+","+readParser[2]+","+String_Document[6]+","+String_Document[8]+","+readParser[3]+","+readParser[4]+","+readParser[5]+"-"+readParser[6];
	    		/*Hien tri chuoi dung tao crc*/
	    			System.out.println("String create CRC mongo:"+tem_crc_rq1);
	    		/*Tinh gia tri crc*/
	    			String Au_CRC_rq1= getCRC16ARC(tem_crc_rq1);
	    		/*Hien thi gia tri crc*/
	    			System.out.println("CRC mongodb:"+Au_CRC_rq1);
	    		/*So sanh crc cua ban tin rq1 va crc tinh trong mongodb*/	
	    			System.out.println("CRC Mess Reques:"+readParser[7]);
	      			boolean Au_re1 = readParser[7].equals(Au_CRC_rq1);
	      			if (Au_re1==false) { ctx.close();
	      								System.out.println("Ket qua so sanh CRC: "+Au_re1);
	      								}
	      			else {
				      		/*Hien thi verifycode*/
				       		System.out.println("VerifyCode Mess Reques:"+readParser[2]);
				       		VerifyCode = readParser[2];
				    		/*Tinh gia tri version*/
							System.out.println("String_VersionFw:"+String_VersionFw[0]+","+String_VersionFw[1]+","
																  +String_VersionFw[2]+","+String_VersionFw[3]);
							/*Luu gia tri version hien tai cua thiet bi vao trong mongodb*/
							Insert_Vs_Dv_toDB("sampleCollection",readParser[1],"1.000-"+readParser[6]);
							/* So sanh gia tri version thiet bi hien tai va version luu tren server */
							int i1=Integer.parseInt(String_VersionFw[2]);
							int i2=Integer.parseInt(readParser[6]);
								if (i1>i2) tg = String_VersionFw[4];
								else tg = "0000";
							/*Tinh gia tri crc cho ban tin report*/
				   			String temp1=String_Document[4]+","+readParser[2]+","+String_Document[6]+","+String_Document[8]+","+getCurrentDate()+","+tg;
				  			System.out.println("Creat temp1 to CRC for RP:"+temp1);
							System.out.println("i1, i2, tg"+i1+i2+tg);
				  			/*Tao ban tin RP1*/
							String Content_RP ="RP1"+","+String_Document[4]+","+getCurrentDate()+","+tg+","+getCRC16ARC(temp1);
							System.out.println("Content_RP:"+Content_RP);
							/*truyen ban tin*/
							Write_respon(message,ctx,Content_RP);
							/*Luu ban tin vao log*/
							LOG.info(Content_RP);
	
				      	}
			}
			if (Messrq2.checkrq2(mess)){
				/*Tao string tinh crc*/
				String data_crc = readParser[5]+"="+readParser[6]+","+readParser[7]+"="+readParser[8]+","+readParser[9]+"="+readParser[10]+","+
						          readParser[11]+"="+readParser[12]+","+readParser[13]+"="+readParser[14]+","+readParser[15]+"="+readParser[16]+","+readParser[17]+"="+readParser[18]+","+readParser[19]+"="+readParser[20]+","+
						          readParser[21]+"="+readParser[22]+","+readParser[23]+"="+readParser[24]+","+readParser[25]+"="+readParser[26]+","+readParser[27]+"="+readParser[28]+","+readParser[29]+"="+readParser[30]+","+
								  readParser[31]+"="+readParser[32]+","+readParser[33]+"="+readParser[34]+","+readParser[35]+"="+readParser[36]+","+readParser[37]+"="+readParser[38]+","+readParser[39]+","+readParser[40]+"="+readParser[41];
				String tem_crc_rq2 = String_Document[4]+","+readParser[2]+","+String_Document[6]+","+String_Document[8]+","+readParser[3]+","+readParser[4]+","+data_crc;
				System.out.println("String create CRC mongo:"+tem_crc_rq2);
				/*Tinh crc*/
				String Au_CRC_rq2= getCRC16ARC(tem_crc_rq2);
				System.out.println("CRC mongodb:"+Au_CRC_rq2);
				/*Tao mang du lieu*/
				ValueMesument V1 = new ValueMesument(readParser[6],readParser[8],
						 readParser[10],readParser[12],
						 readParser[14],readParser[16],
						 readParser[18],readParser[20],
						 readParser[22],readParser[24],
						 readParser[26],readParser[28],
						 readParser[30],readParser[32],
						 readParser[34],readParser[36],
						 readParser[38]+","+readParser[39],readParser[41]); 
				/* check CRC*/
				System.out.println("CRC Mess Reques:"+readParser[42]);
				Au_re2 = readParser[42].equals(Au_CRC_rq2);
				VerifyCode_re = readParser[2].equals(VerifyCode);
				System.out.println("Ket qua so sanh CRC:"+Au_re2);
				System.out.println("Ket qua so sanh VerifyCode:"+VerifyCode_re);
				System.out.println("VerifyCode_mess 1:"+VerifyCode);
				System.out.println("VerifyCode_mess 2:"+readParser[2]);
				/*Lu du lieu vao mongodb*/
					if (Au_re2 && VerifyCode_re) {
						SavetoDB(V1,Imei_date(readParser));
						/* test*/
						String[] DateString = readParser[3].split("[/]+");
						for(int i=0; i< DateString.length; i++)
					     {
					         System.out.println("DateString["+i+"] : "+DateString[i]);
					     }
						/* Doc date da luu*/
						String[] date_Document = DeviceSolarDAO.Read_Document("Imei",readParser[1]+"_"+DateString[1]+"_"+DateString[2],"DateCollection");
//						date_Document[5]
						int i3=Integer.parseInt(date_Document[6]);
						int i4=Integer.parseInt(DateString[0]);
							if (i4 > i3){
								Update_date_db("DateCollection",readParser[1]+"_"+DateString[1]+"_"+DateString[2],DateString[0]);
								System.out.println("cap nhat ngay thanh cong" + i3 + i4);
							}else{
								System.out.println("Khong can cap nhat ngay");
							}
						LOG.info("SAVE DATA");
						ctx.close();
						}
					else{
						LOG.info("Fail Au_re2 && VerifyCode_re"+ Au_re2 + VerifyCode_re);
						ctx.close();
					}
			}
	}
	/* Read Document DeviceSolar*/
	public void ParserDeviceSolarDocument (String Imei) throws Exception{
		System.out.println("ParserDeviceSolarDocument");
		String[]String_Document=DeviceSolarDAO.Read_Document_data(Imei,"DeviceSolar");	
		
	}
	/*Create String of Document*/
//	@SuppressWarnings("null")
	public String[] Au_CRC(String Imei) throws Exception{
		System.out.println("Authenti_Device_0"); 
		String[]String_Document=DeviceSolarDAO.Read_Document("Imei",Imei,"sampleCollection");
		return String_Document;
	}
	
//
	public String[] Read_VersionFw(String admin) throws Exception{
		System.out.println("Read_VersionFw");
		String[]String_VersionFw=DeviceSolarDAO.Read_Document("email",admin,"users");
		return String_VersionFw;
	}
//	@SuppressWarnings("null")
	public void SavetoDB(ValueMesument V1, String q){
		System.out.println("MONGO BEGIN:" + q);
		Date date = new Date();
		System.out.println("check 1");
		DeviceSolar DeviceSolar = DeviceSolarDAO.getDeviceSolarByImei(q);
		if (DeviceSolar != null)
		{
			System.out.println("check 2");
			DeviceSolar.setImei(q);
			DeviceSolar.setUserPhone("1");
			DeviceSolar.setDevicePhone("1");
			DeviceSolar.setPass("123456");
			System.out.println("DeviceSolar != null");
			DeviceSolar.setValueMesument(V1);
		}
		else
		{
			DeviceSolar = new DeviceSolar(q,"1","1","1",date,V1);
			System.out.println("DeviceSolar = null");
		}
		/*Why ?*/
		DeviceSolarDAO.updateDeviceSolar(DeviceSolar);
		System.out.println("SAVE DATA MONGO OK");
		LOG.info(V1.toString());
	}
	
	public void Insert_Vs_Dv_toDB(String collection, String imei, String VsSwDv) throws Exception{
		DeviceSolarDAO.Update_db(collection,imei,VsSwDv);
	}
	
	public void Update_date_db(String collection, String imei, String date_update) throws Exception{
		DeviceSolarDAO.Update_date(collection,imei,date_update);
	}
	
	/*Create respon HTTP_1_1*/
	public void Write_respon(Object message, ChannelHandlerContext ctx, String Content_RP ){
        if (message instanceof FullHttpRequest)
        {  
            final FullHttpRequest request = (FullHttpRequest) message;
            final String responseMessage = Content_RP; 
            FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                copiedBuffer(responseMessage.getBytes())
            );

            if (HttpHeaders.isKeepAlive(request))
            {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, responseMessage.length());
            ctx.writeAndFlush(response);
        }
        else if (message instanceof HttpContent) {
          //  handleContent(ctx, request, (HttpContent) msg);
        	System.out.println("!HttpContent ");
        }
        else
        {
        	System.out.println("!FullHttpRequest ");
        }
		
	}
	/*Save mess to log*/
	public void Save_data(String mess1){
		String[] arrOfmess = mess1.split("[,&{}\n]+"); 
		  for(int i=0; i< arrOfmess.length; i++)
	         {
	             System.out.println("mess["+i+"] : "+arrOfmess[i]);
	             LOG.info( "mess["+i+"] : "+arrOfmess[i]);
	         }
	}

	public void ChannelWriteandFlush(String s, ChannelHandlerContext ctx) {
		ctx.channel().writeAndFlush(s);
	}
	/*Create CRC of a String*/
	 public String getCRC16ARC(String temp) {
	        
	        int[] table = {
	                0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
	                0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
	                0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
	                0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
	                0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
	                0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
	                0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
	                0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
	                0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
	                0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
	                0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
	                0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
	                0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
	                0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
	                0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
	                0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
	                0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
	                0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
	                0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
	                0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
	                0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
	                0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
	                0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
	                0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
	                0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
	                0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
	                0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
	                0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
	                0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
	                0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
	                0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
	                0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
	            };
	        byte[] bytes = temp.getBytes();
	        int crc = 0x0000;
	        for (byte b : bytes) {
	            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
	        }
	        String crc_string = Integer.toHexString(crc);
	        int leg = crc_string.length();
	        System.out.println("crc_string.length: " + leg);
	        if(leg==1) crc_string = "000"+crc_string;
	        if(leg==2) crc_string = "00"+crc_string;
	        if(leg==3) crc_string = "0"+crc_string;
	        return crc_string;
//	        return Integer.toHexString(crc);
	 }
	 /*Read content of a Request POST*/
	 private String parseJosnRequest(FullHttpRequest request){
	        ByteBuf jsonBuf = request.content();
	        String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
	        return jsonStr;
	    }
	 /*Get date current*/
    public String getCurrentDate() {
        String temp;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY,HH:mm:ss");
        Date date = new Date();
        temp = dateFormat.format(date);
        return temp;
    }
    public String getDate() {
        String temp;
        DateFormat dateFormat = new SimpleDateFormat("ddMMYY");
        Date date = new Date();
        temp = dateFormat.format(date);
        return temp;
    }
	 /*Get Imei_date*/
    public String Imei_date(String[] r) {
        String imei_date;
        imei_date = r[1]+"_"+r[38].replace("/", "");
        return imei_date;
    }
}