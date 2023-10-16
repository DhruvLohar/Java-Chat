package testing.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import testing.DataPacket;

public class ClientHandler implements Runnable {
    public static HashMap<String, ArrayList<ClientHandler>> roomToClients = new HashMap<>();
    public static HashMap<String, String> roomTitle = new HashMap<>();
    
    public static final int MAX_CLIENTS_PER_ROOM = 10; // Define your maximum limit
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    DataPacket initalPayload = new DataPacket();
    private String clientUsername;
    private String clientRoomCode;

    public ClientHandler(Socket socket) {
        roomToClients.put("CESSxCODECELL", new ArrayList<>());

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String connectionreq = bufferedReader.readLine();
            DataPacket response = new DataPacket();

            initalPayload.fromString(connectionreq);
            String type = initalPayload.get("type");
            String username = initalPayload.get("username");
            String roomCode = initalPayload.get("roomCode");
            clientUsername = username;
            clientRoomCode = roomCode;

            ArrayList<ClientHandler> clientsInRoom = roomToClients.get(roomCode);
            switch (type) {
                case "create_room":
                    if (clientsInRoom == null) {
                        clientsInRoom = new ArrayList<>();
                        roomToClients.put(roomCode, clientsInRoom);
                    }
                    clientsInRoom.add(this);
                    System.out.println("A room ( " + roomCode + " ) was created by " + username + ".");
                    break;
                case "join_room":
                    // int maxNumOfUsers = Integer.parseInt(initalPayload.get("maxNumOfUsers"));
                    // if (clientsInRoom.size() >= maxNumOfUsers) {
                    //     bufferedWriter.write("SERVER: The room is full. You cannot join.");
                    //     bufferedWriter.newLine();
                    //     bufferedWriter.flush();
                    //     closeEverything(socket, bufferedReader, bufferedWriter);
                    // }
                    clientsInRoom.add(this);

                    response.put("type", "joined_room");
                    response.put("username", username);
                    System.out.println(username + " joined room ( " + roomCode + " ).");
                    broadcastMessage(response.toString(), roomCode);
                    break;
                case "message":
                    broadcastMessage(connectionreq, roomCode);
                    break;
                default:
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String dataFromServer;
        while (socket.isConnected()) {
            try {
                dataFromServer = bufferedReader.readLine();
                broadcastMessage(dataFromServer, clientRoomCode);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend, String room) {
        System.out.println(room);
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
        ArrayList<ClientHandler> clientsInRoom = roomToClients.get(clientRoomCode);
        if (clientsInRoom != null) {
            clientsInRoom.remove(this);
            DataPacket data = new DataPacket();
            data.put("type", "left_room");
            data.put("username", clientUsername);
            broadcastMessage(data.toString(), clientRoomCode);
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
