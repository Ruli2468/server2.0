package servidor.runnables;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Raul
 */
public class rasp implements Runnable{
    DataOutputStream clientr;
    DataOutputStream query; 
    BufferedReader response;
  
    rasp(DataOutputStream clientr) {
        this.clientr = clientr;
    }
    
    @Override
    public void run() {
        try {
            Socket rasp = new Socket("192.168.2.5",6868);
            response = new BufferedReader(new InputStreamReader(rasp.getInputStream()));
            query = new DataOutputStream(new DataOutputStream(rasp.getOutputStream()));
            String txt = "weather";
            query.writeBytes(txt);
            query.write(13);
            query.write(10);
            query.flush();
            String responserp = response.readLine()+"ºC";
            for(int i =0;i<responserp.length();i++){
                clientr.write((byte)responserp.charAt(i));
            }
            clientr.write(13);
            clientr.write(10);
            clientr.flush();
            Thread.currentThread().stop();
        } 
        catch (IOException ex) {
            System.err.println("No puedo conectar con la raspberry pi");
        }   
    }
}
