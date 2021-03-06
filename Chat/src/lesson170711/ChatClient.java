package lesson170711;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Created by andrew on 13.07.17.
 */
public class ChatClient {

    public static void main(String[] args) {

        ChatClient chat = new ChatClient();


        Scanner keyboardScanner = new Scanner(System.in);

        new Thread( () -> {
            while (keyboardScanner.hasNextLine()){
                String line = keyboardScanner.nextLine();
                chat.sendTextToServer(line);
            }
        }).start();

//        chat.init(new Consumer<String>() {
//            @Override
//            public void accept(String line) {
//                System.out.println(line);
//            }
//        });
        chat.init(System.out::println);

    }

    private PrintWriter writer;

    public void init(Consumer<String> consumer) {

        try{

            Socket socket = new Socket("localhost", 10000);

            Scanner serverScanner = new Scanner(socket.getInputStream());//принимает сообщения от сервера
            writer = new PrintWriter(socket.getOutputStream());//отправляет сообщения на сервер


            new Thread( () -> {
                while (serverScanner.hasNextLine()){
                    String line = serverScanner.nextLine();
                    consumer.accept(line);
                }
            }).start();


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendTextToServer(String line) {
        System.out.println("sending to server: " + line);
        writer.println(line);
        writer.flush();
    }
}
