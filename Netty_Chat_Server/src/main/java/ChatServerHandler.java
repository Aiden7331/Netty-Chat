import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    //연결된 모든 채널목록을 가짐
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        Channel newChannel = ctx.channel();
        System.out.println("[channelRegistered] :".concat(newChannel.remoteAddress().toString()));

        for(Channel channel : channelGroup){
            channel.write(
                    "[SERVER] ".concat(newChannel.remoteAddress().toString())
                            .concat(" login")
            );
        }
        channelGroup.add(newChannel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        Channel newChannel = ctx.channel();
        System.out.println("[New Client] remote address - ".concat(newChannel.remoteAddress().toString()));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception{
        Channel oldChannel = ctx.channel();
        System.out.println("[channelUnregistered] :".concat(oldChannel.remoteAddress().toString()));
        for(Channel channel : channelGroup){
            channel.write(
                    "[SERVER] ".concat(oldChannel.remoteAddress().toString())
                                .concat(" logout")
            );
        }
        channelGroup.remove(oldChannel);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception {
        String message = null;
        message = (String)msg;
        System.out.println("[channelRead] :".concat(message));
        Channel msgSender = ctx.channel();
        for(Channel channel : channelGroup){
                channel.writeAndFlush(
                        "[".concat(msgSender.remoteAddress().toString())
                            .concat("]")
                            .concat(message)
                            .concat("\n")
                );
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }


}
