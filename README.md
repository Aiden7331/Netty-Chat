# Netty-Chat
네티 연습 - Netty Client와 Netty Server를 이용한 채팅 프로그램

## Codec

- LineBasedFrameDecoder  
  행 종료 \n 또는 \r\n으로 구분된 프레임을 추출하는 디코더

- StringDecoder  
  MessageToMessageDecoder를 상속받는 ByteBuf를 String으로 변환하는 디코더

- StringEncoder  
  MessageToMessageEncoder를 상속받는 String을 CharSequence로 변환하는 인코더


## Server

- ServerBootStrap  
  서버측 port 번호 설정  
  NIO방식(Select를 이용, 소켓프로그래밍의 Multiplexing과 유사) 설정  
  pipeline 구성

- ChatServerHandler  
  ChannelGroup 생성  
  Channel이 EventLoop에 등록될때 ChannelGroup에 채널 삽입  
  Channel이 EventLoop에 등록해제될때 ChannelGroup에서 채널 삭제  

## Client

- BootStrap  
  서버측 호스트 주소 및 port 번호 설정  
  NIO방식 설정  
  pipeline 구성  
  
- ChatClientHandler  
  Server로 부터 데이터를 읽어 Console에 출력  
