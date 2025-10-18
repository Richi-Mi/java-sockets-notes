package org.example.sendObjects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor_O {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Servidor Iniciado");

            while (true) {
                Socket cl = serverSocket.accept();
                // OutputStream se usa para leer objetos recibidos por la red.
                ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());

                int[][] x = new int[3][3];
                for(int i=0;i<3;i++)
                    for(int j=0; j<3;j++)
                        x[i][j]=1;

                // Se manda .clone() por qué si no se le mandaria la dirección de memoria en vez del objeto.
                MyObject object = new MyObject(1, 2.0f, "tres", x.clone());
                oos.writeObject(object);
                oos.flush();

                System.out.println("Cliente conectado... Enviando objeto con los datos\nX:"+object.getX()+" Y:"+object.getY()+" Z:"+object.getZ() );

                for(int i=0;i<x.length;i++){
                    for(int j=0;j<x[0].length;j++){
                        System.out.print(x[i][j]+" ");
                    }//for
                    System.out.println("");
                }//for
                x = new int[3][3];  /* debes regenerar la matriz antes de volver a mandar*/
                for(int i=0;i<3;i++)
                    for(int j=0; j<3;j++)
                        x[i][j]=2;

                MyObject ob3 = new MyObject(2,2.0f,"dos",x.clone());
                oos.writeObject(ob3);
                oos.flush();
                System.out.println("Enviando objeto con los datos\nX:"+ob3.getX()+" Y:"+ob3.getY()+" Z:"+ob3.getZ() );

                for(int i=0;i<x.length;i++){
                    for(int j=0;j<x[0].length;j++){
                        System.out.print(x[i][j]+" ");
                    }//for
                    System.out.println("");
                }//for


                ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
                MyObject ob = (MyObject) ois.readObject();
                int[][]y = ob.getM();
                System.out.println("Objeto recibido desde"+cl.getInetAddress()+":"+cl.getPort()+" con los datos");
                System.out.println("x:"+ob.getX()+" y:"+ob.getY()+" Z:"+ob.getZ());
                System.out.println("M: \n");
                for(int i=0;i<y.length;i++){
                    for(int j=0;j<y[0].length;j++){
                        System.out.print(y[i][j]+" ");
                    }//for
                    System.out.println("");
                }//for

                ois.close();
                oos.close();
                cl.close();
            }//for

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
