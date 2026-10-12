import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        int port = 8080;
        String message;
        try(Scanner scanner = new Scanner(System.in);
            Socket client = new Socket("localhost", port)) {

            DataInputStream in = new DataInputStream(client.getInputStream());
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

            }
        }catch (UnknownHostException ex){
            System.out.println("Couldn't identify host");
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
