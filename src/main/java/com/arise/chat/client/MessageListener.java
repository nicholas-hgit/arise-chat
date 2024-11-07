package com.arise.chat.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class MessageListener implements Runnable {

    private final Socket socket;

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    MessageListener(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            while(!socket.isClosed()){

                if(in.ready()){

                    String message = in.readLine();
                    System.out.println(message);
                }
            }

        } catch (IOException e) {
             logger.error("COULD NOT GET SOCKET INPUT STREAM",e);
        }
    }
}
