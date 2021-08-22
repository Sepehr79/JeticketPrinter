package com.ansar.jeticketprinter.thread;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.CountDownLatch;

public class ThreadService {

    private final IThreadService iThreadService;

    public ThreadService(IThreadService iThreadService) {
        this.iThreadService = iThreadService;
    }

    public void run(){
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        iThreadService.doSome();
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public IThreadService getIThreadService() {
        return iThreadService;
    }
}
