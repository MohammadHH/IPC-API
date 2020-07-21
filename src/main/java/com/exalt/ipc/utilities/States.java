package com.exalt.ipc.utilities;

public class States {
	public static final String UPLOADED = "UPLOADED";

	public static final String HELDING = "HELDING";

	public static final String PRINTING = "PRINTING";

	public static final String RETAINED = "RETAINED";

	public static final String IPC = "ipc";

	public static final String HELD_QUEUE = "heldQueue";

	public static final String PRINTING_QUEUE = "printQueue";

	public static final String RETAINED_QUEUE = "retainedQueue";


	public static String[] getStates() {
		return new String[]{UPLOADED, HELDING, PRINTING, RETAINED};
	}

	//return state based on the container
	public static String get(String container) {
		switch (container) {
			case IPC:
				return UPLOADED;
			case HELD_QUEUE:
				return HELDING;
			case PRINTING_QUEUE:
				return PRINTING;
			case RETAINED_QUEUE:
				return RETAINED;
		}
		return UPLOADED;
	}

}
