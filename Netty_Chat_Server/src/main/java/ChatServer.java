import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ChatServer {
    private int port;

    public ChatServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        new ChatServer(9000).start();
    }

    public void start() throws Exception {
        //논블로킹 이벤트 루프 그룹 설정
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChatServerInitializer());
            ChannelFuture future = b.bind().sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("Server bound");
                    }else{
                        System.out.println("Bound Attempt Failed");
                        future.cause().printStackTrace();
                    }
                }
            });
            /*
            채널의 closefuture를 얻을때까지
            현재 스래드 블로킹 상태 전환
            */
            future.channel().closeFuture().sync();
        } finally{
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();//4 ways handshake
        }
    }

}
