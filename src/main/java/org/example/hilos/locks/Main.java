package org.example.hilos.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Un bloqueo es un mecanismo para controlar el acceso a un recurso compartido por mutliples hilos. Un lock es un objeto
 * que solamente puede ser poseido por un hilo a la vez.
 */
public class Main {
    public void bloquearHilo() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        // Intenta bloquear el monitor de acceso,
        // Se pone en while hasta para qué se bloquee cuando este listo.
        // while(!lock.tryLock()) {

        // }

        lock.getQueueLength(); // # Hilos en estado de bloqueado
        // int waitQueueLength = lock.getWaitQueueLength( condicion : Condition );// Cantidad de hilos formados en espera de cumplir una condición
        lock.isFair(); // Si se creo con un parametro de justicia.
        lock.isLocked(); // Devuelve si el monitor de acceso esta bloqueado o desbloqueado.

    }
    public void bloquearLecturaYEscritura() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
    }
    public static void main(String[] args) {
        ReentrantLock locker = new ReentrantLock();
        // ReentrantLock lock2 = new ReentrantLock(true); -- Parametro de justicia. Le indicamos
        // al planificador qué este tiene prioridad sobre otros
        locker.lock(); // Bloquea el monitor de acceso para otros hilos.

        // try-catch para acceder a los recursos compartidos.

        // en el finally
        locker.unlock(); // Liberamos el monitor de acceso a otros hilos.
    }
}
