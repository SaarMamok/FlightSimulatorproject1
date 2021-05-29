package model;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ActiveObjectCommon {
    protected BlockingDeque<Runnable>tasks;
    protected Thread activeThread;
    protected volatile boolean stop;
    

    public ActiveObjectCommon() {
        this.tasks=new LinkedBlockingDeque<>();
        this.stop=true;
    }

    public void start(){
       if(stop==false)
           return;
       stop=false;
        activeThread=new Thread(()->this.run());
        activeThread.start();
    }
public void stop(){
        this.stop=true;
        this.activeThread.interrupt();
}
public void execute(Runnable r){
tasks.add(r);
}
public void run(){
        while(!stop){
            try {
                tasks.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}

}
