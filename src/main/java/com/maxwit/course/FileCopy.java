package com.maxwit.course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./test.txt");
        File tofile = new File("./towrite.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
                byte[] initString = "this is init by myself".getBytes();
                OutputStream toinit = new FileOutputStream(file);
                toinit.write(initString);
                toinit.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream ff = new FileInputStream(file);
        OutputStream tow = new FileOutputStream(tofile);

        try {
            byte[] b = new byte[1024];
            int len = ff.read(b);
            String getContent = new String(b, 0, len);
            System.out.println("The contents of test file is : \n" + "\'" + getContent + "\'");
            System.out.println("copying the contents of test.txt\n");
            byte[] bw = getContent.getBytes();
            tow.write(bw);

            ff.close();
            tow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
       
    }
}
