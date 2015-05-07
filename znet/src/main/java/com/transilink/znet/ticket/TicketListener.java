package com.transilink.znet.ticket;
 
public interface TicketListener { 
	public void onResponseExpired(Ticket ticket); 
	public void onResponseReceived(Ticket ticket);
}
