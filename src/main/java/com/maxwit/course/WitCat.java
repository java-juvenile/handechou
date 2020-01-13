package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WitCat {
    private ServerSocket serverSocket;

    WitCat(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void connect() {
        String theContents = "";
        while (true) {
            try {
                Socket server = serverSocket.accept();

                //get file path from client
                DataInputStream getInfo = new DataInputStream(server.getInputStream());
                String filePath = getInfo.readUTF();
                //find file at local
                File file = new File("." + filePath);
                if (!file.exists()){
                    System.exit(1);
                } else {
                    InputStream readFile = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = readFile.read(bytes);
                    theContents = new String(bytes, 0, len);
                }

                //send to client
                OutputStream toclient = server.getOutputStream();
                DataOutputStream writeTo = new DataOutputStream(toclient);
                writeTo.writeUTF(theContents);

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
