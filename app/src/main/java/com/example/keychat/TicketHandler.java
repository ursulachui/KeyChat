package com.example.keychat;

import javax.crypto.SecretKey;

public class TicketHandler {
    private static byte[] ticket;
    private static SecretKey shared_key;

    public static void setTicket(byte[] ticket) {
        TicketHandler.ticket = ticket;
    }

    public static void setShared_key(SecretKey shared_key) {
        TicketHandler.shared_key = shared_key;
    }

    public static byte[] getTicket() {
        return ticket;
    }

    public static SecretKey getShared_key() {
        return shared_key;
    }

}
