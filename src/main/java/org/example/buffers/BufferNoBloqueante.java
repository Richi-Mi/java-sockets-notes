package org.example.buffers;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class BufferNoBloqueante {
    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        String msj = "hola";

        for(int i = 0; i < 10; i++) {
            charBuffer.put(msj.charAt(i));
        }

        // No llamar esto si ya llamamos flip();
        // charBuffer.limit(charBuffer.position());
        // charBuffer.position(0);

        charBuffer.flip(); // Ajustar el limite a posicion y la posicion a 0
        // Nunca llamar 2 veces a flip()
    }
    public static void byteBuffer() {
        String msj = "hola";
        ByteBuffer byteBuffer = ByteBuffer.wrap(msj.getBytes());
        // No llamar a flip despues de un wrap.
    }
}
