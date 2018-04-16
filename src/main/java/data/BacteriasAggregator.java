package data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@XmlRootElement(name = "data")
public class BacteriasAggregator {

    @XmlElementWrapper(name = "bacterias")
    @XmlElement(name = "bacteria")
    private ArrayList<Bacteria> bacterias;

    public BacteriasAggregator(){
        bacterias = new ArrayList<>();
    }

    public BacteriasAggregator(ResultSet bacterias) throws SQLException {
        this.bacterias = new ArrayList<>();

        while (bacterias.next()){
            this.bacterias.add(new Bacteria(bacterias.getString("GENOTYPE"), bacterias.getString("CLASS")));
        }

    }

    public  void addBacterias(ArrayList<Bacteria> bacterias){
        this.bacterias.addAll(bacterias);
    }

    public void clearBacterias(){
        bacterias.clear();
    }

    public void addBacteria(Bacteria bacteria){
        bacterias.add(bacteria);
    }

    public ArrayList<Bacteria> getBacterias(){
        return bacterias;
    }

}
