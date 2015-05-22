/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.kgainfobot;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;

/**
 *
 * @author amalikov
 */
public class Main {
	public static void main(String[] args) throws IOException, UnirestException {
		setShutdownHook(true);
		
		KgaInfoSession session = new KgaInfoSession();
		if (session.login("H6Pu9bp", "NawVUVi")) {
			session.register();
		} else {
			System.err.println("Login failed");
		}
		
		Unirest.shutdown();
		setShutdownHook(false);
	}
	
    private static void setShutdownHook(boolean set) {
        if (hook == null) {
            hook = new Thread() {
                @Override
                public void run() {
					System.out.println("Shutdown hook");
					
					try {
						Unirest.shutdown();
					} catch (IOException ex) {
					}
                }
            };
        }
        
        if (set)
            Runtime.getRuntime().addShutdownHook(hook);
        else
            Runtime.getRuntime().removeShutdownHook(hook);
    }

    private static Thread hook = null;
}
