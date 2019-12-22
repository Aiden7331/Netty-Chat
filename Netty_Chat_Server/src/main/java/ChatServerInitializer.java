import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /*
            1. 구분기호 프로토콜 등록(LineBasedFrameDecoder, \n or \r\n로 구분된 프레임을 추출)
            2. 문자열 Decoder 등록
            3. 문자열 Encoder 등록
            4. ChatServerHandler 등록
         */
        ch.pipeline().addLast(new LineBasedFrameDecoder(64*1024))
                        .addLast(new StringDecoder())
                        .addLast(new StringEncoder())
                        .addLast(new ChatServerHandler());
    }
}
