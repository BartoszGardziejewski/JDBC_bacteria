package application;

import data.Bacteria;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.xml.bind.JAXBException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ApplicationController{

    public VBox bacClass;
    public VBox bacGen;

    public VBox touGamma;
    public VBox touBeta;
    public VBox touRank;

    public VBox flaAlpha;
    public VBox flaBeta;
    public VBox flaNumb;

    public VBox toExamGen;

    public TextField bacteriaText;

    private String titleStyle="-fx-font-size: 16; -fx-font-weight: bold; -fx-pref-height: 50; -fx-padding: 0 0 8 0";

    private ApplicationManager manager;

    public TextField DBpath;

    public void connectDB(){
        manager.connectToDB(DBpath.getText(),"user", "paslo");
    }

    public void setManager(ApplicationManager manager) {
        this.manager = manager;
    }

    public  void clearToExamine(){
        manager.clearToExamine();
    }

    public void addBacteria() throws SQLException {
        String genome = bacteriaText.getText();

        if (genome.length() == 6){
            manager.addNewBacteria(new Bacteria(genome));
        }
    }

    public  void examineAll(){
        try {
            manager.examineAllBacterias();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveXML() throws JAXBException {
        manager.saveXML();
    }

    public void refreshExaminedTable(ResultSet rs) throws SQLException {
        bacClass.getChildren().clear();
        bacGen.getChildren().clear();

        Label title;
        title = new Label("Class");
        title.setStyle(titleStyle);
        bacClass.getChildren().add(title);
        title = new Label("Genotype");
        title.setStyle(titleStyle);
        bacGen.getChildren().add(title);

        while(rs.next()){
            //Retrieve by column name
            String gClass = rs.getString("CLASS");
            String genotype= rs.getString("GENOTYPE");
            bacClass.getChildren().add(new Label(gClass));
            bacGen.getChildren().add(new Label(genotype));
        }
    }

    public void refreshToughnessTable(ResultSet rs) throws SQLException {
        touGamma.getChildren().clear();
        touBeta.getChildren().clear();
        touRank.getChildren().clear();

        Label title;
        title = new Label("Gamma");
        title.setStyle(titleStyle);
        touGamma.getChildren().add(title);
        title = new Label("Beta");
        title.setStyle(titleStyle);
        touBeta.getChildren().add(title);
        title = new Label("Rank");
        title.setStyle(titleStyle);
        touRank.getChildren().add(title);

        while(rs.next()){
            //Retrieve by column name
            String rank  = rs.getString("RANK");
            String beta = rs.getString("BETA");
            String gamma = rs.getString("GAMMA");

            //Display values
            touRank.getChildren().add(new Label(rank));
            touBeta.getChildren().add(new Label(beta));
            touGamma.getChildren().add(new Label(gamma));
        }
    }

    public void refreshFlagellaTable(ResultSet rs) throws SQLException {
        flaAlpha.getChildren().clear();
        flaBeta.getChildren().clear();
        flaNumb.getChildren().clear();

        Label title;
        title = new Label("Alpha");
        title.setStyle(titleStyle);
        flaAlpha.getChildren().add(title);
        title = new Label("Beta");
        title.setStyle(titleStyle);
        flaBeta.getChildren().add(title);
        title = new Label("Number");
        title.setStyle(titleStyle);
        flaNumb.getChildren().add(title);

        while(rs.next()){
            //Retrieve by column name
            String number  = rs.getString("NUMBER");
            String alpha = rs.getString("ALPHA");
            String beta = rs.getString("BETA");

            //Display values
            flaAlpha.getChildren().add(new Label(alpha));
            flaBeta.getChildren().add(new Label(beta));
            flaNumb.getChildren().add(new Label(number));
        }
    }

    public void refreshToExamine(ArrayList<Bacteria> bacterias){
        toExamGen.getChildren().clear();

        Label title;
        title = new Label("Genotype");
        title.setStyle(titleStyle);
        toExamGen.getChildren().add(title);

        for (Bacteria bacteria:bacterias
             ) {

            //Display values
            toExamGen.getChildren().add(new Label(bacteria.getGenotype()));
        }

    }
}
