import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static{
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try{
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html",e);
        }
    }

    HttpRequestHandler(String wsUri){
        this.wsUri = wsUri;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if(wsUri.equalsIgnoreCase(request.getUri())){ //웹소켓 업그레이드가 요청된 경우
            ctx.fireChannelRead(request.retain()); // 참조 카운팅 및 이를 다음 InboundHandler로 전달
        }else{
            if(HttpHeaders.is100ContinueExpected(request)) {//100Continue 요청 처리
            send100Continue(ctx);
            }
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers()
                    .set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request); //Keep Alive. 소켓의 연결을 계속 유지할 것인지?
            if(keepAlive){
                response.headers()
                        .set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                //헤더에 KEEP_ALIVE 옵션을 달아줍니다.
                response.headers()
                        .set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            //응답 메시지를 클라이언트로 기록
            ctx.write(response);
            /*
                index.html을 클라이언트로 기록

             */
            if(ctx.pipeline().get(SslHandler.class) == null){
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            }else{
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            /*
            FullHttpResponse 데이터 컨테이너의 마지막을 의미하는
            LastHttpContent를 붙이고 flush. 클라이언트에 실제로 전송.
             */
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            /*
                Keep Alive요청이 들어오지 않았으면 요청을 처리한 후
                해당 채널을 종료
            */
            if(!keepAlive){
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    //클라이언트에 100 CONTINUE응답을 전송
    private static void send100Continue(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
        cause.printStackTrace();
        System.out.println("[HttpRequestHandler] ".concat(cause.getMessage()));
        ctx.close();
    }
}
