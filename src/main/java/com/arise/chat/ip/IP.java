package com.arise.chat.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Optional;

public class IP {

    private static final Logger logger = LoggerFactory.getLogger(IP.class);

    public static Optional<InetAddress> getAddress(){

        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            if(nis.hasMoreElements()){
                NetworkInterface ni = nis.nextElement();
                if(ni.isUp() && !ni.isLoopback()){
                     Enumeration<InetAddress> ips = ni.getInetAddresses();
                     if(ips.hasMoreElements()){
                         InetAddress ip = ips.nextElement();
                         return Optional.of(ip);
                     }
                }
            }

        } catch (SocketException e) {
             logger.error("COULD NOT GET IP ADDRESS",e);
        }

        return Optional.empty();
    }

}
