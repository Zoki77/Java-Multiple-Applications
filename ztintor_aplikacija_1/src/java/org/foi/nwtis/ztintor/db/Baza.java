/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.web.slusaci.SlusacAplikacije;

/**
 * Klasa za rad s bazom.
 *
 * @author zoran
 */
public class Baza {

    private String baza;
    private String korisnik;
    private String lozinka;
    private String driver;
    private Connection konekcija = null;

    public Baza() {
    }

    /**
     * Metoda za spajanje na bazu pomoću prosljeđenih elemenata. Vraća
     * konekciju.
     *
     * @return
     */
    public Connection spajanjeNaBazu() {
        postavke();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Baza.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nije pronađena klasa kod baze");
        }
        try {
            konekcija = DriverManager.getConnection(baza, korisnik, lozinka);
        } catch (SQLException ex) {
            Logger.getLogger(Baza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return konekcija;
    }

    /**
     * Metoda za dohvačanje potrebnih podataka iz konfiguracijske datoteke.
     */
    public void postavke() {
        BP_Konfiguracija bpKonfig = (BP_Konfiguracija) SlusacAplikacije.kontekst.getAttribute("BP_Konfiguracija");
        baza = bpKonfig.getServer_database() + bpKonfig.getUser_database();
        korisnik = bpKonfig.getUser_username();
        lozinka = bpKonfig.getUser_password();
        driver = bpKonfig.getDriver_database();
    }

    /**
     * Metoda za zatvaranje konekcije, baca SQL iznimku ako ne uspije.
     *
     * @throws SQLException
     */
    public void zatvoriKonekciju() throws SQLException {
        konekcija.close();
    }
}
