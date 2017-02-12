package com.balaev.gradproj.domain;


import javax.persistence.*;

@Entity
@Table(name = "timetable")
public class Timetable {
    public Timetable() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idline")
    private Integer idLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_departure", nullable = false)
    private Station stationDeparture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_arrival", nullable = false)
    private Station stationArrival;

    @Column(name = "distance", nullable = false)
    private double distance;

    public Integer getIdLine() {
        return idLine;
    }

    public void setIdLine(Integer idLine) {
        this.idLine = idLine;
    }

    public Station getStationDeparture() {
        return stationDeparture;
    }

    public void setStationDeparture(Station stationDeparture) {
        this.stationDeparture = stationDeparture;
    }

    public Station getStationArrival() {
        return stationArrival;
    }

    public void setStationArrival(Station stationArrival) {
        this.stationArrival = stationArrival;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
