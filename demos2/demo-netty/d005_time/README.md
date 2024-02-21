
这回增加封装难度，写一个TimeQueryClient，作用是从server端获取当前时间

服务端:
TimeServer.start(port)

客户端:
TimeQueryClient client = new TimeQueryClient();
System.out.println(timeClient.getTime())
System.out.println(timeClient.getTime());
System.out.println(timeClient.getTime());
System.out.println(timeClient.getTime());
System.out.println(timeClient.getTime());

timeClient.getTime();方法返回当前时间，怎么调用都可以