package solar.server.dao;
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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class Authenticate_Device {
	@Autowired
	public MongoConfig mongoConfig;
	
//	public void Read_Document() throws Exception {
//		System.out.println("Read_Document_1");
//		MongoClient Client = mongoConfig.mongoClient();
//		System.out.println("Read_Document_2");
//		DB db = Client.getDB("coresolardb");
//		DBCollection collec=db.getCollection("Authenticate_Device");
//		DBCursor cursor = collec.find();
//		System.out.println("Read_Document_3");
//		while(cursor.hasNext())
//		{
//			int i=1;
//			System.out.println(cursor.next());
//			i++;
//		}
//		System.out.println("Read_Document_4");
	
//	}

	
//	@SuppressWarnings("deprecation")
//	DB db = mongoConfig.mongoClient.getDB("coresolardb");
//	DBCollection collec=db.getCollection("Authenticate_Device");
//	DBCursor cursor = collec.find();
//	while(cursor.hasNet())
//	{
//		int i=1;
//		System.out.println(cursor.next());
//		i++;
//	}
}
