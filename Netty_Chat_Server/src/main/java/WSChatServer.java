import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

public class WSChatServer {
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public static void main(String[] args) throws Exception{
        final WSChatServer endpoint = new WSChatServer();
        int port = 9999;

        ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
    public void destroy(){
        if(this.channel != null){
            this.channel.close();
        }
        this.channelGroup.close();
        this.group.shutdownGracefully();
    }
    public ChannelFuture start(InetSocketAddress address) throws Exception {
        //논블로킹 이벤트 루프 그룹 설정
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSChatServerInitializer(channelGroup));
        ChannelFuture future = b.bind(address);
        this.channel = future.channel();
        return future;
    }

}
