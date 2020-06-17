package domain;

import java.io.Serializable;

public class Meci implements HasId<Integer>, Serializable {

    private Integer id;
    private String nume;
    private String pret;
    private String nrBilete;
    public Meci(Integer id, String nume, String pret, String nrBileteDisponibile) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.nrBilete=nrBileteDisponibile;
    }

    @Override
    public Integer getID(){
        return this.id;
    }

    @Override
    public void setID(Integer id) {
        this.id=id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getNrBileteDisponibile() {
        return nrBilete;
    }

    public void setNrBileteDisponibile(String nrBilete) {
        this.nrBilete = nrBilete;
    }
}
