package com.exalt.ipc.powerMock;

public class Service {

	private int sumPrivate(int a, int b) {
		return a + b;
	}

	public int mulBy2(int a) {
		return sumPrivate(a, a);
	}
}
