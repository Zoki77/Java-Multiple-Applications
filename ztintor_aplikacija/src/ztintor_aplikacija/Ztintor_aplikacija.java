/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ztintor_aplikacija;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author zoran
 */
public class Ztintor_aplikacija {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Unesite argumente");
        } else {
            int portSocketServera = 8000;
            String adresaSocketServera = "127.0.0.1";
            String komanda = "";
            for (int i = 0; i < args.length; i++) {
                komanda += args[i] + " ";
            }
            System.out.println(komanda);
            try {
                Socket socket = new Socket(adresaSocketServera, portSocketServera);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                os.write(komanda.getBytes());
                os.flush();
                socket.shutdownOutput();
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int znak = is.read();
                    if (znak == -1) {
                        break;
                    }
                    sb.append((char) znak);
                }
                System.out.println("odgovor: " + sb);
            } catch (IOException ex) {
                System.out.println("Greška");
            }

        }
    }
}
