import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by cajus on 08/02/17.
 */
public class TD1Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        InetAddress ip = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        String request = args[2].replace("\"", "");

        System.out.println("Connecting to: " + ip.toString() + "@" + port);

        Socket clientSocket = new Socket(ip, port);
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader r = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        w.write(request);
        w.flush();
        String response = r.readLine();

        System.out.println(response);
    }

}
