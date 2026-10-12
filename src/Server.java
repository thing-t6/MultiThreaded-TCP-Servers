import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


class ServerRunner implements Runnable {

    private ServerSocket serverSocket;

    public ServerRunner(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(30000);
    }

    @Override
    public void run() {
        while(true){
            System.out.println("--- Waiting for client on port :" + serverSocket.getLocalPort() + " ---");
            try(Socket server = serverSocket.accept()) {
                System.out.println("-> Just connected to" + server.getRemoteSocketAddress());
                server.setSoTimeout(30000);
                try{
                    DataInputStream inputStream = new DataInputStream(new BufferedInputStream(server.getInputStream()));
                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
                    while(true) {
                        String message = inputStream.readUTF();
                        System.out.println("-> Client: " + message);
                        out.writeUTF("-> Server :Received the message" + server.getRemoteSocketAddress());
                    }
                }catch (SocketTimeoutException e){
                    System.out.println("--- Client took to long to send a message. Closing connection ---");
                }
            } catch (SocketTimeoutException e){
                System.out.println("-- No client connected in 30 seconds ---");
                break;
            } catch (IOException e) {
                System.out.println();
                System.out.println("--- Couldn't disconnected or connection error ---");
            }
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
