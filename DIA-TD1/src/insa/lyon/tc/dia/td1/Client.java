package insa.lyon.tc.dia.td1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Created by cajus on 12/02/17.
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        InetAddress ip = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        System.out.println("Connecting to: " + ip.toString() + "@" + port);

        Socket clientSocket = new Socket(ip, port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

        while (clientSocket.isConnected()){

            String userInput = userInputReader.readLine() + "\n";

            if (userInput.startsWith("EXIT")) {
                clientSocket.close();
                return;
            } else if (userInput.startsWith("PUT")) {
                userInput += userInputReader.readLine();
            }

            writer.write(userInput);
            writer.flush();

            sleep(1000);

            String output = "";
            while (reader.ready()){
                output += (char) reader.read();
            }
            System.out.println(output);

        }

    }

}
