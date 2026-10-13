import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

class ProxyWorker implements Runnable{
    private final Socket clientSocket;
    private final String targetHost;
    private final int targetPort;

    public ProxyWorker(Socket clientSocket, String targetHost, int targetPort){
        this.clientSocket = clientSocket;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    @Override
    public void run() {
        try(Socket targetSocket = new Socket(targetHost, targetPort)){
            System.out.println("-> Connected to taget server for session");
            Thread clientToTarget = new Thread(() -> pipe(clientSocket, targetSocket));
            Thread targetToClient = new Thread(() -> pipe(targetSocket, clientSocket));

            clientToTarget.start();
            targetToClient.start();

            clientToTarget.join();
            targetToClient.join();

        }
        catch (Exception e){
            System.out.println("Session closed or error : " + e.getMessage());
        }finally {
            try{
                clientSocket.close();
            }catch (Exception e){
                System.out.println("Client session terminated");
            }
        }
    }


    private void pipe(Socket from, Socket to) {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(from.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(to.getOutputStream(), StandardCharsets.UTF_8), true);
            String data;

            while((data = in.readLine()) != null){
                out.println(data);
            }
            System.out.println(from.getRemoteSocketAddress() + " Disconnected");
        }
        catch (IOException e){
            System.out.println("Connection error: " + e.getMessage());
        }finally {
            try {
                from.close();
            } catch (IOException e) {}

            try {
                to.close();
            } catch (IOException e) {}
        }
    }
}


public class TcpProxy {
    public static void main(String[] args) {

        int listenPort = -1;
        String targetHost = null;
        int targetPort = -1;
        String origin = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--port":
                    if (i + 1 < args.length) {
                        listenPort = Integer.parseInt(args[i + 1]);
                        i++;
                    }
                    break;
                case "--origin":
                    if (i + 1 < args.length) {
                        origin = args[i + 1];
                        i++;
                    }
                    break;
            }
        }

        if (listenPort == -1 || origin == null) {
            System.out.println("Usage: java CachingProxy --port <number> --origin <url>");
            return;
        }

        URI uri = URI.create(origin);

        targetHost = uri.getHost();
        targetPort = uri.getPort();

        System.out.println("Starting proxy on port:" + listenPort + " with origin " + origin);


        System.out.println("--- Proxy started on port:" + listenPort + "\n" + "-> Forwarding to " +
                targetHost + ":" + targetPort + " ---");

        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            serverSocket.setSoTimeout(60000);
            while (true) {
                try {
                    Socket clientsocket = serverSocket.accept();
                    System.out.println("-> Client connected: " + clientsocket.getRemoteSocketAddress());
                    new Thread(new ProxyWorker(clientsocket, targetHost, targetPort)).start();

//                    #Used this to check multithreading
//                    new Thread(() -> {
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        System.out.println("Client connected on thread: " + Thread.currentThread().getName());
//                        try {
//                            PrintWriter out = new PrintWriter(clientsocket.getOutputStream(), true);
//                            out.print("HTTP/1.1 200 OK\r\n");
//                            out.print("Content-Type: text/plain\r\n");
//                            out.print("Content-Length: 17\r\n");
//                            out.print("\r\n");
//                            out.print("Hello from proxy!");
//                            out.flush();
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }).start();
                } catch (SocketTimeoutException e) {
                    System.out.println("No client connected in 60 seconds ! Closing proxy server connection....");
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Proxy server error: " + ex.getMessage());
        }
    }
}
