package com.balaev.gradproj.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "route_timetables")
public class RouteTimetables extends Throwable {
    public RouteTimetables() {
    }

    @Id
    @Column(name = "id_event")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvent;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "line", nullable = false)
    private Timetable line;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private Route routeId;

    @Column(name = "number_in_route", nullable = false)
    private Integer numberInRoute;

    @Column(name = "date_departure", columnDefinition="DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeparture;

    @Column(name = "date_arrival", nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArrival;

    @Column(name = "free_seats", nullable = false)
    private Integer freeSeats;

    @ManyToMany(mappedBy = "routeTimetables",fetch = FetchType.EAGER)
    private Set<Ticket> tickets = new HashSet<>();

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }

    public Timetable getLine() {
        return line;
    }

    public void setLine(Timetable line) {
        this.line = line;
    }

    public Route getRouteId() {
        return routeId;
    }

    public void setRouteId(Route routeId) {
        this.routeId = routeId;
    }

    public Integer getNumberInRoute() {
        return numberInRoute;
    }

    public void setNumberInRoute(Integer numberInRoute) {
        this.numberInRoute = numberInRoute;
    }

    public Date getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(Date dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public Date getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Integer getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "{" +
                " from=" + line.getStationDeparture().getStationName() +
                ", to=" + line.getStationArrival().getStationName() +
                ", dateDeparture=" + dateDeparture +
                ", dateArrival=" + dateArrival +
                '}';
    }
}
