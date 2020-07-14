package com.exalt.ipc.job;

public class States {
    public static final String UPLOADED = "UPLOADED";
    public static final String HELDING = "HELDING";
    public static final String PRINTING = "PRINTING";
    public static final String RETURNED = "RETURNED";


    public static String[] getStates() {
        return new String[]{UPLOADED, HELDING, PRINTING, RETURNED};
    }

}
