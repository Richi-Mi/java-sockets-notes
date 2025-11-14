package org.example.multicast.serverER;

import java.net.*;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Axel
 */

public class Principal {

    static void despliegaInfoNIC(NetworkInterface netint) throws SocketException {

        System.out.printf("Nombre de despliegue: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        String multicast = (netint.supportsMulticast())?"Soporta multicast":"No soporta multicast";
        System.out.printf("Multicast: %s\n", multicast);

        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("Direccion: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }

    public static void main(String[] args){
        try{
            int pto= 9931,z=0;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"ISO-8859-1"));
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)){
                System.out.print("[Interfaz "+ ++z +"]:");
                despliegaInfoNIC(netint);
            }//for
            System.out.print("\nElige la interfaz multicast:");
            int interfaz = Integer.parseInt(br.readLine());
            //NetworkInterface ni = NetworkInterface.getByName("eth2");
            NetworkInterface ni = NetworkInterface.getByIndex(interfaz);
            //br.close();
            System.out.println("\nElegiste "+ni.getDisplayName());

            MulticastSocket m= new MulticastSocket(pto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            String dir= "231.1.1.1";
            String dir6 = "ff3e::1234:1";
            InetAddress gpo = InetAddress.getByName(dir);
            //InetAddress gpo = InetAddress.getByName("ff3e:40:2001::1");
            SocketAddress dirm;
            try{
                dirm = new InetSocketAddress(gpo,pto);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }//catch
            m.joinGroup(dirm, ni);
            System.out.println("Socket unido al grupo "+gpo);


            Recibe r = new Recibe(m);
            Envia e = new Envia(m, br);
            e.setPriority(10);
            r.start();
            e.start();
            r.join();
            e.join();
        }catch(Exception e){}
    }//main
}