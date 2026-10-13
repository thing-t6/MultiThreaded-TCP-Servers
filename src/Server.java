import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;


class ServerRunner implements Runnable {

    private final ServerSocket serverSocket;
    boolean isConnected = false;

    public ServerRunner(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(120000);
    }

    @Override
    public void run() {
        System.out.println("--- Waiting for client on port :" + serverSocket.getLocalPort() + " ---");
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                isConnected = true;
                new Thread(() -> {
                    try(Socket server = clientSocket;
                        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream(), StandardCharsets.UTF_8));
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(server.getOutputStream(), StandardCharsets.UTF_8), true);) {

                        System.out.println("-> Just connected to" + server.getRemoteSocketAddress());
                        server.setSoTimeout(60000);

                        String data;
                        while(true){
                            if((data = in.readLine()) != null) {
                                System.out.println("-> Client: " + data);
                                out.println("-> Server :Received the message" + server.getRemoteSocketAddress());
                            }
                            else {
                                throw new IOException();
                            }
                        }

                    }catch (SocketTimeoutException e){
                        isConnected = false;
                        System.out.println("--- Client took to long to send a message. Closing connection of: " + clientSocket.getRemoteSocketAddress() +  "---");
                    }
                    catch (IOException e) {
                        isConnected = false;
                        System.out.println("--- Couldn't connect or connection error of :" + clientSocket.getRemoteSocketAddress() +  "---");
                    }
                }).start();
            } catch (SocketTimeoutException e){
                if(isConnected){
                    System.out.println("-- No additional client connected in 2 minutes ---");
                }
                else {
                    System.out.println("-- No client connected in 2 minutes ---");
                    break;
                }
            }
            catch (IOException e) {
                System.out.println();
                System.out.println("--- Couldn't connect or connection error ---");
                break;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server has stopped");
    }
}

public class Server {
    public static void main(String[] args){
        try {
            Thread thread = new Thread(new ServerRunner(8080));
            thread.start();
        }catch (IOException e){
            System.out.println("Could not start server");
        }
    }
}
