package lldexamples.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


enum Type {
    CAR, BIKE, TRUCK, EV_CAR
}

class Ticket {
    private LocalDateTime timeEntered;
    private LocalDateTime timeExited;
    Type vehicleType;
    public Ticket(Type vehicleType) {
        this.vehicleType = vehicleType;
        this.timeEntered = LocalDateTime.now();
    }
    public void markExit(){
        this.timeExited = LocalDateTime.now();
    }

    public int totalTimePassed() {
        Duration duration = Duration.between(timeEntered, timeExited);
        return (int) duration.toHours();
    }

}


class Owner{
    private String name;
    private Ticket ticket;
    public Owner(String name) {
        this.name = name;
    }
    public void assignTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    public Ticket getTicket() {
        return ticket;
    }
    public String getName() {
        return name;
    }
    public void unassignTicket() {
        ticket = null;
    }
    public boolean haveTicket() {
        return ticket != null;
    }
}

class Vehicle {
    private Type type;
    private String number;
    public Owner owner;
    public Vehicle(Type type, String number, Owner owner) {
        this.type = type;
        this.number = number;
        this.owner = owner;
    }
    public boolean isEV(){
        return type == Type.EV_CAR;
    }

    public Type getType() {
        return type;
    }
}

class Spot{
    private Vehicle vehicle;
    private Type type;
    private int number;
    public Spot(Type type, int number) {
        this.type = type;
        this.number = number;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Type getType() {
        return type;
    }
    public void unassignVehicle() {
        vehicle = null;
    }
    public boolean isAvailable() {
        return vehicle == null;
    }
    public boolean isEV(){
        return type == Type.EV_CAR;
    }
    public Integer getSpotNo() {
        return number;
    }
}

class Floor{
    private Spot[] spots;
    private int availableSpots;
    private int floorNo;

    public Floor(int availableSpots, int floorNo) {
        this.availableSpots = availableSpots;
        this.floorNo = floorNo;
        spots = new Spot[availableSpots];
        addSpots();
    }
    private void addSpots() {
        for (int i = 0; i < availableSpots; i++) {
            spots[i] = new Spot(Type.CAR,i);
        }
    }
    public int getFloorNo(){
        return floorNo;
    }
    public Spot[] getSpots() {
        return spots;
    }
}

class Building{
    private int totalFloors;
    private int availableSpotsOnEachFloor;
    private int buildingNo;
    private Floor [] floors;

    public Building(int totalFloors, int availableSpotsOnEachFloor, int buildingNo) {
        this.totalFloors = totalFloors;
        this.availableSpotsOnEachFloor = availableSpotsOnEachFloor;
        this.buildingNo = buildingNo;
        floors = new Floor[totalFloors];
        addFloors();
    }

    private void addFloors() {
        for (int i = 0; i < totalFloors; i++) {
            floors[i] = new Floor(availableSpotsOnEachFloor, i);
        }
    }

    public Floor[] getFloors() {
        return floors;
    }

    public int getBuildingNo() {
        return buildingNo;
    }
}

class Billing{
    private int firstHourRateForCar;
    private int firstHourRateForTruck;
    private int secondHourOnwardRateForCar;
    private int secondHourOnwardRateForTruck;
    private int firstHourRateForBike;
    private int secondHourOnwardRateForBike;
    private int chargingRateForEV;

    public int getFirstHourRateForCar() {
        return firstHourRateForCar;
    }

    public void setFirstHourRateForCar(int firstHourRateForCar) {
        this.firstHourRateForCar = firstHourRateForCar;
    }

    public int getFirstHourRateForTruck() {
        return firstHourRateForTruck;
    }

    public void setFirstHourRateForTruck(int firstHourRateForTruck) {
        this.firstHourRateForTruck = firstHourRateForTruck;
    }

    public int getSecondHourOnwardRateForCar() {
        return secondHourOnwardRateForCar;
    }

    public void setSecondHourOnwardRateForCar(int secondHourOnwardRateForCar) {
        this.secondHourOnwardRateForCar = secondHourOnwardRateForCar;
    }

    public int getSecondHourOnwardRateForTruck() {
        return secondHourOnwardRateForTruck;
    }

    public void setSecondHourOnwardRateForTruck(int secondHourOnwardRateForTruck) {
        this.secondHourOnwardRateForTruck = secondHourOnwardRateForTruck;
    }

    public int getFirstHourRateForBike() {
        return firstHourRateForBike;
    }

    public void setFirstHourRateForBike(int firstHourRateForBike) {
        this.firstHourRateForBike = firstHourRateForBike;
    }

    public int getSecondHourOnwardRateForBike() {
        return secondHourOnwardRateForBike;
    }

    public void setSecondHourOnwardRateForBike(int secondHourOnwardRateForBike) {
        this.secondHourOnwardRateForBike = secondHourOnwardRateForBike;
    }

