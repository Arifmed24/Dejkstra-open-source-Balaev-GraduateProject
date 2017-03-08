package com.balaev.gradproj.domain;

import javax.persistence.*;

@Entity
@Table(name = "train")
public class Train extends Throwable {
    public Train() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtrain")
    private Integer idTrain;

    @Column(name = "seats")
    private Integer seats;

    public Integer getIdTrain() {
        return idTrain;
    }

    public void setIdTrain(Integer idTrain) {
        this.idTrain = idTrain;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
