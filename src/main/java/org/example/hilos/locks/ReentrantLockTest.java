package org.example.hilos.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        try{
            Lock lock=new ReentrantLock(true);
            //ReentrantLock lock1 = new ReentrantLock();
            MyRunnable myRunnable=new MyRunnable(lock);
            Thread t1 = new Thread(myRunnable,"Thread-1");
            Thread t2 = new Thread(myRunnable,"Thread-2");
            t1.start(); t2.start();
            t1.join(); t2.join();
            System.out.println("Termina el hilo principal");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}




class MyRunnable implements Runnable{

    Lock lock;
    public MyRunnable(Lock lock) {
        this.lock=lock;
    }

    public void run(){

        System.out.println(Thread.currentThread().getName()+" esperando por el monitor de acceso");

        lock.lock();
        try{
            System.out.println();
            System.out.println(Thread.currentThread().getName()+" ha llamado lock(), lockHoldCount= "+((ReentrantLock)lock).getHoldCount());
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" ha llamado lock(), lockHoldCount= "+((ReentrantLock)lock).getHoldCount());

        }finally{
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+" llamando unlock(), lockHoldCount = "+((ReentrantLock)lock).getHoldCount());
            lock.unlock();  /*****/
            System.out.println(Thread.currentThread().getName()+" llamando unlock(), lockHoldCount = "+((ReentrantLock)lock).getHoldCount());
        }
    }
}

