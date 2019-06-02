package client;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

public class Client {
    private static Socket server;
    private static DataInputStream input;
    private static DataOutputStream output;
    private static BufferedReader systemInput;


    private static void startExploitation() throws IOException {
        while (true) {
            String command = systemInput.readLine();
            if (command.equalsIgnoreCase("stop session"))
                return;
            output.writeUTF(command);
        }
    }

    public static void main(String[] args) {
        try {
            systemInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Input target ip");
            InetAddress serverAddress = InetAddress.getByName(systemInput.readLine());

            Socket server = new Socket(serverAddress, 5555);

            input = new DataInputStream(server.getInputStream());
            output = new DataOutputStream(server.getOutputStream());

            new Thread(new ServerLogReader()).start();


            //Server send a string: do you wanna start exploitation?
            String operation = systemInput.readLine().toLowerCase();
            if (operation.equals("y") || operation.equals("yes")) {
                System.out.println("System input: " + operation);
                output.writeUTF(operation);
                output.flush();
                Thread.sleep(1000);
                startExploitation();
                System.exit(0);
            }

        } catch (Exception ioe) {
            //TODO
        }
    }

    private static class ServerLogReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String s = "";
                    try {
                        s = input.readUTF();
                    } catch (IOException ignore) {
                    }
                    if (!(s.equals("")))
                        System.out.print("\nServer: " + s);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                //TODO
            }
        }
    }
}
