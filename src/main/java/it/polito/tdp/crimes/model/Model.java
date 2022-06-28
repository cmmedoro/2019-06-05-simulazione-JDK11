package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Integer> quartieri;
	private List<Integer> anni;
	private List<Integer> mesi;
	private List<Integer> giorni;
	private Integer minoreCriminalità;
	private List<Event> eventiData;
	//simulazione
	private Simulatore sim;
	private int numMalGestiti;
	
	public Model() {
		this.dao = new EventsDao();
		this.anni = new ArrayList<>();
		this.anni = this.dao.getAnni();
		this.mesi = new ArrayList<>(this.dao.getMesi());
		this.giorni = new ArrayList<>(this.dao.getGiorni());
	}
	
	public Integer getminCrim(int anno) {
		this.minoreCriminalità = this.dao.getMinoreCriminalita(anno);
		return this.minoreCriminalità;
	}
	
	public List<Event> getEventiData(int anno, int mese, int giorno){
		this.eventiData = new ArrayList<>(this.dao.getEventiData(anno, mese, giorno));
		return this.eventiData;
	}
	
	public List<Integer> getAnni(){
		return this.anni;
	}
	public List<Integer> getMesi(){
		return this.mesi;
	}
	public List<Integer> getGiorni(){
		return this.giorni;
	}
	
	public List<Integer> getQuartieri(){
		return this.quartieri;
	}
	
	public void creaGrafo(Integer anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		this.quartieri = new ArrayList<>();
		this.quartieri = this.dao.getVertici(anno);
		Graphs.addAllVertices(this.grafo, this.quartieri);
		//Aggiungo gli archi
		List<LatLonDistrict> centri = new ArrayList<>(this.dao.getAvgCentroDistretto(anno));
		for(LatLonDistrict ld1 : centri) {
			for(LatLonDistrict ld2 : centri) {
				Integer id1 = ld1.getId();
				Integer id2 = ld2.getId();
				if(!id1.equals(id2) && this.grafo.vertexSet().contains(id1) && this.grafo.vertexSet().contains(id2)) {
					LatLng centro1 = ld1.getCentro();
					LatLng centro2 = ld2.getCentro();
					double peso = LatLngTool.distance(centro1, centro2, LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, id1, id2, peso);
				}
			}
		}
	}
	public boolean isGraphCreated() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getVicini(Integer i){
		List<Adiacenza> adiacenze = new ArrayList<>();
		List<Integer> vicini = Graphs.neighborListOf(this.grafo, i);
		for(Integer vicino : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(i, vicino);
			double peso = this.grafo.getEdgeWeight(e);
			adiacenze.add(new Adiacenza(i, vicino, peso));
		}
		Collections.sort(adiacenze);
		return adiacenze;
	}
	
	public void simula(int anno, int mese, int giorno, int N) {
		this.sim = new Simulatore(this.grafo, this);
		this.sim.init(anno, mese, giorno, N);
		this.sim.run();
		this.numMalGestiti = this.sim.getNumMalGestiti();
	}
	
	public int getMalGestiti() {
		return this.numMalGestiti;
	}
	
}
