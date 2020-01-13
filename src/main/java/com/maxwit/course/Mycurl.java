package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Mycurl {
    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        String filePath = args[2];
        String requestMethd = "Get";
        String requestProtocol = " HTTP/1.1";
        String requestHeader = requestMethd + " " + filePath + " " + requestProtocol;

        try {
            Socket curl = new Socket(serverName, port);            
            OutputStream toServer = curl.getOutputStream();
            DataOutputStream out = new DataOutputStream(toServer);
            out.writeUTF(requestHeader);

            InputStream fromServer = curl.getInputStream();
            DataInputStream getInfo = new DataInputStream(fromServer);
            String responseHeader = getInfo.readUTF();
            String[] headerList = responseHeader.split(" ");
            String theContents = headerList[headerList.length - 1];
            
            String[] list = filePath.split("/");
            String filename = list[list.length - 1];
            File downDir = new File("WitDownloads");
            if (!downDir.exists()) {
                downDir.mkdirs();
            }
            String downfile = downDir + "/" + filename; 

            File downloadFile = new File(downfile);
            if (!downloadFile.exists()){
                downloadFile.createNewFile();
                FileOutputStream toDown = new FileOutputStream(downloadFile);
            
                toDown.write(theContents.getBytes());
                toDown.close();
            }

            curl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
