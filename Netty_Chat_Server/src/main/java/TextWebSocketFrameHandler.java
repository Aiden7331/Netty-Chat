import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group){
        this.group = group;
    }

    /*
        이 메서드는 연결이 오랫동안 유휴상태에 접어든 경우
        IdleStateEvent가 생성되고
        이때 이 Event를 처리하는데 사용되는 메서드이다.
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        /*
            HTTP가 WebSocket으로 업그레이드 되면 HandShake 완료 후 수행
         */

        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            //ctx.pipeline().remove(HttpRequestHandler.class);
            System.out.println("Client".concat(ctx.channel().toString()).concat("joined"));
            group.writeAndFlush(new TextWebSocketFrame("Client".concat(ctx.channel().toString()).concat("joined")));
            group.add(ctx.channel());
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //메시지의 참조 카운터를 증가시키고, ChannelGroup에 연결된 모든 클라이언트에 메시지 전송
        group.writeAndFlush(msg.retain());
    }
}
