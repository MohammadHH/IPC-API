package com.exalt.ipc.utilities;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class PrintingThread implements Runnable {
	private Thread worker;

	private Supplier supplier;

	private int interval = 1000;

	private AtomicInteger i = new AtomicInteger(0);

	private AtomicBoolean running = new AtomicBoolean(false);

	private AtomicBoolean stopped = new AtomicBoolean(true);


	public PrintingThread(int sleepInterval, Supplier supplier) {
		interval = sleepInterval;
		this.supplier = supplier;
	}

	public void start() {
		System.out.println("starting thread " + i.incrementAndGet());
		worker = new Thread(this);
		worker.start();
	}

	public void stop() {
		System.out.println("stopping thread " + i.getAndDecrement());
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
				System.out.println("Thread was interrupted, Failed to complete operation");
			}
			supplier.get();
		}
		stopped.set(true);
	}
}
