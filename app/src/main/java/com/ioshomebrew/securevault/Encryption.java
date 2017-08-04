package com.ioshomebrew.securevault;

/**
 * Created by ioshomebrew on 5/16/17.
 */

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Encryption {
    // Encrypt file by taking existing file, then overwriting it with an the encrypted content
    public static void encryptData(String pass, File file) throws org.jasypt.exceptions.EncryptionOperationNotPossibleException
    {
        // create basictextencryptor
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(pass);

        // buffer file into string
        String fileData = "";

        // buffer file into string
        try {
            FileReader fr = new FileReader(file);
            BufferedReader in = new BufferedReader(fr);

            // decrypt password
            String data;
            while ((data = in.readLine()) != null) {
                fileData += data;
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // encrypt file data
        try
        {
            String encryptedData = textEncryptor.encrypt(fileData);
            // write vault pass
            PrintWriter writer=null;

            try {
                FileOutputStream os = new FileOutputStream(file);
                writer=new PrintWriter(os);
            } catch (IOException e) {
                // log exception
                e.printStackTrace();
            }
            writer.print(encryptedData);
            writer.flush();
            writer.close();
        } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e) {
            throw e;
        }
    }

    // Decrypt file by taking existing file, then overwriting it with an the encrypted content
    public static void decryptData(String pass, File file) throws org.jasypt.exceptions.EncryptionOperationNotPossibleException {
        // create basictextencryptor
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(pass);

        // buffer file into string
        String fileData = "";

        // buffer file into string
        try {
            FileReader fr = new FileReader(file);
            BufferedReader in = new BufferedReader(fr);

            // decrypt password
            String data;
            while ((data = in.readLine()) != null) {
                fileData += data;
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // decrypt file data
        try {
            String decryptedData = textEncryptor.decrypt(fileData);

            // write to file
            PrintWriter writer=null;

            try {
                FileOutputStream os = new FileOutputStream(file);
                writer=new PrintWriter(os);
            } catch (Exception e) {
                // log exception
                e.printStackTrace();
            }
            writer.print(decryptedData);
            writer.flush();
            writer.close();
        }
        catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e) {
            throw e;
        }
    }
}
