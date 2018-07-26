JCC = javac
JFLAGS = -g

default: Client.class Server.class

Client.class: Client.java
	$(JCC) $(JFLAGS) Client.java

Server.class: Server.java
	$(JCC) $(JFLAGS) Server.java

