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
        Client client = new Client(clientName);
        client.receiveMessages();
    }

    public Client(String clientName) throws IOException{
        clientSocket = new Socket(IP,8081);
        outToServer = new PrintWriter(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("Welcome, "+clientName+"!");
        sendAMessage(clientName);
    }

    public void sendAMessage(String clientName){
        Scanner sc = new Scanner(System.in);
        try {
            outToServer.write(clientName);
            outToServer.flush();
            String messageToSend = sc.nextLine();
            while (!"STOP".equals(messageToSend)) {
                System.out.println("[" + clientName + "]: " + messageToSend);
                outToServer.write("[" + clientName + "]: " + messageToSend);
                outToServer.flush();
                messageToSend = sc.nextLine();
            }
        } catch (Exception e) {
            closeEverything(clientSocket, outToServer, inFromServer);
        }
    }

    public void receiveMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;
                while (clientSocket.isConnected()) {
                    try {
                        messageFromChat = inFromServer.readLine();
                        System.out.println(messageFromChat);
                    } catch (IOException e) {
                        closeEverything(clientSocket, outToServer, inFromServer);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, PrintWriter out, BufferedReader in){
        try {
            if (socket!=null){
                socket.close();
            }
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
