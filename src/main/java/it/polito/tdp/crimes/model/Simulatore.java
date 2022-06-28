package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Evento.EventType;

public class Simulatore {
	
	//parametri in ingresso della simulazione
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	private int N; //numero di agenti ---> 1 - 10
	private Integer centrale;
	
	//parametri in output ---> risultati
	private int numMalGestititi; 
	//stato del mondo
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Model m;
	private List<Agent> agenti;
	private List<Event> eventiData;
	
	//coda prioritaria
	private PriorityQueue<Evento> queue;
	
	public Simulatore(Graph<Integer, DefaultWeightedEdge> grafo, Model m) {
		this.grafo = grafo;
		this.m = m;
	}
	
	public void init(int anno, int mese, int giorno, int N) {
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		this.N = N;
		this.centrale = this.m.getminCrim(anno);
		//recuperare gli eventi criminosi di quella data
		this.eventiData = new ArrayList<>();
		this.eventiData = this.m.getEventiData(anno, mese, giorno);
		//creare gli agenti
		this.agenti = new ArrayList<>();
		for(int i = 0; i < this.N; i++) {
			agenti.add(new Agent(i, true, this.centrale)); //libero
		}
		//inizializzo l'output
		this.numMalGestititi = 0;
		//inizializzare la coda degli eventi
		this.queue = new PriorityQueue<>();
		for(Event e : this.eventiData) {
			LocalDateTime inizio = e.getReported_date();
			this.queue.add(new Evento(inizio, null, e, EventType.NOTIFICA_NUOVO_CRIMINE));
		}
		System.out.println(this.queue.size());
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		Event caso = e.getCrimine();
		LocalDateTime ora = e.getTempo();
		int ore = ora.getHour();
		int minuti = ore*60 + ora.getMinute();
		Agent a = e.getAgente();
		switch(e.getTipo()) {
		case NOTIFICA_NUOVO_CRIMINE:
			//devo trovare l'agente libero più vicino
			Agent piuVicino = this.getPiuVicino(caso.getDistrict_id());
			if(piuVicino != null) {
				piuVicino.setLibero(false);
				double minutes = this.getTempoSpostamento(caso.getDistrict_id(), piuVicino);
				if(minutes > 15.0) {
					this.numMalGestititi++;
				}
				piuVicino.setQuartiereAttuale(caso.getDistrict_id());
				this.queue.add(new Evento(ora.plusMinutes((long)minutes), piuVicino, caso, EventType.ARRIVO_SUL_LUOGO));
			}else {
				this.numMalGestititi++;
			}
			break;
		case ARRIVO_SUL_LUOGO:
			String tipo = caso.getOffense_category_id();
			if(tipo.equals("all-other-crimes")) {
				double prob = Math.random();
				long permanenza = 0;
				if(prob < 0.5) {
					permanenza = 1;
				}else {
					permanenza = 2;
				}
				this.queue.add(new Evento(ora.plusHours(permanenza), a, caso, EventType.FINE_GESTIONE));
			}else {
				long permanenza = 1;
				this.queue.add(new Evento(ora.plusHours(permanenza), a, caso, EventType.FINE_GESTIONE));
			}
			
			break;
		case FINE_GESTIONE:
			a.setLibero(true);
			break;
		}
	}
	
	public int getNumMalGestiti() {
		return this.numMalGestititi;
	}
	
	private double getTempoSpostamento(Integer distretto, Agent assegnato) {
		if(distretto == assegnato.getQuartiereAttuale()) {
			return 0.0;
		}
		double oreSpostamento = 0.0;
		DefaultWeightedEdge edge = this.grafo.getEdge(assegnato.getQuartiereAttuale(), distretto);
		double peso = this.grafo.getEdgeWeight(edge);
		oreSpostamento = peso;
		return oreSpostamento;
	}
	
	private Agent getPiuVicino(Integer distretto) {
		double distMin = 60.0;
		Agent migliore = null;
		for(Agent a : this.agenti) {
			if(a.isLibero()) {
				if(a.getQuartiereAttuale() == distretto) {
					migliore = a;
				}else {
					DefaultWeightedEdge e = this.grafo.getEdge(a.getQuartiereAttuale(), distretto);
					double peso = this.grafo.getEdgeWeight(e);
					if(peso < distMin) {
						distMin = peso;
						migliore = a;
					}
				}
			}
		}
		return migliore;
	}
}
