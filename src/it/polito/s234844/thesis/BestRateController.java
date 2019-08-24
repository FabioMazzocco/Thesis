package it.polito.s234844.thesis;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.s234844.thesis.model.Order;
import it.polito.s234844.thesis.model.Part;
import it.polito.s234844.thesis.model.ThesisModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class BestRateController {
	
	private ThesisController home;
	private ThesisModel model;
	private HashMap<String, Integer> orderMap;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane bestRateTop;
    
    @FXML
    private Slider bestRateSliderProbability;

    @FXML
    private Text txtProbability;

    @FXML
    private Slider bestRateSliderPercentage;

    @FXML
    private Text txtPercentage;	
    
    @FXML
    private Button btnBestRate;
    
    @FXML
    private ListView<Order> listView;
    
    @FXML
    private Text txtBestRate;
    
    @FXML
    private Text txtDays;
    
    @FXML
    private PieChart pieParts;

    @FXML
    private PieChart pieQuantity;
    
    @FXML
    private Button btnHome;

    @SuppressWarnings("unchecked")
	@FXML
    void handleBestRateCalculation(ActionEvent event) {
    	HashMap<String, Object> result = this.model.bestRate(this.orderMap, this.bestRateSliderProbability.getValue()/100, this.bestRateSliderPercentage.getValue()/100);
    	
    	if(((String)result.get("errors")).compareTo("")!=0) {
    		//Case with errors
    		System.out.println((String)result.get("errors"));
    		return;
    	}
    	
    	//List view
    	List<Order> bestRateList = new ArrayList<Order>();
    	for(Part p : (List<Part>)result.get("list")) {
    		Order o = new Order(null, p.getPart_number(), this.orderMap.get(p.getPart_number()), p.getDescription(), null, null);
    		bestRateList.add(o);
    	}
    	this.listView.getItems().clear();
    	this.listView.getItems().addAll(bestRateList);
    	
    	//Best rate text
    	this.txtBestRate.setText(String.format("%.2f", result.get("bestRate")));
    	this.txtDays.setText(String.format("%d", result.get("bestRateDays")));
    	
    	//Pie charts
    	ObservableList<PieChart.Data> partsData = FXCollections.observableArrayList(
    			new PieChart.Data("Produced quantity", this.listView.getItems().size())	,
    			new PieChart.Data("Missing quantity", this.orderMap.size()-this.listView.getItems().size()));
    	this.pieParts.setData(partsData);
    	
    	ObservableList<PieChart.Data> quantityData = FXCollections.observableArrayList(
    			new PieChart.Data("Produced parts", (Integer)result.get("bestRatePieces")),
    			new PieChart.Data("Missing parts", ((Integer)result.get("bestRateTotalPieces")-(Integer)result.get("bestRatePieces"))));
    	this.pieQuantity.setData(quantityData);
 
    }
    
    @FXML
    void initialize() {
    	assert bestRateSliderProbability != null : "fx:id=\"bestRateSliderProbability\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert txtProbability != null : "fx:id=\"txtProbability\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert bestRateSliderPercentage != null : "fx:id=\"bestRateSliderPercentage\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert txtPercentage != null : "fx:id=\"txtPercentage\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert btnBestRate != null : "fx:id=\"btnBestRate\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert txtBestRate != null : "fx:id=\"txtBestRate\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert txtDays != null : "fx:id=\"txtDays\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert pieParts != null : "fx:id=\"pieParts\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert pieQuantity != null : "fx:id=\"pieQuantity\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert btnHome != null : "fx:id=\"btnHome\" was not injected: check your FXML file 'BestRate.fxml'.";
        assert bestRateTop != null : "fx:id=\"bestRateTop\" was not injected: check your FXML file 'BestRate.fxml'.";
        //Top
        this.bestRateTop.setStyle("-fx-background-color: rgb(33, 215, 243);");
        //Sliders
        this.txtProbability.textProperty().bind(Bindings.format("%.2f%%",this.bestRateSliderProbability.valueProperty()));
        this.txtPercentage.textProperty().bind(Bindings.format("%.2f%%",this.bestRateSliderPercentage.valueProperty()));        
    }
    
    @FXML
    void goBackHome(ActionEvent event) {
    	this.home.returnToPrimary();
    }
    
    public void setOrder(ThesisController home, ThesisModel model, HashMap<String, Integer> orderMap) {
    	this.home = home;
    	this.model = model;
    	this.orderMap = orderMap;
    }
}
