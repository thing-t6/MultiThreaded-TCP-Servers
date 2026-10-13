import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        int port = 8888;
        String message;
        try(Scanner scanner = new Scanner(System.in);
            Socket client = new Socket("localhost", port)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
            System.out.println("-> Just connected to " + client.getRemoteSocketAddress());
            while(true) {
                System.out.print("-> ");
                message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Closing connection...");
                    break;
                }
                out.println(message + " from " + client.getLocalAddress());
                String data;
                if((data = in.readLine()) != null){
                    System.out.println(data);
                }
                else {
                    throw new IOException();
                }
            }
        }catch (UnknownHostException ex){
            System.out.println("Couldn't identify host");
        }catch (IOException ex){
            System.out.println("Error Couldn't connect with the Server");
        }
    }
}
