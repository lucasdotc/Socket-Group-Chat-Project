import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket serverSocket;
    private Socket clientSocket;

    public static void main(String[]args) throws IOException {
        new Server().startServer();
    }
    public void startServer() throws IOException{
        System.out.println("Start server socket BEGIN");
        serverSocket = new ServerSocket(8081);
        System.out.println("Waiting for client to connect...");
        clientSocket = serverSocket.accept();
        System.out.println("A new client has joined the chat!" );
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        Thread thread = new Thread(clientHandler);
        thread.start();

    }
}
