package com.arise.chat.server;

import com.arise.chat.util.WelcomeMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MessageHandler implements Runnable{

    private final Socket socket;
    private static final List<BufferedWriter> clientChannels;

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    static {
        clientChannels = new ArrayList<>();
    }

    MessageHandler(Socket socket){
        this.socket = socket;

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
            clientChannels.add(out);

        } catch (IOException e) {
             logger.error("FAILED TO GET SOCKET OUTPUT STREAM",e);
        }
    }
    @Override
    public void run() {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8))) {

            out.write(getServerASCII());

            out.write("Enter username");
            out.newLine();
            out.flush();

            String username = in.readLine();
            broadcastWelcomeMessage(username);

            while(!socket.isClosed()){

                if(in.ready()){

                    String message = in.readLine();
                    broadcastMessage(message,username);
                }
            }

        } catch (IOException e) {
             logger.error("FAILED TO GET SOCKET STREAMS",e);
        }
    }

    private static void broadcastMessage(String message,String username){

        clientChannels.forEach((channel) -> {
            try {

                channel.write("user::" + username);
                channel.newLine();
                channel.write(message);
                channel.newLine();
                channel.flush();

            } catch (IOException e) {
                 logger.error("FAILED TO BROADCAST MESSAGE",e);
            }
        });
    }

    private static void broadcastWelcomeMessage(String username){

        int index = new Random().nextInt(6);

        clientChannels.forEach((channel) -> {

            try {

                String message = WelcomeMessageFactory.getMessage(index).formatted(username);
                channel.write(message);
                channel.newLine();
                channel.flush();

            } catch (IOException e) {
                logger.error("FAILED TO BROADCAST WELCOME MESSAGE",e);
            }
        });

    }

    private static String getServerASCII(){
        return """
                   _____        .__              \s
                  /  _  \\_______|__| ______ ____ \s
                 /  /_\\  \\_  __ \\  |/  ___// __ \\\s
                /    |    \\  | \\/  |\\___ \\\\  ___/\s
                \\____|__  /__|  |__/____  >\\___  >
                        \\/              \\/     \\/\s
                """;
    }
}
