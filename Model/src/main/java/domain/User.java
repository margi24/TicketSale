package domain;

import java.io.Serializable;

public class User implements HasId<Integer>, Serializable {

    private String nume;
    private String parola;
    private Integer id;
    public User(Integer id, String nume, String parola) {
        this.id=id;
        this.nume = nume;
        this.parola = parola;
    }
    public User(String nume, String parola) {
        this.nume = nume;
        this.parola = parola;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
