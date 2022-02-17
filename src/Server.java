import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {
    private static ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    public Scanner sc = new Scanner(System.in);

    public static void main(String[]args) throws IOException{
        serverSocket = new ServerSocket(8081);
        for (int i=0;i<3;i++){
            new Thread(()->{
                try {
                    new Server().startServer(serverSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
    public void startServer(ServerSocket serverSocket) throws IOException{
        System.out.println("Start server socket BEGIN");
        System.out.println("Waiting for client to connect...");
        clientSocket = serverSocket.accept();
        Client name = new Client();
        String clientName = name.getClientName();
        System.out.println(clientName+ " has joined the chat!" );
        outToClient = new PrintWriter(clientSocket.getOutputStream());
        inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread sender = new Thread(new Runnable() {
            String msg = inFromClient.readLine();
            @Override
            public void run() {
                while(!"STOP".equals(msg)){
                    msg = sc.nextLine();
                    outToClient.println(msg);
                    outToClient.flush();
                }

            }
        });
        sender.start();


        Thread receiver = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                try {
                    msg = inFromClient.readLine();
                    while (!"STOP".equals(msg)){
                        if (msg == null){
                            continue;
                        }
                        System.out.println(clientName+":"+ msg);
                        msg = inFromClient.readLine();
                    }System.out.println();

                    serverSocket.close();
                    clientSocket.close();
                    inFromClient.close();
                    outToClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        receiver.start();

    }
}
