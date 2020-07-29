package com.exalt.ipc.utilities;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

//Safe thread that allows creating a thread,run it and stop it
public class PrintingThread implements Runnable {

	private Thread worker;

	private Supplier supplier;

	private int interval = 1000;

	private AtomicBoolean running = new AtomicBoolean(false);

	private AtomicBoolean stopped = new AtomicBoolean(true);

	//Supplier is a work to do when running the thread
	public PrintingThread(int sleepInterval, Supplier supplier) {
		interval = sleepInterval;
		this.supplier = supplier;
	}

	public void start() {
		worker = new Thread(this);
		worker.start();
	}

	public void stop() {
		running.set(false);
	}

	public void interrupt() {
		running.set(false);
		worker.interrupt();
	}

	public boolean isRunning() {
		return running.get();
	}

	public boolean isStopped() {
		return stopped.get();
	}

	@Override
	public void run() {
		running.set(true);
		stopped.set(false);
		while (running.get()) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			supplier.get();
		}
		stopped.set(true);
	}
}
