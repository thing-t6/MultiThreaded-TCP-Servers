import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        int port = 8888;
        String message;
        try(Scanner scanner = new Scanner(System.in);
            Socket client = new Socket("localhost", port)) {

            DataInputStream in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("-> Just connected to " + client.getRemoteSocketAddress());
            while(true){
                System.out.print("-> ");
                message = scanner.nextLine();
                if(message.equalsIgnoreCase("exit")){
                    System.out.println("Closing connection...");
                    break;
                }
                out.writeUTF(message +" from " + client.getLocalAddress());
                System.out.println("-> Server: " + in.readUTF());
            }
        }catch (UnknownHostException ex){
            System.out.println("Couldn't identify host");
        }catch (IOException ex){
            System.out.println("Error Couldn't connect with the Server");
        }
    }
}
