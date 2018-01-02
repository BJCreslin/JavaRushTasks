package com.javarush.task.task32.task3208;


import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/* 
RMI-2
*/
public class Solution {
    public static Registry registry;

    //pretend we start rmi client as CLIENT_THREAD thread
    public static Thread CLIENT_THREAD = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                for (String bindingName : registry.list()) {
                    Animal service = (Animal) registry.lookup(bindingName);
                    service.printName();
                    service.say();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    });

    //pretend we start rmi server as SERVER_THREAD thread
    public static Thread SERVER_THREAD = new Thread(new Runnable() {
        @Override
        public void run() {

            // Уникальные ключи для классов
            final String UNIQUE_KEY_FOR_CAT = "catKey";
            final String UNIQUE_KEY_FOR_DOG = "dogKey";
            try {
                //1. В методе run() необходимо инициализировать поле registry.
                // Для этого используй LocateRegistry.createRegistry (2099).
                Registry registry = LocateRegistry.createRegistry(2099);
                //2. В методе run() необходимо создать два объекта - Cat и Dog.
                Cat cat = new Cat("Cats Name");
                Dog dog = new Dog("Dogs Name");
                //3. В методе run() необходимо создать Remote объекты для созданных Cat и Dog используя
                // UnicastRemoteObject.exportObject (Remote, int).
                Remote stubCat = UnicastRemoteObject.exportObject(cat, 0);
                Remote stubDog = UnicastRemoteObject.exportObject(dog, 0);
                //4. Для Cat и Dog нужно добавить в registry связь уникального текстового ключа и
                // Remote объекта используя registry.bind (String, Remote).
                registry.bind(UNIQUE_KEY_FOR_CAT, stubCat);
                registry.bind(UNIQUE_KEY_FOR_DOG, stubDog);

                //5. При возникновении любого исключения нужно вывести его стек-трейс в поток System.err
                // используя метод printStackTrace ().

            }
            catch (Exception e) { e.printStackTrace(System.err); }
            

            //напишите тут ваш код
        }
    });

    public static void main(String[] args) throws InterruptedException {
        //start rmi server thread
        SERVER_THREAD.start();
        Thread.sleep(1000);
        //start client
        CLIENT_THREAD.start();
    }
}