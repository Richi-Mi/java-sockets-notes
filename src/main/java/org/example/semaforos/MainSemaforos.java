package org.example.semaforos;

import java.util.concurrent.Semaphore;

/**
 * Semaphore(int permisos)
 * Semaphore(int permisos, boolean justicia)
 * Metodos:
 * acquire()
 * acquireUninterruptibly()
 * int availablePermits()
 * boolean isFair() - Es un semaforo injusto si es false
 * void release()
 * void release (int permisos)
 * boolean tryAquire() - Devuelve true si podemos acceder al monitor de acceso
 */

/**
 * Tuberias (pipeS)
 * Las tuberias permiten redireccionar la salida de un hilo con la entrada de otro permitiendo así el manejo de
 * memoria compartida.
 */
public class MainSemaforos {
    final Semaphore sem = new Semaphore(2, true);


}
