package org.example.multicast.serverER;

import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class Envia extends Thread {

    private MulticastSocket socket;
    private BufferedReader br;

    public Envia(MulticastSocket m, BufferedReader br){
        this.socket = m;
        this.br     = br;
    }
    public void run(){
        try {

            //BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            String dir = "231.1.1.1";
            int pto=9931;
            InetAddress gpo = InetAddress.getByName(dir);

            while (true){
                System.out.println("Escribe un mensaje para ser enviado:");
                String mensaje= br.readLine();
                byte[] b = mensaje.getBytes();
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,pto);
                socket.send(p);
            }
        }
        catch(Exception e){
            e.printStackTrace();

        }

    }

}