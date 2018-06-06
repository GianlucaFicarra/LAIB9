/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

	Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
	
    @FXML
    private Button btnConfini;

    @FXML
    private ChoiceBox<Country> btnChoice;

    @FXML
    private TextArea txtResultVicini;

    @FXML
    private Button btnVicini;



	
	public void setModel(Model model) {
		this.model = model;
		btnChoice.getItems().addAll(model.listaCountry());
		
		btnVicini.setDisable(true); //blocco il tasto vicini
	}


	@FXML
	void doCalcolaConfini(ActionEvent event) {

		
		//salvo l'anno passato dall'utente
		Integer anno;
		String annotxt = txtAnno.getText();

		//verifico se la stringa anno non sia vuota
		if (annotxt.length() == 0) {
			txtResult.appendText("Inserire un anno\n");
			return;
		}

		//se la stringa non è vuota lo converto in intero
		try {
			anno = Integer.parseInt(annotxt);
			
			//controllo anno valido
			if (anno<1816 || anno>2016) {
			txtResult.appendText("Anno fuori range valido\n");
			return;
		}
			
		} catch (NumberFormatException e) { //se non lo riesce a convertire genera un eccezione
			txtResult.appendText("Inserire un numero valido\n");
			return;
		}
		
		

		//creo un grafo per calcolare i confini
		try {
			model.createGraph(anno);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			txtResult.appendText("Errore\n");
			return;
		}
		

		//se anno valido e creazione grafo riuscita sblocco pulsante
		btnVicini.setDisable(false);
		

		txtResult.clear();
	
		//Stampare il numero di componenti connesse nel grafo.
		txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));
		
		
		//Stampare l’elenco degli stati, indicando per ciascuno il numero di stati confinanti (grado del vertice)
		Map<Country, Integer> stats = model.getCountryCounts();
		txtResult.appendText(String.format("\nElenco vertici(stati) col loro grado(archi collegati): "));
		for (Country country : stats.keySet())
			txtResult.appendText(String.format("%s %d\n", country, stats.get(country)));
	
	}
	
		@FXML
	    void doCalcolaVicini(ActionEvent event) {
			
			txtResultVicini.clear();
			
			Country country = btnChoice.getValue();
			
			if(country==null) {
				txtResultVicini.setText("Seleziona uno stato.\n");
				return;
			}
			
			if(!model.vertex(country)) {
				txtResultVicini.setText("Lo stato non è nel grafo perche non ha archi a lui connessi");
			}
			
			String vicini = model.calcolaVicini(country);
			txtResultVicini.setText("I vicini trovati partendo dal vertice "+country+" sono:\n"+vicini);

	    }
		
		

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {

		   assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
	        assert btnConfini != null : "fx:id=\"btnConfini\" was not injected: check your FXML file 'Borders.fxml'.";
	        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	        assert btnChoice != null : "fx:id=\"btnChoice\" was not injected: check your FXML file 'Borders.fxml'.";
	        assert btnVicini != null : "fx:id=\"btnVicini\" was not injected: check your FXML file 'Borders.fxml'.";
	        assert txtResultVicini != null : "fx:id=\"txtResultVicini\" was not injected: check your FXML file 'Borders.fxml'.";

	}
	
	
}
