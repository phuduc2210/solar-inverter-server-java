package solar.server.socket.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import solar.server.dao.Authenticate_Device;


@Component
@Qualifier("serverHandler")
@Sharable
//public class ServerHandler extends SimpleChannelInboundHandler<Object> {
	public class ServerHandler extends ChannelInboundHandlerAdapter {	
	private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class);
	
	@Autowired
	private CommandParser commandParser;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//Channel comming =ctx.channel();	
//		commandParser.ParserDeviceSolarDocument("123456789");
		commandParser.readParser(msg,ctx);
//		commandParser.read_mess(msg, ctx);
//		commandParser.ParserDeviceSolarDocument("VTSL02345670");
//		commandParser.printmessrq2("Run");
//		Messrq2.printmessrq2("Why not run !");
//		commandParser.Authenti_Device();
//		System.out.println("Read_Document");
//		authenticate_Device.Read_Document();
//		System.out.println("Read_Document_OK");
//		LOG.info( msg.toString());
		//String ack = commandParser.readParser(msg,ctx);
		//ctx.channel().writeAndFlush(ack);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("Channel is active");
		super.channelActive(ctx);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("Channel is disconnected");
		super.channelInactive(ctx);
	}

}
