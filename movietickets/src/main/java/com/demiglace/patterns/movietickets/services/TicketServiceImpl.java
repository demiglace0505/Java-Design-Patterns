package com.demiglace.patterns.movietickets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demiglace.patterns.movietickets.dao.TicketDao;
import com.demiglace.patterns.movietickets.bo.Ticket;

@Service
public class TicketServiceImpl implements TicketService {
	@Autowired
	TicketDao dao;
	
	@Override
	public void purchaseTicket(Ticket ticket) {
		// Insert logic for processing payment here
		com.demiglace.patterns.movietickets.entities.Ticket ticketEntity = new com.demiglace.patterns.movietickets.entities.Ticket();
		ticketEntity.setId(ticket.getId());
		ticketEntity.setScreen(ticket.getScreenNo());
		ticketEntity.setMovie(ticket.getMovieName());
		ticketEntity.setSeat(ticket.getSeatNo());
		dao.create(ticketEntity);
		// Sending email/SMS to user
	}
}
