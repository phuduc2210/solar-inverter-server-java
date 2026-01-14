package solar.server.dao;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import solar.server.cache.DeviceSolarCache;
import solar.server.configs.MongoConfig;
import solar.server.models.DeviceSolar;

import com.google.common.collect.ImmutableList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.Iterator;

import com.mongodb.client.ClientSession;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase; 
//import com.mongodb.client.FindIterable; 
@Component
@Qualifier("deviceSolarDAO")
public class DeviceSolarDAO {
	public static final String DeviceSolarCollection = "DeviceSolar";
	ArrayList<String> Imei_temp = new ArrayList<String>();
	@Autowired
	public DeviceSolarCache DeviceSolarCache;

	@Autowired
	public MongoConfig mongoConfig;

	private static String idField = "_id";
	private static String intervalConnectField = "intervalConnect";
	private static String updateConfigField = "updateConfig";
	private static String dateDeviceConfigField = "dateDeviceConfig";
	
	public MongoTemplate mongoTempl() throws Exception {
		return mongoConfig.mongoTemplate();
	}
	//
	 private static DBObject getWhereClause_1(String Imei_1) {
	       BasicDBObjectBuilder whereBuilder = BasicDBObjectBuilder.start();
	       // Sử dụng method append (cũng giống với dùng add)
	       whereBuilder.append("_Imei", Imei_1);
	       //
	       DBObject where = whereBuilder.get();
	       System.out.println("Imei: " + where.toString());
	       return where;
	   }
   //
	public String[] Read_Document(String TrFi ,String Imei_1 , String Collection) throws Exception{
		/*Tim kiem docment voi Imei=Imei_1*/
		MongoClient Client = mongoConfig.mongoClient();
		MongoDatabase db = Client.getDatabase("coresolardb");
		MongoCollection<Document> collec = db.getCollection(Collection);
		System.out.println("Collection sampleCollection selected successfully");
//		Document myDoc = collec.find().first();
//		System.out.println(myDoc.toJson());
		
		FindIterable<Document> iterDoc = collec.find(Filters.eq(TrFi,Imei_1));
//		System.out.println("successfully+"+ iterDoc.first());
		  if (iterDoc.first() == null) {
			  System.out.println("no records at the collection");
			  return null;
			  }	  
		  else{
			  	  String[] Arr_dbObject;
			      String Device_Au = iterDoc.first().toString();
			      if(TrFi.equals("Imei")){
			    	  System.out.println("Imei");
				      int i = 1; 
				      Iterator it = iterDoc.iterator(); 
				      while (it.hasNext()) {  
				         System.out.println(it.next());  
				         i++; 
				      }     
					  Arr_dbObject = Device_Au.split("[-:,&{}\n= ]+"); 
			      } else{// read document in collection user
			    	  System.out.println("email");
			    	  int index1=Device_Au.indexOf("firm_ware");
			    	  int index2=Device_Au.indexOf("firmware_crc16");
			    	  String str1 = Device_Au.substring(index1, index1+18);
			    	  String str2 = Device_Au.substring(index2, index2+19);
			    	  String str = str1 + ":" + str2;
			    	  Arr_dbObject =  str.split("[-:,&{}\n = ]+");
			      }
			      
				  for(int i1=0; i1< Arr_dbObject.length; i1++)
				         {
				             System.out.println("Arr_dbObject["+i1+"] : "+Arr_dbObject[i1]);
				         }  
				  Client.close();
			      return Arr_dbObject;
			      
	
		  }
	}
	public DeviceSolar createDeviceSolar(DeviceSolar DeviceSolar) {
		if (DeviceSolar == null) {
			return null;
		}
		try {
			mongoTempl().save(DeviceSolar, DeviceSolarCollection);
			try {
				DeviceSolarCache.putDeviceSolarMap(DeviceSolar);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DeviceSolar;
	}
	/**
//	 * @throws Exception *****************************/
//	public void create_Document(String Imei_1 , String Collection) throws Exception {
//		MongoClient Client = mongoConfig.mongoClient();
//		MongoDatabase db = Client.getDatabase("coresolardb");
//		MongoCollection<Document> collec = db.getCollection(Collection);
//		System.out.println("Collection selected successfully");
//		Document document = new Document("Imei", "MongoDB")
//		.append("description", "database")
//		.append("likes", 100)
//		.append("url", "http://www.tutorialspoint.com/mongodb/")
//		.append("by", "tutorials point");
//		collec.insertOne(document);
//		System.out.println("Document inserted successfully");
//	}

	/*******************************/
	public String[] Read_Document_data(String Imei_1 , String Collection) throws Exception{
		MongoClient Client = mongoConfig.mongoClient();
		MongoDatabase db = Client.getDatabase("coresolardb");
		MongoCollection<Document> collec = db.getCollection(Collection);
		System.out.println("Collection DeviceSolar selected successfully");
		FindIterable<Document> iterDoc = collec.find(Filters.eq("_id",Imei_1));
	      String Device_Au = iterDoc.first().toString();
	      int i = 1; 
	      Iterator it = iterDoc.iterator(); 
	      while (it.hasNext()) {  
	         System.out.println(it.next());  
	         i++; 
	      }     
		String[] Arr_data= Device_Au.split("[:,\n= ]+"); 
		  for(int i1=0; i1< Arr_data.length; i1++)
	         {
	             System.out.println("Arr_data["+i1+"] : "+Arr_data[i1]);
	         }  
		System.out.println("Read data ok");
		return Arr_data;
	}
	
	
	public void Update_db(String Collection, String imei, String vsSwDv) throws Exception {
		// TODO Auto-generated method stub
		MongoClient Client = mongoConfig.mongoClient();
		MongoDatabase db = Client.getDatabase("coresolardb");
		MongoCollection<Document> collec = db.getCollection(Collection);
		//collection.updateOne(Filters.eq("name", "Robert"), Updates.set("city", "Delhi"));
		collec.updateOne(Filters.eq("Imei", imei), Updates.set("SfVs_device", vsSwDv));
		System.out.println("Update ok");
	}
	
	public void Update_date(String collection_date, String imei, String date_update) throws Exception {
		// TODO Auto-generated method stub
		MongoClient Client = mongoConfig.mongoClient();
		MongoDatabase db = Client.getDatabase("coresolardb");
		MongoCollection<Document> collec = db.getCollection(collection_date);
		FindIterable<Document> iterDoc = collec.find(Filters.eq("Imei",imei));
		 if (iterDoc.first() == null) {
			  System.out.println("No Date records at the collection");
				Document document = new Document("Imei", imei)
				.append("Day_end", date_update);
			    collec.insertOne(document);
			  }	  
		  else{
			  collec.updateOne(Filters.eq("Imei", imei), Updates.set("Day_end", date_update));
			  System.out.println("Update day ok");
		  }
	}
	/*******************************/
	
	public DeviceSolar getDeviceSolarByImei(String imei) {
		if (StringUtils.isEmpty(imei)) {
			return null;
		}
		DeviceSolar DeviceSolar = null;
		try {
			DeviceSolar = DeviceSolarCache.getDeviceSolarByImei(imei);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (DeviceSolar == null) {
			try {
				Query query = new Query(Criteria.where("_id").is(imei));
				DeviceSolar =mongoTempl().findOne(query, DeviceSolar.class,
						DeviceSolarCollection);

				try {
					if(DeviceSolar!=null) {
						DeviceSolarCache.putDeviceSolarMap(DeviceSolar);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return DeviceSolar;
	}

	public DeviceSolar getDeviceSolarBySSID(String ssid) {
		if (StringUtils.isEmpty(ssid)) {
			return null;
		}
		DeviceSolar DeviceSolar = null;
		try {
			DeviceSolar = DeviceSolarCache.getDeviceSolarBySSID(ssid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (DeviceSolar == null) {
			try {
				Query query = new Query(Criteria.where("sessionId").is(ssid));
				DeviceSolar =mongoTempl().findOne(query, DeviceSolar.class,
						DeviceSolarCollection);
				try {
					if(DeviceSolar!=null) {
						DeviceSolarCache.putDeviceSolarMap(DeviceSolar);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return DeviceSolar;
	}
	
	public DeviceSolar updateDeviceSolar(DeviceSolar DeviceSolar) {
		if (DeviceSolar == null) {
			return null;
		}

		try {
			mongoTempl().save(DeviceSolar, DeviceSolarCollection);
			try {
				DeviceSolarCache.putDeviceSolarMap(DeviceSolar);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DeviceSolar;
	}

	public int deleteDeviceSolarByImei(String imei) {
		Query query = new Query(Criteria.where("_id").is(imei));
		WriteResult result = null;
		try {
			result = mongoTempl().remove(query, DeviceSolar.class, DeviceSolarCollection);
			try {
				DeviceSolarCache.deleteDeviceSolarMapbyImei(imei);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.getN();
	}
	
	public void deleteDeviceSolarLoginMapbySSID(String ssid)
	{
		try {
			DeviceSolarCache.deleteDeviceSolarLoginMapbySSid(ssid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateIntervalConnect(String imei, long intervalConnect){
		if(StringUtils.isEmpty(imei)){
			return false;
		}
		try {
			Query query = new Query(Criteria.where(idField).is(imei));
			Update update = new Update();
			update.set(intervalConnectField, intervalConnect);
			update.set(updateConfigField,true);
			mongoTempl().updateFirst(query, update, DeviceSolar.class, DeviceSolarCollection);
			try {
				DeviceSolarCache.deleteDeviceSolarMapbyImei(imei);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean setUpdateConfig(DeviceSolar DeviceSolar){
		if(DeviceSolar==null){
			return false;
		}
		try {
			Query query = new Query(Criteria.where(idField).is(DeviceSolar.getImei()));
			Update update = new Update();
			update.set(dateDeviceConfigField, DeviceSolar.getDateDeviceConfig());
			update.set(updateConfigField,DeviceSolar.isUpdateConfig());
			mongoTempl().updateFirst(query, update, DeviceSolar.class, DeviceSolarCollection);
			try {
				DeviceSolarCache.putDeviceSolarMap(DeviceSolar);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}



}