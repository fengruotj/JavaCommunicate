package com.basic.net.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * locate com.basic.net.task
 * Created by 79875 on 2017/4/18.
 */
public class ReceiveTread extends Thread{
    BufferedReader in ;
    ServerSocket server;
    PrintWriter out;
    BufferedReader userin;
    Socket client;


    public ReceiveTread(ServerSocket server,BufferedReader in,PrintWriter out,BufferedReader userin,Socket client) {
        this.in = in;
        this.server = server;
        this.client = client;
        this.out = out;
        this.userin = userin;
    }

    public ReceiveTread(Socket client ,BufferedReader in,PrintWriter out,BufferedReader userin) {
        this.in = in;
        this.client = client;
        this.out = out;
        this.userin = userin;
    }


    @Override
    public void run() {
        try {
            while(true){
                String info = in.readLine();
                while(info !=null){
                    System.out.println(info);
                    info = in.readLine();
                }
                if(in.readLine().equals("end")){
                    break;
                }
            }
            in.close();
            out.close();
            userin.close();
            if(client != null){
                client.close();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
