package it.polito.tdp.borders.model;

import java.util.List;
import java.util.Map;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		System.out.println("Creo il grafo relativo al 2000");
		model.createGraph(2000);

		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
		List<Country> countries = model.listaCountry();
		System.out.format("Trovate %d nazioni\n", countries.size());

		System.out.println();
		
		System.out.println("Creo il grafo relativo al 1900");
		model.createGraph(1900);

		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
		countries = model.listaCountry();
		System.out.format("Trovate %d nazioni\n", countries.size());
		
		Map<Country, Integer> stats = model.getCountryCounts();
		for (Country country : stats.keySet())
			System.out.format("%s %d\n", country, stats.get(country));
	}

}
