package testing.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String roomName; // Add room name

    public Client(Socket socket, String username, String roomName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.roomName = roomName; // Initialize room name
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String text) {
        try {
            if (socket.isConnected()) {
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // public void sendMessage() {
    //     try {
    //         bufferedWriter.write(username); // Include room name
    //         bufferedWriter.newLine();
    //         bufferedWriter.flush();

    //         bufferedWriter.write(roomName); // Include room name
    //         bufferedWriter.newLine();
    //         bufferedWriter.flush();

    //         Scanner scanner = new Scanner(System.in);
    //         while (socket.isConnected()) {
    //             String messageToSend = scanner.nextLine();
    //             bufferedWriter.write(username + " (" + roomName + "): " + messageToSend); // Include room name
    //             bufferedWriter.newLine();
    //             bufferedWriter.flush();
    //         }
    //         scanner.close();
    //     } catch (IOException e) {
    //         closeEverything(socket, bufferedReader, bufferedWriter);
    //     }
    // }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat:");
        String username = scanner.nextLine();
        System.out.println("Enter the room name:");
        String roomName = scanner.nextLine(); // Input the room name
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username, roomName); // Pass the room name
        client.listenForMessage();
        client.sendMessage("NAME");        
        client.sendMessage("SEX");


        scanner.close();
    }
}

