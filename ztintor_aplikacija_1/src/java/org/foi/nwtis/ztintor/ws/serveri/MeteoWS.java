/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.ws.serveri;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.ztintor.db.Baza;
import org.foi.nwtis.ztintor.web.kontrole.PodaciWS;
import org.foi.nwtis.ztintor.ws.klijenti.WeatherBugKlijent;

/**
 * Web servis za prikupljanje raznih podataka definiranih operacijama
 *
 * @author zoran
 */
@WebService(serviceName = "MeteoWS")
public class MeteoWS {

    private Connection konekcija;
    private Statement upit;
    private Baza baza;
    private String select_upit;
    private List<PodaciWS> zip_lista;

    /**
     * Web service operation Operacija za preuzimanje meteo podatka za željeni
     * zip.
     */
    @WebMethod(operationName = "dajMeteoWSPodatkeZaZip")
    public LiveWeatherData dajMeteoWSPodatkeZaZip(@WebParam(name = "zip") String zip) {
        WeatherBugKlijent wbk = new WeatherBugKlijent();
        LiveWeatherData lwd = wbk.dajMeteoPodatke(zip);
        return lwd;
    }

    private void postavke() {
        baza = new Baza();
        konekcija = baza.spajanjeNaBazu();
        try {
            upit = konekcija.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(MeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Pogreška kod kreiranja upita u MeteoWS");
        }
        zip_lista = new ArrayList<>();
    }

    /**
     * Web service operation Operacija za preuzimanje aktivnih zip kodova iz
     * baze.
     */
    @WebMethod(operationName = "dajSveZipKodove")
    public java.util.List<PodaciWS> dajSveZipKodove() {

        try {
            postavke();
            select_upit = "SELECT * FROM ztintor_activezipcodes";
            try (ResultSet rs = upit.executeQuery(select_upit)) {
                while (rs.next()) {
                    PodaciWS pws = new PodaciWS();
                    pws.setZip(rs.getString("ZIP_kodovi"));
                    zip_lista.add(pws);
                }
                rs.close();
                upit.close();
            }
            konekcija.close();
            baza.zatvoriKonekciju();
            return zip_lista;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Web service operation Operacija za preuzimanje zip kodova iz baze u
     * željenom vremenskom intevalu.
     */
    @WebMethod(operationName = "dajZipKodoveZaInterval")
    public java.util.List<PodaciWS> dajZipKodoveZaInterval(@WebParam(name = "od") String od, @WebParam(name = "doo") String doo, @WebParam(name = "zip") String zip) {

        try {
            postavke();
            select_upit = "SELECT * FROM ztintor_meteopodaci WHERE zip='" + zip + "' AND vrijeme_preuzimanja <='" + doo + "' AND vrijeme_preuzimanja >='" + od + "'";
            try (ResultSet rs = upit.executeQuery(select_upit)) {
                while (rs.next()) {
                    PodaciWS pws = new PodaciWS();
                    pws.setZip(rs.getString("zip"));
                    pws.setWbzip(rs.getString("zipWB"));
                    pws.setGrad(rs.getString("grad"));
                    pws.setTemperatura(rs.getString("temperatura"));
                    pws.setTlak(rs.getString("tlak"));
                    pws.setVlaga(rs.getString("vlaga"));
                    pws.setBrzinaVjetra(rs.getString("brzina_vjetra"));
                    pws.setSmjerVjetra(rs.getString("smjer_vjetra"));
                    pws.setDatum(rs.getString("vrijeme_preuzimanja"));
                    zip_lista.add(pws);
                }
                rs.close();
                upit.close();
            }
            konekcija.close();
            baza.zatvoriKonekciju();
            return zip_lista;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Web service operation Operacija za preuzimanje n najčešćih zip kodova iz
     * baze.
     */
    @WebMethod(operationName = "dajNajcescihNZipKodova")
    public java.util.List<PodaciWS> dajNajcescihNZipKodova(@WebParam(name = "n") String n) {

        try {
            postavke();
            select_upit = "SELECT zip, COUNT(*) AS brojac FROM ztintor_meteopodaci GROUP BY zip ORDER BY brojac DESC LIMIT " + n;
            try (ResultSet rs = upit.executeQuery(select_upit)) {
                while (rs.next()) {
                    PodaciWS pws = new PodaciWS();
                    pws.setZip(rs.getString("zip"));
                    pws.setBroj(rs.getString("brojac"));
                    zip_lista.add(pws);
                }
                rs.close();
                upit.close();
            }
            konekcija.close();
            baza.zatvoriKonekciju();
            return zip_lista;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Web service operation Operacija za preuzimanje n zadnjih odabranih zip
     * kodova iz baze.
     */
    @WebMethod(operationName = "dajZadnjihNZipKodova")
    public java.util.List<PodaciWS> dajZadnjihNZipKodova(@WebParam(name = "n") String n, @WebParam(name = "zip") String zip) {

        try {
            postavke();
            select_upit = "SELECT * FROM ztintor_meteopodaci WHERE zip ='" + zip + "' ORDER BY vrijeme_preuzimanja DESC LIMIT " + n;
            try (ResultSet rs = upit.executeQuery(select_upit)) {
                while (rs.next()) {
                    PodaciWS pws = new PodaciWS();
                    pws.setZip(rs.getString("zip"));
                    pws.setWbzip(rs.getString("zipWB"));
                    pws.setGrad(rs.getString("grad"));
                    pws.setTemperatura(rs.getString("temperatura"));
                    pws.setTlak(rs.getString("tlak"));
                    pws.setVlaga(rs.getString("vlaga"));
                    pws.setBrzinaVjetra(rs.getString("brzina_vjetra"));
                    pws.setSmjerVjetra(rs.getString("smjer_vjetra"));
                    pws.setDatum(rs.getString("vrijeme_preuzimanja"));
                    zip_lista.add(pws);
                }
                rs.close();
                upit.close();
            }
            konekcija.close();
            baza.zatvoriKonekciju();
            return zip_lista;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}