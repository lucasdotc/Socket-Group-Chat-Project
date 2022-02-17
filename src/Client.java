import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedReader inFromServer;
    private PrintWriter outToServer;
    private static final String IP = "10.0.0.193";
    public static String clientName;


    public static void main(String[]args) throws IOException{
        setClientName();
        new Client().startClient();
    }
    public static void setClientName(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        clientName = sc.nextLine();
    }

    public String getClientName(){
        return this.clientName;
    }


    public void startClient() throws IOException{
        clientSocket = new Socket(IP,8081);
        outToServer = new PrintWriter(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Scanner sc = new Scanner(System.in);
        Thread sender = new Thread(new Runnable() {
            String text;
            @Override
            public void run() {
                    while(!"STOP".equals(text)){
                        text = sc.nextLine();
                        outToServer.write(clientName+":"+text);
                        outToServer.flush();
                }

            }
        });
        sender.start();

        Thread receiver = new Thread(new Runnable() {
            String text;
            @Override
            public void run() {
                try {
                    text = inFromServer.readLine();
                    while (!"STOP".equals(text)){
                        if (text == null){
                            continue;
                        }
                        System.out.println(clientName+":"+ text);
                        text = inFromServer.readLine();
                    }System.out.println(clientName+" has left the chat!");

                    clientSocket.close();
                    outToServer.close();
                    inFromServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        receiver .start();
    }


}
