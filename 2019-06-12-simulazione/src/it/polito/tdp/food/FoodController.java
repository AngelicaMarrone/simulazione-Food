package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {

	Model model=new Model();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtCalorie;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<Condiment> boxIngrediente;

    @FXML
    private Button btnDietaEquilibrata;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaDieta(ActionEvent event) {
    	try {
    		txtResult.clear();
    	model.creaGrafo(Integer.parseInt(this.txtCalorie.getText()));
    	if(this.boxIngrediente.getValue()!=null) {
    		model.generaRicorsione(Integer.parseInt(this.txtCalorie.getText()),this.boxIngrediente.getValue());
    		for(Condiment c : model.getSoluzioneRicorsione())
    		txtResult.appendText(""+c+"\n");
    	}
    	else {
    		txtResult.appendText("Devi selezionare un ingrediente\n");
    	}
    	
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Devi inserire un numero\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	try {	
    		int calorie=Integer.parseInt(txtCalorie.getText());
    		if(calorie<=0) {
    			txtResult.appendText("Inserisci un numero positivo\n");
    		}
    		else {
    			model.creaGrafo(calorie);
    			this.boxIngrediente.getItems().clear();
    			this.boxIngrediente.getItems().addAll(model.getGrafo().vertexSet());		
    			
    			for(Condiment c:model.getGrafo().vertexSet()) {
    				txtResult.appendText(""+c.getCondiment_id()+" "+c.getDisplay_name()+" "+c.getCondiment_calories()+" cal. Contenuto in "+model.getCibiContenuti(c)+" cibi diversi\n");
    			}
    		}
    		}
    		catch(NumberFormatException nfe) {
    			txtResult.appendText("Inserisci un numero\n");
    		}
    }

    @FXML
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model=model;
    }
}
