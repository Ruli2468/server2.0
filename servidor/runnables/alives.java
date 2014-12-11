package servidor.runnables;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raul
 */
public class alives implements Runnable{
    Socket client;
    BufferedReader aliveok;
    DataOutputStream alives;
    alives(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            aliveok = new BufferedReader(new InputStreamReader(client.getInputStream()));
            alives = new DataOutputStream(client.getOutputStream());
            int counter=0;
            do{
                for(int x=0;x<5;x++){
                    String textSend = "are u alive? "+counter;
                    for(int i =0;i<textSend.length();i++){
                        alives.write((byte)textSend.charAt(i));
                    } 
                    alives.write(13);
                    alives.write(10);
                    alives.flush();
                    Thread.sleep(2000);
                    String ok = aliveok.readLine();    
                    if(ok.contains("ok")){
                        counter=0;
                    }
                    counter++;
                }
            }
            while (counter<5);
            String textSend = "You have exceeded the time limit unanswered \n Press ENTER for EXIT";
            for(int i =0;i<textSend.length();i++){
                alives.write((byte)textSend.charAt(i));
            } 

            alives.write(13);
            alives.write(10);
            alives.flush();
        } 
        catch (IOException ex) {
            System.err.println("No puedo crear los alives");
        } catch (InterruptedException ex) {
            Logger.getLogger(alives.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
