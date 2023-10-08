package testing.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import testing.DataPacket;

public class Client {
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dataFromServer;
                while (socket.isConnected()) {
                    try {
                        dataFromServer = bufferedReader.readLine();
                        DataPacket payload = new DataPacket();
                        payload.fromString(dataFromServer);
                        String name = payload.get("username");

                        switch (payload.get("type")) {
                            case "joined_room":
                                System.out.println("[JOINED ROOM] " + name);
                                break;
                            case "left_room":
                                System.out.println("[LEFT ROOM] " + name);
                                break;
                            case "message":
                                System.out.println("[" + name + " SAYS]" + payload.get("message"));
                                break;
                            default:
                                closeEverything(socket, bufferedReader, bufferedWriter);
                                break;
                        }
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
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket); // Pass the room name
        String username;
        String roomName;
        DataPacket data = new DataPacket();

        int choice;
        System.out.println("Create or Join? (1 / 2) : ");
        choice = scanner.nextInt();

        System.out.println("Enter your username for the group chat:");
        username = scanner.nextLine();

        System.out.println("Enter the room name:");
        roomName = scanner.nextLine();

        switch (choice) {
            case 1:
                data.put("type", "create_room");
                data.put("username", username);
                data.put("roomName", roomName);
                // data.put("roomTitle", "Title");
                // data.put("roomDesc", "Desc");
                data.put("maxNumOfUsers", "5");

                break;
            case 2:
                data.put("type", "join_room");
                data.put("username", username);
                data.put("roomCode", roomName);

                break;
        }

        client.sendMessage(data.toString());
        client.listenForMessage();

        scanner.close();
    }
}
