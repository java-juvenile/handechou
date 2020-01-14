package com.maxwit.course;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WitCat {
    private ServerSocket serverSocket;
    String theContents = "";
    String responeProtocol = "HTTP/1.1";
    String responseStatusCode = "200";
    String responseStatus = "OK";
    String contentLength = "";
    String responseLine = "";
    String filePath = "";
    Map<String, String> responseMap = new HashMap<>();

    WitCat(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void getFromclient(Socket server) throws IOException {
        InputStreamReader getInfo = new InputStreamReader(server.getInputStream());
        BufferedReader reader = new BufferedReader(getInfo);
        String requestHeader = reader.readLine();
        String[] tolist = requestHeader.split(" ");
        filePath = "." + tolist[1];
    }

    public void readFile(File file) {
        try {
            FileInputStream readfile = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            try {
                len = readfile.read(bytes);
                responseMap.put("Content-Length", Integer.toString(len));
                responseMap.forEach((k, v) -> {contentLength += k + ": " + v + "\n";});
                theContents = new String(bytes, 0, len);
                readfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendToclient(Socket server) {
        responseLine = responeProtocol + " " + responseStatusCode + " " + responseStatus;
        OutputStream toclient;
        try {
            toclient = server.getOutputStream();
            DataOutputStream writeTo = new DataOutputStream(toclient);
            String responseHeader = responseLine + "\n"
                    + contentLength + "\n" + theContents;
            writeTo.writeUTF(responseHeader);
            writeTo.flush();
            writeTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connect() {
        while (true) {
            try {
                Socket server = serverSocket.accept();

                //get file path from client
                getFromclient(server);
                
                //find file at local
                File file = new File("." + filePath);
                if (!file.exists()){
                    responseStatusCode = "404";
                    responseStatus = "Not found";
                    File file404 = new File("404.html");
                    readFile(file404);
                } else {
                    readFile(file);
                }

                //send to client
                sendToclient(server);

                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);

        WitCat witCat = new WitCat(port);
        witCat.connect();
    }


}
