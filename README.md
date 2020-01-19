# WebSocket-Netty-Chat (V 0.1)
Web Socket 기반의 Netty Server Program

### 사용법
Web Browser에서 127.0.0.1:9999/ws로 websocket 생성시
HTTP가 websocket으로 업그레이드 된 후 join 메시지 전달.


### Codec

LineBasedFrameDecoder
- 행 종료 \n 또는 \r\n으로 구분된 프레임을 추출하는 디코더

StringDecoder
- MessageToMessageDecoder를 상속받는 ByteBuf를 String으로 변환하는 디코더

StringEncoder
- MessageToMessageEncoder를 상속받는 String을 CharSequence로 변환하는 인코더


## Server

## Client

