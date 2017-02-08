/**
 * Created by ywang on 08/02/17.
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class TD1Server {

     private static ConcurrentHashMap<String, String> database;

     public static void main(String args[]) throws IOException {

         database = new ConcurrentHashMap<String, String>();

         final InetAddress ip = InetAddress.getByName("localhost");
         final int port = 6969;

         ExecutorService executor = Executors.newCachedThreadPool();

         ServerSocket serverSocket = new ServerSocket();
         serverSocket.bind(new InetSocketAddress(ip, port), 100000);

         while (true){

             Socket clientSocket = serverSocket.accept();


             Runnable task = () -> {

                 try {

                     BufferedReader r = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream())
                     );
                     BufferedWriter w = new BufferedWriter(
                             new OutputStreamWriter(clientSocket.getOutputStream())
                     );

                     String command = r.readLine();

                     if (command.startsWith("KEYS")) {
                         database.keySet().forEach((k) -> {
                             try {
                                 w.write(k + "\n");
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         });
                     } else if (command.startsWith("PUT")) {

                     } else if (command.startsWith("DEL")) {

                     } else if (command.startsWith("GET")) {

                     }

                 } catch (Exception ioe){
                     throw new RuntimeException(ioe);
                 }

             };

             executor.submit(task);
         }
     }

}
