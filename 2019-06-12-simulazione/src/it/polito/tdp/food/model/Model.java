package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.Food;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<Condiment,DefaultWeightedEdge>grafo;
	FoodDao dao;
	private Map<Integer,Condiment>idCondimentMap;
	private Map<Integer,Food>idFoodMap;
	private double best;
	private List<Condiment>soluzione;
	private List<Condiment> list;
	
	public Model() {
		 dao=new FoodDao();
		 idCondimentMap=new HashMap<Integer,Condiment>();
		 idFoodMap=new HashMap<Integer,Food>();
		 dao.listAllCondiment(idCondimentMap);
		 dao.listAllFood(idFoodMap);
		 best=Double.MIN_VALUE;
		 soluzione=new ArrayList<>();
	}
	
	public int getCibiContenuti(Condiment c) {
		return dao.getCibiContenuti(c);
	}
	
	public Graph<Condiment, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}



	public Map<Integer, Condiment> getIdCondimentMap() {
		return idCondimentMap;
	}



	public Map<Integer, Food> getIdFoodMap() {
		return idFoodMap;
	}



	public void creaGrafo(int calorie) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listCondimentCalories(calorie, idCondimentMap));
		
		for(Condiment c : grafo.vertexSet()) {
		for(Condiment c1 : dao.getViciniVertice(c, this.idCondimentMap,calorie)) {
			if(!grafo.edgeSet().contains(grafo.getEdge(c, c1)) && !grafo.edgeSet().contains(grafo.getEdge(c1, c)) ) {
			grafo.addEdge(c, c1);
			grafo.setEdgeWeight(grafo.getEdge(c, c1), dao.getPesoVicini(c, c1));
			}
		}
		}	
	}
	
	public void generaRicorsione(int calorie,Condiment c) {
		list=new ArrayList<>();
		List<Condiment> parziale=new ArrayList<>();
		parziale.add(c);
		this.Ricorsione(parziale, 0);
	}
	
	public double calcolaPunteggio(List<Condiment>list) {
	double somma=0;
		for(Condiment c: list) {
		somma+=c.getCondiment_calories();
		}
		return somma;
	}
	
	public void Ricorsione(List<Condiment>parziale, int level) {
		if(calcolaPunteggio(parziale) > best) {
			soluzione.clear();
			soluzione.addAll(parziale);
			best=calcolaPunteggio(parziale);
		}
		boolean flag=true;
		List<Condiment>list=new ArrayList<>();
		list.clear();
		list.addAll(parziale);
		
			for(Condiment c: grafo.vertexSet()) {
				parziale.add(c);
				for(Condiment c1: list) {	
					if(Graphs.neighborListOf(grafo, c1).contains(c) || list.contains(c) ) {
						flag=false;
					}
				}
				if(flag==true)
				Ricorsione(parziale,level+1);
				if(parziale.size()>1) {
				parziale.remove(c);
				}
			}
	}
	
	public List<Condiment> getSoluzioneRicorsione(){
		return soluzione;
	}
}
