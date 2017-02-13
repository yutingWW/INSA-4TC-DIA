package insa.lyon.tc.dia.td1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.concurrent.*;

/**
 * Created by cajus on 10/02/17.
 */
public class Server implements Runnable{

    private static final int DEFAULT_PORT = 6969;

    private final ConcurrentHashMap<String, String> database = new ConcurrentHashMap<String, String>();
    private final ServerSocket socket;
    private final ExecutorService executorService;

    public static void main(String args[]){
        try {
            new Server(DEFAULT_PORT);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
        executorService = Executors.newCachedThreadPool();
    }

    public void start(){
        new Thread(this).start();
    }

    public boolean stop(){

        if (executorService.isTerminated()) return true;

        try {

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            System.out.println("WARN | Thread was Interrupted");
        }

        return true;
    }

    @Override
    public void run() {
        while (!executorService.isShutdown()){
            try {
                executorService.submit(new ClientRequestHandler(socket.accept(), this));
            } catch (IOException e) {
                System.out.println("ERROR | Connection to client failed");
                e.printStackTrace();
            }
        }
    }

    Enumeration<String> getKeys(){
        return database.keys();
    }

    String putData(String key, String value) {
        return database.put(key, value);
    }

    String delData(String key){
        return database.remove(key);
    }

    String getData(String key){
        return database.get(key);
    }

}
