/**
 * Created by ywang on 08/02/17.
 */

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.*;


public class TD1Server {

    private static ConcurrentHashMap<String, String> database = new ConcurrentHashMap<String, String>();

    public TD1Server() throws UnknownHostException {
    }

    public static void main(String args[]) throws IOException {

        final int DEFAULT_PORT = 6969;
        final InetAddress DEFAULT_IP = InetAddress.getByName("localhost");

         ExecutorService executor = Executors.newCachedThreadPool();

         ServerSocket serverSocket = new ServerSocket();
         serverSocket.bind(new InetSocketAddress(DEFAULT_IP, DEFAULT_PORT), 100000);

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
                         System.out.println("received keys");

                         Enumeration<String> keys = database.keys();
                         while (keys.hasMoreElements()) {
                             w.write(keys.nextElement() + "\n");
                         }
                         w.write("--------------------------\n");

                     } else if (command.startsWith("PUT")) {
                         try {
                             String[] put_command = command.split(" ");
                             int datalength = Integer.parseInt(put_command[2]);
                             System.out.println("Datalength: " + datalength);
                             String put_data = "";
                             for (int i = 0; i<datalength; i++) {
                                 put_data += (char) r.read();
                             }
                             System.out.println("DATA: " + put_data);

                             database.put(put_command[1], put_data);
                             w.write("OK\n");
                         } catch (ArrayIndexOutOfBoundsException e){
                            w.write("ERROR | PUT COMMAND MALFORMED\n");
                         }
                     } else if (command.startsWith("DEL")) {
                        try {
                            String[] del_command = command.split(" ");
                            database.remove(del_command[1]);
                            w.write("OK\n");
                        } catch (Exception e) {
                            w.write("ERROR | DEL COMMAND MALFORMED\n");
                        }
                     } else if (command.startsWith("GET")) {

                         try {
                             String[] get_command = command.split(" ");
                             String get_returndata = database.get(get_command[1]);
                             String get_returnlength = Integer.toString(get_returndata.length());

                             w.write(get_returnlength + "\n");
                             w.write(get_returndata + "\n");
                         } catch (Exception e) {
                             w.write("ERROR | GET COMMAND MALFORMED\n");
                         }
                     }

                     w.flush();
                 } catch (Exception ioe){
                     throw new RuntimeException(ioe);
                 }

             };

             executor.submit(task);
         }
     }

}
