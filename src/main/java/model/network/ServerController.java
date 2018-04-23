package model.network;

import model.units.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController extends Thread {
    ServerSocket server;
    Socket connectionToTheClient;

    List<Player> players = new ArrayList();

    private boolean hasClient = false;

    public ServerController(){
        {
            try {
                server = new ServerSocket(8888);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(){
        System.out.println("thread run...");
        while(!hasClient){
            try {
                Thread.sleep(1000);
                System.out.println("hyväksytään client jos sellainen on...");
                connectionToTheClient = server.accept();

                if(connectionToTheClient != null){
                    InputStream in = connectionToTheClient.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line = br.readLine();
                    System.out.println(line); // Prints "Hello, Other side of the connection!", in this example (if this would be the other side of the connection.
                }



            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }


        }
    }

}
