package solar.server.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.stereotype.Component;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@PropertySource(value = {
        "classpath:application-${spring.profiles.active:default}.properties"
})
@Component
@Qualifier("mongoConfig")
public class MongoConfig extends AbstractMongoConfiguration{
	private static final Logger LOG = LoggerFactory.getLogger(MongoConfig.class);

	@Value("${mongo.network.members}")
	private String networkMembers;
	
	@Value("${mongo.network.connectionPerHost}")
	private int connectionPerHost;
	
	@Value("${mongo.network.threadAllowBlock}")
	private int threadAllowBlock;
	
	@Value("${mongo.network.replicaSet}")
	private boolean isReplicaSet;
	
	@Value("${mongo.network.socketKeepAlive}")
	private boolean socketKeepAlive;
	
	@Value("${mongo.network.socketTimeout}")
	private int socketTimeout;
	
	@Value("${mongo.network.connectTimeout}")
	private int connectTimeout;
	
	@Value("${mongo.db.username}")
	private String username;
	
	@Value("${mongo.db.password}")
	private String password;
	
	@Value("${mongo.db.databaseName}")
	private String databaseName;
	
	@Autowired
	public MongoClient mongoClient;
    
	@Bean(name = "mongoClient")
	public MongoClient mongoClient() throws Exception {
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		for(String networkMember: Arrays.asList(networkMembers.split(","))){
			networkMember = networkMember.trim();
			List<String> network = Arrays.asList(networkMember.split(":"));
			String host = network.get(0);
			int port = Integer.parseInt(network.get(1));
			seeds.add(new ServerAddress(host,port));
		}

		MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
		MongoClientOptions options = MongoClientOptions.builder()
										//.writeConcern(WriteConcern.REPLICAS_SAFE)
										//.readPreference(ReadPreference.secondaryPreferred())		
										.connectionsPerHost(connectionPerHost) //recommence 40 < 100
										.threadsAllowedToBlockForConnectionMultiplier(threadAllowBlock) // recommence 1/4 connectionPerHost
										.socketKeepAlive(true) // true | false |socketKeepAlive
										.socketTimeout(socketTimeout) //millisecond
										.connectTimeout(connectTimeout) //millisecond
										.build();
		
		MongoClient client = new MongoClient(seeds, Arrays.asList(credential), options);
		if(isReplicaSet){
			client.setWriteConcern(WriteConcern.REPLICAS_SAFE);
		}
		
		LOG.info("----------------------new mongoClient-------------------------------");
		return client;
	}
	
	/*@Bean(name = "mongoDbFactory")
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongoClient(), coregpsdb);
	}
	
	@Bean(name = "mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		//replace rs.slaveOK()
		mongoTemplate.setReadPreference(ReadPreference.secondary());
		return mongoTemplate;
	}*/

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	public Mongo mongo() throws Exception {
		return mongoClient;
	}
	
	@PreDestroy
	public void shutdown() {
		try {
			LOG.info("MongoConfig shutdown: mongo().close()");
			mongo().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
}
