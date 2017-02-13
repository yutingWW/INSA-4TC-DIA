package insa.lyon.tc.dia.td1;

import java.io.*;
import java.net.Socket;
import java.util.Enumeration;

/**
 * Created by cajus on 12/02/17.
 */
public class ClientRequestHandler implements Runnable {

    Socket clientSocket;
    Server server;
    BufferedReader reader;
    BufferedWriter writer;


    public ClientRequestHandler(Socket clientSocket, Server server){

        this.clientSocket = clientSocket;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {

                String command = reader.readLine();
                String[] parsedCommand = command.split(" ");

                if (command.startsWith("KEYS")) {

                    Enumeration<String> keys = server.getKeys();

                    while (keys.hasMoreElements()) {
                        writer.write(keys.nextElement() + "\n");
                    }
                    writer.write("--------------------------\n");

                } else if (command.startsWith("PUT")) {

                    int datalength = Integer.parseInt(parsedCommand[2]);
                    String put_data = "";
                    for (int i = 0; i<datalength; i++) {
                        put_data += (char) reader.read();
                    }

                    System.out.println("DATA: " + put_data);

                    server.putData(parsedCommand[1], put_data);
                    writer.write("OK\n");

                } else if (command.startsWith("DEL")) {

                    server.delData(parsedCommand[1]);
                    writer.write("OK\n");

                } else if (command.startsWith("GET")) {

                    String _data = server.getData(parsedCommand[1]);
                    String _length = Integer.toString(_data.length());

                    writer.write(_length + "\n");
                    writer.write(_data + "\n");

                } else {
                    writer.write("Please enter a valid command.\n");
                }

                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
