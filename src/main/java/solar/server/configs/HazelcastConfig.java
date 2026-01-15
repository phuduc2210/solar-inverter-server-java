package solar.server.configs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

@PropertySource(value = {
        "classpath:application-${spring.profiles.active:default}.properties"
})
@Component
@Qualifier("hazelcastConfig")
public class HazelcastConfig {
	private static final Logger LOG = LoggerFactory.getLogger(HazelcastConfig.class);
	
	@Value("${hazelcast.group.name}")
	private String groupName;
	
	@Value("${hazelcast.group.password}")
	private String groupPassword;
	
	@Value("${hazelcast.network.members}")
	private String networkMembers;
	
	@Autowired
	private HazelcastInstance hazelcastClient;
	
	@Bean(name = "hazelcastClient")
	public HazelcastInstance hazelcastInstance() {
		List<String> networklist = Arrays.asList(networkMembers.split(","));
		String[] networkArray = networklist.toArray(new String[0]);
		
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getGroupConfig().setName(groupName)
									.setPassword(groupPassword);
		clientConfig.getNetworkConfig().addAddress(networkArray);
		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		LOG.info("----------------------inited hazelcast client----------------------");
		return client;
	}
	
	public HazelcastInstance getClient(){
		return hazelcastClient;
	}
	
	@PreDestroy
	public void shutdownHazelcast(){
		getClient().shutdown();
		LOG.info("----------------------shutdown hazelcast client----------------------");
	}
	
	// ---------------------- clearCache ------------------------
		@SuppressWarnings("rawtypes")
		public void clearMapCache(String mapName) {
			Map map = getClient().getMap(mapName);
			if (map != null) {
				map.clear();
			}
		}
		// ---------------------- clearCache ------------------------
}
