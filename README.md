# Netty-Chat (V 0.1)
Web Socket 기반의 Netty Server Program
 
### Codec

LineBasedFrameDecoder
- 행 종료 \n 또는 \r\n으로 구분된 프레임을 추출하는 디코더

StringDecoder
- MessageToMessageDecoder를 상속받는 ByteBuf를 String으로 변환하는 디코더

StringEncoder
- MessageToMessageEncoder를 상속받는 String을 CharSequence로 변환하는 인코더


## Server

## Client

