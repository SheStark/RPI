package pg.eti.ksg.ProjektInzynierski.Models;

import java.util.LinkedList;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;

public class RouteWithPoints {
    private Routes route;
    private LinkedList<Points> points;

    public RouteWithPoints(Routes route, LinkedList<Points> points) {
        this.route = route;
        this.points = points;
    }

    public RouteWithPoints() {
    }

    public Routes getRoute() {
        return route;
    }

    public Iterable<Points> getPoints() {
        return points;
    }

    public void setRoute(Routes route) {
        this.route = route;
    }

    public void setPoints(LinkedList<Points> points) {
        this.points = points;
    }
}
