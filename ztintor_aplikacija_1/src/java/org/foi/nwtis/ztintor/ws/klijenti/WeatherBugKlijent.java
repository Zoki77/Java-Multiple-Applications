/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.ws.klijenti;

import net.wxbug.api.LiveWeatherData;
import net.wxbug.api.UnitType;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.web.slusaci.SlusacAplikacije;

/**
 * Klasa za preuzimanje podataka s Weather Bug-a.
 *
 * @author zoran
 */
public class WeatherBugKlijent {

    private String wbk;
    BP_Konfiguracija bpKonfig = (BP_Konfiguracija) SlusacAplikacije.kontekst.getAttribute("BP_Konfiguracija");

    /**
     * Metoda za dohvaćanje podataka za željeni zip kod. Vraća te podatke pomoću
     * objekta klase LiveWeatherData.
     *
     * @param zip
     * @return
     */
    public LiveWeatherData dajMeteoPodatke(String zip) {
        wbk = bpKonfig.getWbKod();
        return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, wbk);
    }

    private static LiveWeatherData getLiveWeatherByUSZipCode(java.lang.String zipCode, net.wxbug.api.UnitType unittype, java.lang.String aCode) {
        net.wxbug.api.WeatherBugWebServices service = new net.wxbug.api.WeatherBugWebServices();
        net.wxbug.api.WeatherBugWebServicesSoap port = service.getWeatherBugWebServicesSoap();
        return port.getLiveWeatherByUSZipCode(zipCode, unittype, aCode);
    }
}
