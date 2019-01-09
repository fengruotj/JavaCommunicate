package com.basic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * locate com.basic
 * Created by MasterTj on 2019/1/9.
 */
public class Server {
    public static int port=8999;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        Socket socket = serverSocket.accept();

        BufferedReader bufferedReader;
         BufferedWriter bufferedWriter;
        try {
            bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = bufferedReader.readLine();
            System.out.println(Thread.currentThread().getName()+" 从客户端收到内容: "+line);

            bufferedWriter.write("服务器端传来相应");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
