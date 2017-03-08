package com.balaev.gradproj.domain;

import javax.persistence.*;

@Entity
@Table(name = "station")
public class Station extends Throwable {
    public Station() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstation")
    private Integer idStation;

    @Column(name = "stationname")
    private String stationName;

    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
