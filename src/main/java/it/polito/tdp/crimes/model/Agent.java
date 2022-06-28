package it.polito.tdp.crimes.model;

public class Agent {
	
	private int id;
	private boolean libero;
	private Integer quartiereAttuale;
	public Agent(int id, boolean libero, Integer quartiereAttuale) {
		super();
		this.id = id;
		this.libero = libero;
		this.quartiereAttuale = quartiereAttuale;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isLibero() {
		return libero;
	}
	public void setLibero(boolean libero) {
		this.libero = libero;
	}
	public Integer getQuartiereAttuale() {
		return quartiereAttuale;
	}
	public void setQuartiereAttuale(Integer quartiereAttuale) {
		this.quartiereAttuale = quartiereAttuale;
	}
	
	

}
