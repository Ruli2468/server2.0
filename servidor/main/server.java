package servidor.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import servidor.runnables.client;

/**
 *
 * @author Raul
 */
public class server {
    private static ArrayList<String> clients;
    
    public static void main(){
        clients = new ArrayList<>();
        try {
            ServerSocket server = new ServerSocket(1112);
            System.out.println("***SERVIDOR CONECTADO***");
            while(true){
                Socket client = server.accept();
                String newpc = client.getInetAddress().getHostAddress()+client.getInetAddress().getHostName();
                if(clients.isEmpty()){
                    clients.add(newpc);
                    System.out.println("Aceptada conexion de: "+newpc);
                    new Thread(new client(client, newpc,clients)).start();
                }
                else{
                    boolean found = isAlwaysConnect(newpc);
                    if(found){
                        System.err.println("Cliente ya conectado al servidor");
                        DataOutputStream exit = new DataOutputStream(client.getOutputStream());
                        String textSend = "Este pc ya está conectado al servidor";
                        for(int i =0;i<textSend.length();i++){
                            exit.write((byte)textSend.charAt(i));
                        }
                        exit.write(13);
                        exit.write(10);
                        exit.flush();
                        exit.close();
                        client.close();
                    }
                    else{
                        clients.add(newpc);
                        System.out.println("Aceptada conexion de: " + newpc+clients);
                        new Thread(new client(client, newpc,clients)).start();
                    }
                }
            }
        } 
        catch (IOException ex) {
            System.err.println("***IMPOSIBLE DE CONECTAR***");
        }
    }
     
    private static boolean isAlwaysConnect(String newpc){
        Iterator it = clients.iterator();
        boolean found = false;
        while (it.hasNext()){
            String client = (String) it.next();
            if(client.contains(newpc))
                found = true;
        }
        return found;
    }
}
