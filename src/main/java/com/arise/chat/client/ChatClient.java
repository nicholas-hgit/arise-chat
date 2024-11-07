package com.arise.chat.client;

import com.arise.chat.ip.IP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient {

    public static Logger logger = LoggerFactory.getLogger(ChatClient.class);

    public static void main(String[] args) {

        try(ExecutorService executor = Executors.newSingleThreadExecutor()) {

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(IP.getAddress().orElse(InetAddress.getLocalHost()),1024));

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in,StandardCharsets.UTF_8));

            executor.execute(new MessageListener(socket));

            while (!socket.isClosed()){

                if(in.ready()){

                    String message = in.readLine();
                    if(message.equalsIgnoreCase("Q")){
                        socket.close();

                    }else {

                        out.write(message);
                        out.newLine();
                        out.flush();
                    }
                }
            }

        } catch (IOException e) {
            logger.error("FAILED TO CONNECT TO SERVER",e);
        }
    }
}
