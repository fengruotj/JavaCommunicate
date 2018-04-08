package com.basic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/8.
 */
public class Server {
    public static int prot=8999;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(prot);
        ExecutorService service= Executors.newCachedThreadPool();

        try {
            while (true){
                Socket socket = serverSocket.accept();
                service.execute(new ServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }
}
