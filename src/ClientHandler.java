import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler>clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientName = in.readLine();
        clientHandlers.add(this);
    }
    public void announce(String clientName, String message)throws IOException{
        for (ClientHandler clientHandler: clientHandlers){
            clientHandler.out.write(clientName + message);
            clientHandler.out.flush();
        }
    }
    public void removeClient() throws IOException{
        clientHandlers.remove(this);
        announce(clientName, "[SERVER]:"+ clientName +" has left the chat.");
    }

    public void closeAll(Socket socket, PrintWriter out, BufferedReader in) throws IOException{
        removeClient();

    }


    @Override
    public void run() {

    }
}
