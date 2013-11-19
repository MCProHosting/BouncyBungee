# BouncyBungee
## A tool created to link BungeeCord proxies together, in harmony.


BouncyBungee utilizes a NetCommand system designed to send messages via a Redis Publish/Subscribe system to send information and events across multiple BungeeCord instances.
This NetCommand system can be used by plugins, or applications to do anything needed on either side of the application.

This implementation is also designed to sync both player counts, and MOTD data. A list of players will be sent via the HeartBeat system constantly.

####Compiling Note
For this to compile properly, you will need to go to Settings > Compiler > Annotation Processors and check "Enable Annotation Processing"
You must also have the lombok plugin installed.
(assuming you are using IntelliJ)