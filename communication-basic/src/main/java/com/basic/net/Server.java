package com.basic.net;

import com.basic.net.task.ReceiveTread;
import com.basic.net.task.SendThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * locate com.basic.net
 * Created by 79875 on 2017/4/17.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8888);
            Socket client = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out=new PrintWriter(client.getOutputStream());
            BufferedReader userin = new BufferedReader(new InputStreamReader(System.in));


            new ReceiveTread(server,in,out,userin,client).start();
            new SendThread(out, userin,true).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
