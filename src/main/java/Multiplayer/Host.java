package Multiplayer;

import model.Unit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Host {
    String ip = "10.112.195.107";
    int port = 1111;
    DataInputStream in;
    //DataOutputStream out;
    ObjectOutputStream out;

    public Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Host (){}

    public void connect() {
        try {
            Socket s = new Socket(ip, port);
            //in = new DataInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
            //out = new DataOutputStream(s.getOutputStream());

            System.out.println("Connected");
        } catch (IOException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Not connected");
        }
        //streamOut();
    }

    public void streamOut(Data data) {
        //Scanner sc = new Scanner(System.in);


        Boolean stop = false;
        while (!stop) {

            try {

                //int i = sc.nextInt();

                out.writeObject(data);
                //out.writeInt(i);
                out.flush();

            } catch (IOException ex) {
                Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
