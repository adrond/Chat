import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    final int serverPort = 1234;

    ArrayList<Client> clients = new ArrayList<>();
    ServerSocket serverSocket;

    ChatServer() {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String GetUsersList(String name) {
        String ret = "You - " + name;
        for (Client client : clients) {
            if (!client.name.equals(name)) {
                ret += ", " + client.name;
            }
        }
        return ret;

    }

    void sendPrivate(String name, String message) {
        for (Client client : clients) {
            if (client.name.equals(name)) {
                client.receive(name + " private writing to you: " + message);
            }
        }

    }

    boolean checkUserName(String name) {
        for (Client client : clients) {
            if (client.name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    void sendAll(String name, String message) {
        for (Client client : clients) {
            client.receive(name + " writing: " + message);

        }
    }

    public void Run() throws IOException {
        while (true) {
            System.out.println("Waiting...");
            // ждем клиента из сети
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            // создаем клиента на своей стороне
            clients.add(new Client(socket, this));
        }

    }

    public static void main(String[] args) throws IOException {
        new ChatServer().Run();

    }
}