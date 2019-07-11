package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	
	//inserire tipo di dao

		private FoodDao dao;

		

		//scelta valore mappa

		private Map<Integer,Condiment> idMap;

		

		//scelta tipo valori lista

		private List<Condiment> vertex;

		

		//scelta tra uno dei due edges

		private List<Adiacenza> edges;

		

		//scelta tipo vertici e tipo archi

		private Graph<Condiment,DefaultWeightedEdge> graph;
		
		public Model() {

			

			//inserire tipo dao

			dao  = new FoodDao();

			//inserire tipo values

			idMap = new HashMap<Integer,Condiment>();

		}

		

	public String creaGrafo(Double calorie) {
		
		//scelta tipo vertici e archi

				graph = new SimpleWeightedGraph<Condiment,DefaultWeightedEdge>(DefaultWeightedEdge.class);

				

				//scelta tipo valori lista

				vertex = new ArrayList<Condiment>(dao.getVertex(calorie,idMap));

				Graphs.addAllVertices(graph,vertex);

				

				edges = new ArrayList<Adiacenza>(dao.getEdges(calorie));

				

				for(Adiacenza a : edges) {


					Condiment source = idMap.get(a.getId1());

					Condiment target = idMap.get(a.getId2());

					double peso = a.getPeso();

					Graphs.addEdge(graph,source,target,peso);

					System.out.println("AGGIUNTO ARCO TRA: "+source.toString()+" e "+target.toString());

					

				}

				

				System.out.println("#vertici: "+ graph.vertexSet().size());

				System.out.println("#archi: "+ graph.edgeSet().size());
				
				String ris= trovaVicini();
				
				return ris;
		
		
	}



	private String trovaVicini() {
		
		String ris= "Elenco di tutti gli ingredienti: \n";
		
		Collections.sort(vertex);
		
		for (Condiment c: vertex)
		{
			List<Condiment> vicini= Graphs.neighborListOf(graph, c);
			int peso=0;
			for (Condiment v: vicini)
			{
				DefaultWeightedEdge e= graph.getEdge(c, v);
				peso+= graph.getEdgeWeight(e);
				
			}
			
			ris+= "- "+ c.getDisplay_name() + " " + c.getCondiment_portion_size()+ " "+ c.getCondiment_calories()+" contenuto in " + peso + " cibi\n";
			
		}
		
		
		return ris;
	}

}