    public int getChargingRateForEV() {
        return chargingRateForEV;
    }

    public void setChargingRateForEV(int chargingRateForEV) {
        this.chargingRateForEV = chargingRateForEV;
    }

    public Billing(int firstHourRateForCar, int firstHourRateForTruck, int secondHourOnwardRateForCar, int secondHourOnwardRateForTruck, int firstHourRateForBike, int secondHourOnwardRateForBike, int chargingRateForEV) {
        this.firstHourRateForCar = firstHourRateForCar;
        this.firstHourRateForTruck = firstHourRateForTruck;
        this.secondHourOnwardRateForCar = secondHourOnwardRateForCar;
        this.secondHourOnwardRateForTruck = secondHourOnwardRateForTruck;
        this.firstHourRateForBike = firstHourRateForBike;
        this.secondHourOnwardRateForBike = secondHourOnwardRateForBike;
        this.chargingRateForEV = chargingRateForEV;
    }

    public int getBill(Ticket ticket) {
        if(ticket.vehicleType==Type.CAR){
            return firstHourRateForCar + (ticket.totalTimePassed()-1)*secondHourOnwardRateForCar;
        }
        else if(ticket.vehicleType==Type.TRUCK){
            return firstHourRateForTruck + (ticket.totalTimePassed()-1)*secondHourOnwardRateForTruck;
        }
        return firstHourRateForBike + (ticket.totalTimePassed()-1)*secondHourOnwardRateForBike;
    }
}

interface Observer{
    void update(boolean isParkingFull);
}

interface Subject{
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

class Display implements Observer{
    private boolean isParkingFull;
    public Display() {
        this.isParkingFull = false;
    }

    public void update(boolean isParkingFull) {
        this.isParkingFull = isParkingFull;
        info();
    }

    public void info(){
        if(isParkingFull){
            System.out.println("Parking Full");
        }
        else{
            System.out.println("Parking Available");
        }
    }
}


class SpotAssigner implements Subject{
    private ArrayList<Observer> observers;
    private ParkingLot parkingLot;
    private boolean parkingFull;
    public SpotAssigner(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        parkingFull = false;
        observers = new ArrayList<Observer>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(parkingFull);
        }
    }

    public Spot searchSpot(Vehicle vehicle) {
        for(Building building : parkingLot.getBuildings()){
            for(Floor floor : building.getFloors()){
                for(Spot spot : floor.getSpots()){
                    if(spot.isAvailable() && spot.getType().equals(vehicle.getType())){
                        System.out.println("Found spot");
                        System.out.println("Details : Building "+ building.getBuildingNo() + " Floor " + floor.getFloorNo() + " Spot "+ spot.getSpotNo());
                        return spot;
                    }
                }
            }
        }
        parkingFull = true;
        notifyObservers();
        return null;
    }

    public void exitVehicle(){
        parkingFull = false;
        notifyObservers();
    }
}

class ParkingLot{
    private Building [] buildings;
    private int availableSpots;
    private int totalFloors;
    private int totalBuildings;
    private Billing billing;
    private SpotAssigner spotAssigner;
    private Display display;
    private static ParkingLot instance;

    private ParkingLot(int availableSpots, int totalFloors, int totalBuildings) {
        this.availableSpots = availableSpots;
        this.totalFloors = totalFloors;
        this.totalBuildings = totalBuildings;
        buildings = new Building[totalBuildings];
        display = new Display();
        spotAssigner = new SpotAssigner(this);
        spotAssigner.addObserver(display);
        billing = new Billing(2,5,1,2,1,1,1);
        addBuildings();
    }
    private void addBuildings() {
        for (int i = 0; i < totalBuildings; i++) {
            buildings[i] = new Building(availableSpots, totalFloors, i);
        }
    }

    public Building[] getBuildings() {
        return buildings;
    }

    void addNewVehicle(Vehicle vehicle) {
        Spot spot = spotAssigner.searchSpot(vehicle);
        if(spot != null){
            spot.setVehicle(vehicle);
            vehicle.owner.assignTicket(new Ticket(vehicle.getType()));
        }
        else System.out.println("No spot found");
    }

    void exitVehicle(Spot spot) {
        Vehicle vehicle = spot.getVehicle();
        Ticket ticket = vehicle.owner.getTicket();
        ticket.markExit();
        spotAssigner.exitVehicle();
        System.out.println("Total Bill : " + billing.getBill(ticket));
    }

    public static ParkingLot getInstance(int availableSpots, int totalFloors, int totalBuildings) {
        if(instance == null) {
            synchronized (ParkingLot.class) {
                if(instance == null) {
                    instance = new ParkingLot(availableSpots, totalFloors, totalBuildings);
                }
            }
        }
        return instance;
    }
}


public class Parking {
    public static void main(String[] args) {
        ParkingLot parkingLot = ParkingLot.getInstance(10, 10, 10);
    }
}

// UML available in the same folder
