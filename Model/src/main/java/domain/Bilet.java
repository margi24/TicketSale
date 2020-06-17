package domain;

import java.io.Serializable;

public class Bilet implements HasId<Integer>, Serializable {
    private Integer id;
    Integer idMeci;


    private Integer nrBilete;

    public Bilet(Integer id, Integer meci, Integer nrBilete) {
        this.id = id;
        this.idMeci = meci;
        this.nrBilete = nrBilete;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {

    }

    public Integer getIdMeci() {
        return idMeci;
    }

    public void setIdMeci(Integer idMeci) {
        this.idMeci = idMeci;
    }

    public Integer getNrBilete() {
        return nrBilete;
    }

    public void setNrBilete(Integer nrBilete) {
        this.nrBilete = nrBilete;
    }
}
