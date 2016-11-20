package com.example.nmarcantonio.flysys2;



/**
 * Created by Usuario on 19/11/2016.
 */
//TODO agregarle variables en caso de necesitar
public class Flight {
    private String number;
    private Airline airline;
    public String adults;

   public Flight(String number,Airline airline,String adults){
       this.number = number;
       this.airline = airline;
       this.adults = adults;
   }

    public Airline getAirline() {
        return airline;
    }

    public String getNumber() {
        return number;
    }


}
