package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	public enum EventType{
		NOTIFICA_NUOVO_CRIMINE, //si verifica un evento criminoso
		ARRIVO_SUL_LUOGO, //ho trovato un agente libero e ho assegnato il caso all'agente
		FINE_GESTIONE //terminata la gestione del caso, l'agente torna libero
	}
	private LocalDateTime tempo;
	private Agent agente;
	private Event crimine;
	private EventType tipo;
	public Evento(LocalDateTime tempo, Agent agente, Event crimine, EventType tipo) {
		super();
		this.tempo = tempo;
		this.agente = agente;
		this.crimine = crimine;
		this.tipo = tipo;
	}
	public LocalDateTime getTempo() {
		return tempo;
	}
	public void setTempo(LocalDateTime tempo) {
		this.tempo = tempo;
	}
	public Agent getAgente() {
		return agente;
	}
	public void setAgente(Agent agente) {
		this.agente = agente;
	}
	public Event getCrimine() {
		return crimine;
	}
	public void setCrimine(Event crimine) {
		this.crimine = crimine;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	@Override
	public int compareTo(Evento o) {
		return this.tempo.compareTo(o.getTempo());
	}
	
}
