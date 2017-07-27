package de.dokukaefer;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Liquinator extends Application {

	private TextField textfield_milliliter, textfield_prozent, textfield_pg48_anteil;
	private Button okButton, clearButton;
	private Label label;
	private static final String MELDUNG = "1) Flüssigkeitsmenge in ml\n"
			+ "2) prozentualer Anteil von VG (0 - 100)\n"
			+ "3) mg/L Nicotin (0 - 48)";
	
	public Results calculateLiquids(double fluessigkeitsMenge, double prozent, double nicotin) {
		double ergebnisPG48, ergebnisPG0, mililiterVG, mililiterPG, prozentVG, prozentPG, prozentualerPGwert;		
		//berechnungen 
		prozentualerPGwert = 100.0 - prozent;

		mililiterVG = (prozent /100.0) * fluessigkeitsMenge;
		mililiterPG = fluessigkeitsMenge - mililiterVG;
		
		prozentVG = prozent / 100.0 * fluessigkeitsMenge;
		prozentPG = fluessigkeitsMenge - prozentVG;

		ergebnisPG48 = ((1/48.0) * nicotin)* prozentPG;
		ergebnisPG0 = prozentPG - ergebnisPG48;
		
		return new Results(fluessigkeitsMenge, prozent, prozentualerPGwert, ergebnisPG48, ergebnisPG0, mililiterPG, mililiterVG);
	}
	
	public class Results {
		
		private Double ergebnisVGInProzent, ergebnisPGInProzent, ergebnisPG48inMl, ergebnisPG0inMl, ergebnisPGgesamtInMl, ergebnisVGinMl, fluessigkeitsMenge;
		
		public Results(double fluessigkeitsMenge, double vGinProzent, double pGinProzent, double pG48inMl, double pG0inMl, double pGGesamtInMl, double vGinMl) {
			this.fluessigkeitsMenge = fluessigkeitsMenge;
			this.ergebnisVGInProzent = vGinProzent;
			this.ergebnisPGInProzent = pGinProzent;
			this.ergebnisPG48inMl = pG48inMl;
			this.ergebnisPG0inMl = pG0inMl;
			this.ergebnisPGgesamtInMl = pGGesamtInMl;
			this.ergebnisVGinMl = vGinMl;
		}
		public String getAllResults() {
			return String.format("Flüssigkeitsmenge = %s\n"
					+ "Prozentualer Anteil VG = %s\nProzentualer Anteil PG = %s\n"
					+ "VG Gesamt = %s ml\n"
					+ "PG Gesamt = %s ml\n"
					+ "PG48 = %s ml\n"
					+ "PG0 = %s ml\n",
					fluessigkeitsMenge.toString(), ergebnisVGInProzent.toString(), ergebnisPGInProzent.toString(), ergebnisVGinMl.toString(), 
					ergebnisPGgesamtInMl.toString(), ergebnisPG48inMl.toString(), ergebnisPG0inMl.toString());
		}
		
	}
	
	
	private HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    
	    textfield_milliliter = new TextField("1000");
	    textfield_milliliter.setPrefSize(50, 20);
	    textfield_prozent = new TextField("50");
	    textfield_prozent.setPrefSize(50, 20);
		textfield_pg48_anteil = new TextField("24");
	    textfield_pg48_anteil.setPrefSize(50, 20);
		
	    okButton = new Button("OK");
		okButton.setPrefSize(60, 20);
		clearButton = new Button("Clear");
		clearButton.setPrefSize(60, 20);
		
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(textfield_milliliter, textfield_prozent, textfield_pg48_anteil, okButton, clearButton); 
		
		return hbox;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private VBox addVBox() {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		
		label = new Label();
		label.setText(MELDUNG);
	    
		
	    okButton.setOnAction(new EventHandler() {
			@Override
			public void handle(Event e) {
				try {
					Double flm = Double.parseDouble(textfield_milliliter.getText());
					Double prozent = Double.parseDouble(textfield_prozent.getText());
					Double nicotin = Double.parseDouble(textfield_pg48_anteil.getText());
					if ((prozent >= 0 && prozent < 101) && (nicotin >= 0 && nicotin < 49)) {
					label.setText(calculateLiquids(flm, prozent, nicotin).getAllResults());
					} else {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException nfe) {
					label.setText(MELDUNG);
				}
				
			}
		});
	  
	    clearButton.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				textfield_milliliter.setText("1000");
				textfield_prozent.setText("50");
				textfield_pg48_anteil.setText("24");
				label.setText(MELDUNG);
				
			}
	    	
	    });
	    
	    vbox.getChildren().addAll(label);
	    return vbox;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane borderPane = new BorderPane();
		HBox hbox = addHBox();
		VBox vbox = addVBox();
		
		borderPane.setTop(hbox);
		borderPane.setCenter(vbox);
		
		Scene scene = new Scene(borderPane);
		
		primaryStage.setTitle("Liquinator");
		primaryStage.setScene(scene);
		
		//primaryStage.setWidth(330);
		primaryStage.setHeight(250);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}

