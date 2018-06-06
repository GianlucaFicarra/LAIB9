package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;


public class Model {

	private Graph<Country, DefaultEdge> graph;
	private List<Country> stati;
	private BordersDAO dao;
	
	//PER I METODI ALTERNATIVI
	private CountryIdMap countryIdMap;
	
	public Model() {
	dao= new BordersDAO();
	}

	/*metodo per creare grafo che rappresenti la situazione dei confini mondiali nell’anno specificato dall’utente

	 I vertici del grafo rappresentano tutte le nazioni tra le quali esiste un confine nell’arco di
	tempo specificato, e gli archi (non orientati e non pesati) rappresentano un confine via terra
	(‘conttype’=1)*/
	
	public void createGraph(Integer anno) {
		//Le relazioni di confine da considerare sono tutte quelle in cui campo ‘year’ sia minore o uguale dell’anno specificato
		List<Border> statiConfinanti= new ArrayList<Border>();
		
		/*prendo tutti gli stati confinanti fino alla data passata
		 * diventeranno i vertiti del mio grafo*/
		stati=dao.loadAllCountries();
		statiConfinanti= dao.getStatiConfinanti(stati, anno);
		
		//controllo se ho stati confinanti
		if (statiConfinanti == null || statiConfinanti.isEmpty())
			throw new RuntimeException("NO stati confinanti in quell'anno");

		
		//creo grafo
		this.graph= new SimpleGraph<>(DefaultEdge.class);
		
		//aggiungo vertici(country) e archi(border=confini) al grafo
		for(Border paese: statiConfinanti) {
			// Aggiungo le nazioni per il solo intervallo specificato dall'anno
						graph.addVertex(paese.getC1());
						graph.addVertex(paese.getC2());
						graph.addEdge(paese.getC1(), paese.getC2());
		}
		

		System.out.format("\nInseriti: %d vertici, %d archi\n", graph.vertexSet().size(), graph.edgeSet().size());


		stati = new ArrayList<>(graph.vertexSet());
		Collections.sort(stati);

		
	}

	

	//il numero di stati confinanti (grado del vertice)
	public int getNumberOfConnectedComponents() {
		if (graph == null)
			throw new RuntimeException("Grafo non esistente");
		
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(graph);
		return ci.connectedSets().size();
	}

	//numero di componenti connesse nel grafo
	public Map<Country, Integer> getCountryCounts() {
		if (graph == null)
			throw new RuntimeException("Grafo non esistente");
		
		Map<Country, Integer> stats = new TreeMap<Country, Integer>();
		for (Country country : graph.vertexSet()) { //per ogni vertice(country) salvo il valore degli assi connessi
 			stats.put(country, graph.degreeOf(country));
		}

		return stats;
	}

	public List<Country> listaCountry() {
		BordersDAO dao= new BordersDAO();
		return dao.loadAllCountries();
	}

	public String calcolaVicini(Country country) {
		List<Country> visitati = new ArrayList<>();
		
		//visita in ampiezza, finche cè prossimo aggiungilo ai visitati
		DepthFirstIterator<Country, DefaultEdge> dfv= new DepthFirstIterator<>(this.graph, country);
		while(dfv.hasNext())
			visitati.add(dfv.next());
		
		String result="";
		for(Country c: visitati) {
			result+= c.getStatoNome()+"\n";
		}
		
		return result;
	}

	public boolean vertex(Country start) {
		if(!this.graph.containsVertex(start))
				return false;
		else return true;
	}
	
	//---------------- MODI ALTERNATIVI -----------------------

	public List<Country> getReachableCountries(Country selectedCountry) {

		if (!graph.vertexSet().contains(selectedCountry)) {
			throw new RuntimeException("Selected Country not in graph");
		}

		List<Country> reachableCountries = this.displayAllNeighboursIterative(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());
		reachableCountries = this.displayAllNeighboursJGraphT(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());
		reachableCountries = this.displayAllNeighboursRecursive(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());

		return reachableCountries;
	}
	
	/*
	 * VERSIONE ITERATIVA
	 */
	private List<Country> displayAllNeighboursIterative(Country selectedCountry) {

		// Creo due liste: quella dei noti visitati ..
		List<Country> visited = new LinkedList<Country>();

		// .. e quella dei nodi da visitare
		List<Country> toBeVisited = new LinkedList<Country>();

		// Aggiungo alla lista dei vertici visitati il nodo di partenza.
		visited.add(selectedCountry);

		// Aggiungo ai vertici da visitare tutti i vertici collegati a quello inserito
		toBeVisited.addAll(Graphs.neighborListOf(graph, selectedCountry));

		while (!toBeVisited.isEmpty()) {

			// Rimuovi il vertice in testa alla coda
			Country temp = toBeVisited.remove(0);

			// Aggiungi il nodo alla lista di quelli visitati
			visited.add(temp);

			// Ottieni tutti i vicini di un nodo
			List<Country> listaDeiVicini = Graphs.neighborListOf(graph, temp);

			// Rimuovi da questa lista tutti quelli che hai già visitato..
			listaDeiVicini.removeAll(visited);

			// .. e quelli che sai già che devi visitare.
			listaDeiVicini.removeAll(toBeVisited);

			// Aggiungi i rimanenenti alla coda di quelli che devi visitare.
			toBeVisited.addAll(listaDeiVicini);
		}

		// Ritorna la lista di tutti i nodi raggiungibili
		return visited;
	}

	/*
	 * VERSIONE LIBRERIA JGRAPHT
	 */
	private List<Country> displayAllNeighboursJGraphT(Country selectedCountry) {

		List<Country> visited = new LinkedList<Country>();

		// Versione 1 : utilizzo un BreadthFirstIterator
//		GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<Country, DefaultEdge>(graph,
//				selectedCountry);
//		while (bfv.hasNext()) {
//			visited.add(bfv.next());
//		}

		// Versione 2 : utilizzo un DepthFirstIterator
		GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<Country, DefaultEdge>(graph, selectedCountry);
		while (dfv.hasNext()) {
			visited.add(dfv.next());
		}

		return visited;
	}

	/*
	 * VERSIONE RICORSIVA
	 */
	private List<Country> displayAllNeighboursRecursive(Country selectedCountry) {

		List<Country> visited = new LinkedList<Country>();
		recursiveVisit(selectedCountry, visited);
		return visited;
	}

	private void recursiveVisit(Country n, List<Country> visited) {
		// Do always
		visited.add(n);

		// cycle
		for (Country c : Graphs.neighborListOf(graph, n)) {	
			// filter
			if (!visited.contains(c))
				recursiveVisit(c, visited);
				// DO NOT REMOVE!! (no backtrack)
		}
	}

}
