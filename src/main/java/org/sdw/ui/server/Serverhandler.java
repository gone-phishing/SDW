package org.sdw.ui.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class Serverhandler extends Thread
{
   private Socket socket = null;
   private String name = null;

   public Serverhandler(Socket s)
   {
      super("Server handler");
      this.socket = s;
   }

   public void run()
   {
      try
      (
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      )
      {
         System.out.println("hello");
      }
      catch(IOException iex)
      {
         iex.printStackTrace();
      }
   }
}
