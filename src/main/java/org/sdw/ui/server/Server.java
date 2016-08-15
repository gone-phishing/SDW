package org.sdw.ui.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Server
{
    public static void main(String[] args) throws IOException
    {
        if(args.length != 1)
        {
            System.err.println("Usage: java HostServer <host-port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        boolean listen = true;

        try (ServerSocket ss = new ServerSocket(port))
        {
            // TODO : Get server public ip from eth0 or wlan
            System.out.println("Server hosted at -> "+ss.getLocalSocketAddress());
            while(listen)
            {
                new Serverhandler(ss.accept()).start();
            }
        }
        catch(IOException ex)
        {
            System.err.println("Listening failed on port : "+port);
            System.exit(-1);
        }
    }
}
