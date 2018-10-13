package me.mingshan.demo;

import java.util.concurrent.*;

public class CallableTest implements Callable<String> {

    @Override
    public String call() throws Exception {
        return "hello";
    }

    public static void main(String[] args) throws InterruptedException,
        ExecutionException {
//        FutureTask<String> future = new FutureTask<>(new CallableTest());
//        new Thread(future).start();
//        System.out.println(future.get());
        System.out.println("Start:" + System.nanoTime());
        FutureTask<String> futureTask = new FutureTask<String>(new CallableTest());
        Executor executor= Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        for(int i=0;i<5;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("get result "+futureTask.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println(futureTask.get());
        System.out.println("End:" + System.nanoTime());

    }

}
