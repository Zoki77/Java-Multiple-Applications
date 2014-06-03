/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.ztintor.db.Baza;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.ztintor.ws.klijenti.WeatherBugKlijent;

/**
 * Dretva za preuzimanje i spremanje meto podataka u bazu.
 *
 * @author zoran
 */
public class WbDretva extends Thread {

    private BP_Konfiguracija bpKonfig;
    public static boolean pauza = false;
    private Connection konekcija;
    private Statement upit;
    private Statement upit_insert;
    private Baza baza;
    private String select_upit;
    private String insert_upit;
    private int interval;
    private long pocetak;
    private long zavrsetak;
    private long razlika;

    public WbDretva(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        System.out.println("*****  WbDretva Pokrenuta  *****");
        while (true) {
            try {
                this.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException WbDretva");
            }
            if (pauza == false) {
                if (SlusacAplikacije.countWb != 0) {
                    Thread.currentThread().interrupt();
                    break;
                }
                spremiZipUBazu();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
        System.out.println("DretvaWB je zaustavljena!");
    }

    /**
     * Metoda koja se koristi za spremanje meteo podataka u bazu na temelju
     * aktivnih zip kodova,
     */
    private void spremiZipUBazu() {
        System.out.println("****** Spremanje u bazu *******");
        interval = Integer.parseInt(bpKonfig.getInterval());
        pocetak = new Date().getTime();
        try {
            baza = new Baza();
            konekcija = baza.spajanjeNaBazu();
            upit = konekcija.createStatement();
            select_upit = "SELECT * FROM ztintor_activezipcodes";
            ResultSet rs = upit.executeQuery(select_upit);
            while (rs.next()) {
                WeatherBugKlijent wbk = new WeatherBugKlijent();
                LiveWeatherData lwd = wbk.dajMeteoPodatke(rs.getString(1));
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
                System.out.println(
                        "Zip: " + rs.getString(1)
                        + " ZipWB: " + lwd.getZipCode()
                        + " Grad: " + lwd.getCity()
                        + " Temperatura: " + lwd.getTemperature()
                        + " Tlak: " + lwd.getPressure()
                        + " Vlaga: " + lwd.getHumidity()
                        + " Brzina vjetra: " + lwd.getWindSpeed()
                        + " Smjer vjetra: " + lwd.getWindDirection());
                upit_insert = konekcija.createStatement();
                insert_upit = "INSERT INTO ztintor_meteopodaci (zip, zipWB,grad,temperatura,tlak,vlaga,brzina_vjetra,smjer_vjetra,vrijeme_preuzimanja) VALUES('" + rs.getString(1) + "','" + lwd.getZipCode() + "','" + lwd.getCity() + "','" + lwd.getTemperature() + "','" + lwd.getPressure() + "','" + lwd.getHumidity() + "','" + lwd.getWindSpeed() + "','" + lwd.getWindDirection() + "','" + sdf.format(new Date().getTime()) + "')";
                upit_insert.execute(insert_upit);
            }
            rs.close();
            konekcija.close();
            baza.zatvoriKonekciju();
            upit.close();
        } catch (SQLException ex) {
            Logger.getLogger(WbDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        zavrsetak = new Date().getTime();
        razlika = zavrsetak - pocetak;
        if (interval * 1000 >= razlika) {
            try {
                System.out.println("Dretva spava sljedećih " + (((interval * 1000) - razlika)) + " ms");
                this.sleep(((interval * 1000) - razlika));
            } catch (InterruptedException ex) {
            }
        }
    }
}
