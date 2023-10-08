package testing.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandler implements Runnable {
    public static HashMap<String, ArrayList<ClientHandler>> roomToClients = new HashMap<>();
    public static final int MAX_CLIENTS_PER_ROOM = 10; // Define your maximum limit
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private String clientRoom;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientRoom = bufferedReader.readLine(); // Read the room name from the client
            System.out.print(clientUsername + " " + clientRoom);

            ArrayList<ClientHandler> clientsInRoom = roomToClients.get(clientRoom);

            if (clientsInRoom == null) {
                clientsInRoom = new ArrayList<>();
                roomToClients.put(clientRoom, clientsInRoom);
            }

            if (clientsInRoom.size() >= MAX_CLIENTS_PER_ROOM) {
                // If the room is full, you can choose to handle this situation
                // e.g., send a message to the client that the room is full and close the
                // connection
                bufferedWriter.write("SERVER: The room is full. You cannot join.");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                closeEverything(socket, bufferedReader, bufferedWriter);
            } else {
                clientsInRoom.add(this);
                broadcastMessage("SERVER: " + clientUsername + " has entered the chat!", clientRoom);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient, clientRoom);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend, String room) {
        ArrayList<ClientHandler> clientsInRoom = roomToClients.get(room);
        if (clientsInRoom == null) {
            return;
        }

        for (ClientHandler clientHandler : clientsInRoom) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        ArrayList<ClientHandler> clientsInRoom = roomToClients.get(clientRoom);
        if (clientsInRoom != null) {
            clientsInRoom.remove(this);
            broadcastMessage("SERVER: " + clientUsername + " has left the chat!", clientRoom);
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}

