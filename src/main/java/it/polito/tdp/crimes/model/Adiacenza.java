package it.polito.tdp.crimes.model;

public class Adiacenza implements Comparable<Adiacenza>{

	private Integer d1;
	private Integer d2;
	private Double peso;
	public Adiacenza(Integer d1, Integer d2, Double peso) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.peso = peso;
	}
	public Integer getD1() {
		return d1;
	}
	public void setD1(Integer d1) {
		this.d1 = d1;
	}
	public Integer getD2() {
		return d2;
	}
	public void setD2(Integer d2) {
		this.d2 = d2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Adiacenza o) {
		return this.peso.compareTo(o.peso);
	}
	
}
