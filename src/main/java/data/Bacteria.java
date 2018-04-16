package data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "bacteria")

@XmlType(propOrder = { "genotype", "gClass"})
public class Bacteria {

    private String genotype;
    private String gClass;

    public Bacteria(){
        genotype = null;
        gClass = null;
    }

    public  Bacteria(String genotype, String gClass){
        this.genotype = genotype;
        this.gClass = gClass;
    }

    public Bacteria(String genotype){
        this.genotype = genotype;
        this.gClass = null;
    }

    public int getAlpha(){
        return Integer.parseInt((genotype.charAt(0) + (genotype.charAt(5)+"")) );
    }

    public int getBeta(){
        return Integer.parseInt((genotype.charAt(1) + (genotype.charAt(4)+"")) );
    }

    public int getGamma(){
        return Integer.parseInt((genotype.charAt(2) + (genotype.charAt(3)+"")) );
    }

    public char getNumber(){
        return  gClass.charAt(0);
    }

    public char getRank(){
        return gClass.charAt(1);
    }

    public String getgClass() {
        return gClass;
    }

    public void setgClass(String gClass){
        this.gClass = gClass;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }
}
