package org.example.multicast.serverER;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

class Recibe extends Thread{
    private MulticastSocket socket;

    public Recibe(MulticastSocket m){
        this.socket = m;
    }
    public void run(){
        try{

            while (true) {
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                System.out.println("Listo para recibir mensajes...");
                socket.receive(p);
                String msj = new String(p.getData(),0,p.getLength());
                System.out.println("Mensaje recibido: "+msj);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

