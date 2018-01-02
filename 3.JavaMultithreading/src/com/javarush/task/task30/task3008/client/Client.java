package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.javarush.task.task30.task3008.ConsoleHelper.readString;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        String lineFromClient = "";
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("во время ожидания возникло исключение");
                return;
            }
        }
        if (clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        } else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }
        while (clientConnected) {
            lineFromClient = ConsoleHelper.readString();
            if (lineFromClient.equals("exit")) {
                break;
            }
            if (shouldSendTextFromConsole()) {
                sendTextMessage(lineFromClient);
            }
        }
    }

    public class SocketThread extends Thread {

        public void run() {
//           1) Запроси адрес и порт сервера с помощью методов getServerAddress() и getServerPort().
//           2) Создай новый объект класса java.net.Socket, используя данные, полученные в предыдущем пункте.
//           3) Создай объект класса Connection, используя сокет из п.17.2.
//           4) Вызови метод, реализующий "рукопожатие" клиента с сервером (clientHandshake()).
//           5) Вызови метод, реализующий основной цикл обработки сообщений сервера.
//           6) При возникновении исключений IOException или ClassNotFoundException сообщи главному потоку о проблеме, используя notifyConnectionStatusChanged и false в качестве параметра.
            String serverAddres = getServerAddress();
            int serverPort = getServerPort();
            try (Socket socketNew = new Socket(serverAddres, serverPort);
                 Connection connectionNew = new Connection(socketNew)) {
                connection=connectionNew;
                clientHandshake();
                clientMainLoop();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            } catch (ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);

            }

        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
//                . Этот метод будет представлять клиента серверу.
//        Он должен:
//        а) В цикле получать сообщения, используя соединение connection.
//        б) Если тип полученного сообщения NAME_REQUEST (сервер запросил имя), запросить ввод имени пользователя с помощью метода getUserName(),
// создать новое сообщение с типом MessageType.USER_NAME и введенным именем, отправить сообщение серверу.
//        в) Если тип полученного сообщения MessageType.NAME_ACCEPTED (сервер принял имя), значит сервер принял имя клиента,
// нужно об этом сообщить главному потоку, он этого очень ждет. Сделай это с помощью метода notifyConnectionStatusChanged(), передав в него true. После этого выйди из метода.
//                г) Если пришло сообщение с каким-либо другим типом, кинь исключение IOException("Unexpected MessageType").

            Message messageReceived;
            while (true) {
                messageReceived = connection.receive();
                if (messageReceived.getType() == MessageType.NAME_REQUEST) {
                    String userNameFromKeyboard = getUserName();
                    Message message = new Message(MessageType.USER_NAME, userNameFromKeyboard);
                    connection.send(message);
                } else if (messageReceived.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    return;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
//         Этот метод будет реализовывать главный цикл обработки сообщений сервера. Внутри метода:
//        а) Получи сообщение от сервера, используя соединение connection.
//        б) Если это текстовое сообщение (тип MessageType.TEXT), обработай его с помощью метода processIncomingMessage().
//        в) Если это сообщение с типом MessageType.USER_ADDED, обработай его с помощью метода informAboutAddingNewUser().
//        г) Если это сообщение с типом MessageType.USER_REMOVED, обработай его с помощью метода informAboutDeletingNewUser().
//        д) Если клиент получил сообщение какого-либо другого типа, брось исключение IOException("Unexpected MessageType").
//        е) Размести код из предыдущих пунктов внутри бесконечного цикла. Цикл будет завершен автоматически если произойдет
// ошибка (будет брошено исключение) или поток, в котором работает цикл, будет прерван.

            Message messageFromServer;

            while (true) {
                messageFromServer = connection.receive();
                if (messageFromServer.getType() == MessageType.TEXT)
                    processIncomingMessage(messageFromServer.getData());
                else if (messageFromServer.getType() == MessageType.USER_ADDED)
                    informAboutAddingNewUser(messageFromServer.getData());
                else if (messageFromServer.getType() == MessageType.USER_REMOVED)
                    informAboutDeletingNewUser(messageFromServer.getData());
                else throw new IOException("Unexpected MessageType");
            }
        }


        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " подключился к чату");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " покинул чат");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            synchronized (Client.this) {
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }


    }

    protected String getServerAddress() {
//        1. String getServerAddress() - должен запросить ввод адреса сервера у пользователя и вернуть введенное значение.
//        Адрес может быть строкой, содержащей ip, если клиент и сервер запущен на разных машинах или 'localhost', если клиент и сервер работают на одной машине.
        String adressForServer = ConsoleHelper.readString();
        return adressForServer;
    }

    protected int getServerPort() {
        int portServer = ConsoleHelper.readInt();
        return portServer;
    }

    protected String getUserName() {
        String userNAme = ConsoleHelper.readString();
        return userNAme;
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() { //- должен создавать и возвращать новый объект класса SocketThread
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("во время отправки или создания сообщения возникло исключение IOException");
            clientConnected = false;

        }

    }
}
