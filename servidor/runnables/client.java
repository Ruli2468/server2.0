package servidor.runnables;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raul
 */
public class client implements Runnable{
    
    int counter;
    Socket client;
    BufferedReader clientq;
    DataOutputStream clientr;
    String newpc;
    ArrayList<String> clients;
    

    
    public client(Socket client, String newpc, ArrayList<String> clients) {
        this.client = client;
        this.newpc = newpc;
        this.clients = clients;
    }

    @Override
    public void run() {
        try{
            boolean finish = false;
            Thread alive = new Thread( new alives(client));
            Thread rasp = new Thread(new rasp(client));
            alive.start();
            rasp.start();
            while(!finish){
                if(!alive.isAlive()){
                    finish = true;
                    System.err.println(newpc+" ha excedido el tiempo maximo de respuesta");
                    System.err.println("Procedo a su desconexion...");
                    clientq.close();
                    clientr.close();
                    rasp.stop();
                    client.close();
                    clients.remove(newpc);
                    Thread.currentThread().stop();
                }
            }
        }
        catch (IOException ex){
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
