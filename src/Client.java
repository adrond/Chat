import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    // получаем потоки ввода и вывода
    InputStream is;
    OutputStream os;
    Scanner in;
    PrintStream out;
    ChatServer server;
    String name;

    public Client(Socket socket, ChatServer server){

        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread(this).start();

    }

    void receive (String message){
        out.println (message);
    }
    public void run() {
        try {
            // получаем потоки ввода и вывода
            is = socket.getInputStream();
            os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat!\n Enter you name");
            String input = in.nextLine();
            name = input;
            out.println("Hi " + name + ". I'm glad to see you in our wonderful chat");
            out.println("You can use the following command");
            out.println("list - list of all participants");
            out.println("/<name>: - private message to selected user ");
            out.println("bye - leave this chat ");
            input = in.nextLine();
            while (!input.equals("bye")) {
                if ((input.length()>3)&&input.substring(0,4).equals("list")){
                    out.println(server.GetUsersList(name));
                } else if (input.substring(0,1).equals("/")) {
                    String[] splited = input.substring(1).split("\\s+");
                    if (server.checkUserName(splited[0])){
                        server.sendPrivate(splited[0], input.substring(splited[0].length()+1));
                    }
                    else {out.println("User - " + splited[0] + " not found.");}
                } else {
                server.sendAll(name, input);}

                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}