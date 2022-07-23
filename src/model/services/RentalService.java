package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

import java.util.concurrent.TimeUnit;

public class RentalService {
    private Double pricePerDay;
    private Double pricePerHour;

    private TaxService taxService;

    public RentalService(Double pricePerDay, Double pricePerHour, TaxService taxService) {
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.taxService = taxService;
    }

    public void processInvoice(CarRental carRental) {
        long t1 = carRental.getFinish().getTime();
        long t2 = carRental.getStart().getTime();
        double hours = (double) (t1 - t2) / 1000 / 60 / 60;

        double basicPayment;
        if(hours <= 12.00) {
            basicPayment = Math.ceil(hours) * pricePerHour;
        }
        else {
            basicPayment = Math.ceil(hours / 24) * pricePerDay;
        }

        double tax = taxService.tax(basicPayment);

        carRental.setInvoice(new Invoice(basicPayment, tax));
    }
}
