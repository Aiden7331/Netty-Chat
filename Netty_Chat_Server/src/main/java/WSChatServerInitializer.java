import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSChatServerInitializer extends ChannelInitializer<SocketChannel> {
    private final ChannelGroup group;

    public WSChatServerInitializer(ChannelGroup group){
        this.group = group;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /*
         */
        ch.pipeline().addLast(new HttpServerCodec())
                        .addLast(new ChunkedWriteHandler())
                        .addLast(new HttpObjectAggregator(64*1024))
                        //.addLast(new HttpRequestHandler("/ws"))
                        .addLast(new WebSocketServerProtocolHandler("/ws"))
                        .addLast(new TextWebSocketFrameHandler(group));
    }
}
