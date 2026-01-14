package solar.server.configs;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import solar.server.socket.handlers.StringProtocolInitalizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * This class contains the bean definitions for this netty server. These beans
 * are autowired into the respective java classes in org.nerdronix.* packages
 * using component scan feature of spring. Properties are injected using the
 * PropertySource. For more information look <a href=
 * "http://static.springsource.org/spring/docs/3.2.2.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html"
 * >here</a> and <a href=
 * "http://blog.springsource.com/2011/06/10/spring-3-1-m2-configuration-enhancements/"
 * >here</a>
 * 
 * @author trongkn
 * 
 */
@Configuration
@ComponentScan("solar.server")
@PropertySource(value = {
        "classpath:application-${spring.profiles.active:default}.properties"})
public class SpringConfig {
	
	@Value("${netty.boss.thread.count}")
	private int bossCount;

	@Value("${netty.worker.thread.count}")
	private int workerCount;

	@Value("${netty.tcp.port}")
	private int tcpPort;

	@Value("${netty.so.keepalive}")
	private boolean keepAlive;

	@Value("${netty.so.backlog}")
	private int backlog;
	
	@Value("${log4j.configuration}")
	private String log4jConfiguration;
	
//	@Value("${device.config.connect.type}")
//	private String deviceConfigConnectType;
//	
//	@Value("${device.config.connect.address}")
//	private String deviceConfigConnectAddress;
//	
//	@Value("${device.config.connect.port}")
//	private String deviceConfigConnectPort;
//	
//	@Value("${device.config.connect.updatetime}")
//	private long deviceConfigConnectUpdatetime;
	
	@Autowired
	@Qualifier("springProtocolInitializer")
	private StringProtocolInitalizer protocolInitalizer;
	
	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(protocolInitalizer);
		System.out.println("ServerBootstrap running");
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossCount);
	}

	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerCount);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpPort() {
		return new InetSocketAddress(tcpPort);
	}

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}

	@Bean(name = "stringEncoder")
	public StringEncoder stringEncoder() {
		return new StringEncoder();
	}

	@Bean(name = "stringDecoder")
	public StringDecoder stringDecoder() {
		return new StringDecoder();
	}

	/**
	 * Necessary to make the Value annotations work.
	 * 
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
