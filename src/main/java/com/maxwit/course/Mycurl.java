package com.maxwit.course;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mycurl {
    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        String filePath = args[2];
        String requestMethd = "Get";
        String requestProtocol = " HTTP/1.1";
        String requestLine = requestMethd + " " + filePath + " " + requestProtocol;

        try {
            Socket curl = new Socket(serverName, port);

            OutputStream toServer = curl.getOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(toServer);
            BufferedWriter outServer = new BufferedWriter(out);
            outServer.write(requestLine);
            outServer.flush();
            curl.shutdownOutput();

            InputStream fromServer = curl.getInputStream();
            byte[] b = new byte[1024];
            int len = fromServer.read(b);
            String responseHeader = new String(b, 0, len);
            String[] line = responseHeader.split("\n");

            if (!line[0].contains("404")) {
                String pattern = "(<.+>)(\\s+.+)+";
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(responseHeader);
                String theContents = matcher.group();

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
                }

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
