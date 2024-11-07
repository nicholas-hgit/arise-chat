package com.arise.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("InfiniteLoopStatement")
public class ChatServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) {

        try(ServerSocket socket = new ServerSocket(1024);
            ExecutorService executor = Executors.newCachedThreadPool()) {

            while (true){
                executor.execute(new MessageHandler(socket.accept()));
            }

        } catch (IOException e) {
            logger.error("COULD NOT START SERVER",e);
        }
    }
}
