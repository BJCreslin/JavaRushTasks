package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Server - основной класс сервера.
public class Server {
    static private Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    static public void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
            try {
                entry.getValue().send(message);
            } catch (IOException e) {
                try {
                    entry.getValue().send(new Message(MessageType.TEXT, "Сервер не смог отправить сообщение"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    private static class Handler extends Thread {
        Socket socket;

        public void run() {
            String userName = null;
                ConsoleHelper.writeMessage("установлено соединение с удаленным адресом: " + String.valueOf(socket.getRemoteSocketAddress()));
                try (Connection connection = new Connection(socket)) {
                    userName = serverHandshake(connection);
                    sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                    sendListOfUsers(connection, userName);
                    serverMainLoop(connection, userName);
                    socket.close();
                } catch (IOException | ClassNotFoundException e) {
                    ConsoleHelper.writeMessage("произошла ошибка при обмене данными с удаленным адресом");
                }

                if (!(userName==null)) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                    ConsoleHelper.writeMessage("Соединение с удаленным адресом закрыто");
                }
            }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            Message messageReceived;
            Message messageSendForBroadcoast;

            while (true) {
                messageReceived = connection.receive();
                if ((messageReceived != null) && (messageReceived.getType() == MessageType.TEXT)) {
                    String textBroadcoast = userName + ": " + messageReceived.getData();
                    messageSendForBroadcoast = new Message(MessageType.TEXT, textBroadcoast);
                    sendBroadcastMessage(messageSendForBroadcoast);
                } else {
                    ConsoleHelper.writeMessage("Error!");
                }
            }
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException {

            connectionMap.forEach((nameUSerFromMap, connectionFromMap) -> {
                if (!nameUSerFromMap.equals(userName)) {
                    try {
                        Message message = new Message(MessageType.USER_ADDED, nameUSerFromMap);
                        connection.send(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            Message message;

            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                message = connection.receive();
                if ((message.getType().equals(MessageType.USER_NAME))) {
                    String nameFromConnection = message.getData();
                    if ((!nameFromConnection.isEmpty()) &&
                            (nameFromConnection != null) &&
                            (!connectionMap.containsKey(nameFromConnection))) {
                        connectionMap.put(nameFromConnection, connection);
//                        System.out.println(nameFromConnection+" было принято");
                        connection.send(new Message(MessageType.NAME_ACCEPTED));
                        return nameFromConnection;
                    }
                }
            }
        }

        public Handler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public synchronized void start() {

//            while (true) {
//
//            }
        }
    }

    public static void main(String[] args) {
        int portForServer = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(portForServer)) {
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
