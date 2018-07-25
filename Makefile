JCC = javac
JFLAGS = -g

default: hello.class

hello.class: hello.java
	$(JCC) $(JFLAGS) hello.java
