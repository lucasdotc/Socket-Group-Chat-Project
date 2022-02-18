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
        announce(clientName,"[SERVER]: "+ clientName+" has joined the chat!");
        closeAll(socket,out,in);
    }
    public void announce(String clientName, String message)throws IOException{
        for (ClientHandler clientHandler: clientHandlers){
            clientHandler.out.write(clientName + message);
            clientHandler.out.flush();
        }closeAll(socket,out,in);
    }
    public void removeClient() throws IOException{
        clientHandlers.remove(this);
        announce(clientName, "[SERVER]:"+ clientName +" has left the chat.");

        closeAll(socket,out,in);
    }

    public void closeAll(Socket socket, PrintWriter out, BufferedReader in) throws IOException{
        removeClient();
        if (socket != null){
            socket.close();

        }
        if (out != null){
            out.close();
        }
        if (in != null){
            in.close();
        }
    }


    @Override
    public void run() {
        String textFromClient;
        while (socket.isConnected()){
            try{
                textFromClient = in.readLine();
                announce(clientName, textFromClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
