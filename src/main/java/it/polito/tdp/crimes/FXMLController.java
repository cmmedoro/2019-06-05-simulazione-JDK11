/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		this.txtResult.setText("Devi selezionare un anno dall'apposito menù a tendina");
    		return;
    	}
    	//se sono qui proseguo con la creazione del grafo
    	this.model.creaGrafo(anno);
    	this.txtResult.setText("Grafo creato\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    	for(Integer i : this.model.getQuartieri()) {
    		this.txtResult.appendText("\nVicini del distretto "+i+"\n");
    		List<Adiacenza> vicini = this.model.getVicini(i);
    		for(Adiacenza a : vicini) {
    			this.txtResult.appendText(a.getD2()+ " - "+a.getPeso()+"\n");
    		}
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		this.txtResult.setText("Devi selezionare un anno dall'apposito menù a tendina");
    		return;
    	}
    	Integer mese = this.boxMese.getValue();
    	if(mese == null) {
    		this.txtResult.setText("Devi selezionare un mese dall'apposito menù a tendina");
    		return;
    	}
    	Integer giorno = this.boxGiorno.getValue();
    	if(giorno == null) {
    		this.txtResult.setText("Devi selezionare un giorno dall'apposito menù a tendina");
    		return;
    	}
    	int n;
    	try {
    		n = Integer.parseInt(this.txtN.getText());
    		if( n < 1 || n > 10) {
    			this.txtResult.setText("Devi inserire un numero compreso fra 1 e 10");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero");
    		return;
    	}
    	//simula
    	this.model.simula(anno, mese, giorno, n);
    	this.txtResult.setText("Numero eventi criminosi mal gestiti: "+this.model.getMalGestiti()+"\n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().clear();
    	this.boxAnno.getItems().addAll(this.model.getAnni());
    	this.boxMese.getItems().clear();
    	this.boxMese.getItems().addAll(this.model.getMesi());
    	this.boxGiorno.getItems().clear();
    	this.boxGiorno.getItems().addAll(this.model.getGiorni());
    }
}
