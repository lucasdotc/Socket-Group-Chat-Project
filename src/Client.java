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
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        clientName = sc.nextLine();
        new Client().startClient(clientName);
    }

    public void startClient(String clientName) throws IOException{
        clientSocket = new Socket(IP,8081);
        outToServer = new PrintWriter(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("Welcome, "+clientName+"!");
        receiveMessages();
        Scanner sc = new Scanner(System.in);
        String messageToSend = "";
        while (!"STOP".equals(messageToSend)){
            messageToSend = sc.nextLine();
            sendAMessage(clientName, messageToSend);
            System.out.println("["+clientName+"]: "+messageToSend);

        }
        clientSocket.close();
        outToServer.close();
        inFromServer.close();
    }

    public void sendAMessage(String clientName, String messageToSend) throws IOException{
        outToServer.write(clientName);
        outToServer.flush();

        while(clientSocket.isConnected()){
            outToServer.write("["+clientName+"]"+messageToSend);
            outToServer.flush();
        }
    }

    public void receiveMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;
                while (clientSocket.isConnected()){
                    try {
                        messageFromChat = inFromServer.readLine();
                        System.out.println(messageFromChat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, PrintWriter in, BufferedReader out){

    }
}
