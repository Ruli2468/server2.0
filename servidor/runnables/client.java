package servidor.runnables;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
            clientq = new BufferedReader(new InputStreamReader(client.getInputStream()));
            clientr = new DataOutputStream(client.getOutputStream());
            Thread alive = new Thread( new alives(client));
            Thread rasp = new Thread(new rasp(clientr));
            alive.start();
            while(!finish){
                if(!alive.isAlive()){
                    finish = true;
                    System.err.println(newpc+" ha excedido el tiempo maximo de respuesta");
                    System.err.println("Procedo a su desconexion...");
                    clientq.close();
                    clientr.close();
                    client.close();
                    clients.remove(newpc);
                    Thread.currentThread().stop();
                }
                String query = clientq.readLine();
                if(query.equalsIgnoreCase(null)){
                    System.err.println("conexion perdida con: "+newpc);
                    clients.remove(newpc);
                    alive.stop();
                    Thread.currentThread().stop();
                }
                else if(query.equalsIgnoreCase("bye")){
                    finish = true;
                    clients.remove(newpc);
                    alive.stop();
                }
                else if(query.equals("weather")){
                    rasp.start();
                }
            }
            System.out.println(newpc + " ha finalizado conexion con el servidor");
            clients.remove(newpc);
            clientq.close();
            clientr.close();
            client.close();
            alive.stop();
            Thread.currentThread().stop();
            
        }
        catch (IOException ex){
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
