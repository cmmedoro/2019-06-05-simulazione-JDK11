package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class LatLonDistrict {
	
	private Integer id;
	private double lon;
	private double lat;
	private LatLng centro;
	public LatLonDistrict(Integer id, double lon, double lat, LatLng centro) {
		super();
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.centro = centro;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public LatLng getCentro() {
		return centro;
	}
	public void setCentro(LatLng centro) {
		this.centro = centro;
	}
	

}
