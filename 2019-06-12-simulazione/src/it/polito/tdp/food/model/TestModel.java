package it.polito.tdp.food.model;

import it.polito.tdp.food.db.Condiment;

public class TestModel {
	public static void main(String[] args) {

		Model model=new Model();
		model.creaGrafo(150);
		for(Condiment c:model.getGrafo().vertexSet()) {
			System.out.println(""+c+" "+model.getCibiContenuti(c));
		}
		
				
	}
}
