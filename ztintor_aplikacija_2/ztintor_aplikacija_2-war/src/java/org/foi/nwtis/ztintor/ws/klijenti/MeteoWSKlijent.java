/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.ws.klijenti;

import net.wxbug.api.LiveWeatherData;

/**
 * Klasa koja sadzi pozive operacije web servisa.
 *
 * @author zoran
 */
public class MeteoWSKlijent {

    public static LiveWeatherData dajMeteoWSPodatkeZaZip(java.lang.String zip) {
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service service = new org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service();
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS port = service.getMeteoWSPort();
        return port.dajMeteoWSPodatkeZaZip(zip);
    }

    public static java.util.List<org.foi.nwtis.ztintor.ws.serveri.PodaciWS> dajSveZipKodove() {
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service service = new org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service();
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS port = service.getMeteoWSPort();
        return port.dajSveZipKodove();
    }

    public static java.util.List<org.foi.nwtis.ztintor.ws.serveri.PodaciWS> dajZipKodoveZaInterval(java.lang.String od, java.lang.String doo, java.lang.String zip) {
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service service = new org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service();
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS port = service.getMeteoWSPort();
        return port.dajZipKodoveZaInterval(od, doo, zip);
    }

    public static java.util.List<org.foi.nwtis.ztintor.ws.serveri.PodaciWS> dajNajcescihNZipKodova(java.lang.String n) {
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service service = new org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service();
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS port = service.getMeteoWSPort();
        return port.dajNajcescihNZipKodova(n);
    }

    public static java.util.List<org.foi.nwtis.ztintor.ws.serveri.PodaciWS> dajZadnjihNZipKodova(java.lang.String n, java.lang.String zip) {
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service service = new org.foi.nwtis.ztintor.ws.serveri.MeteoWS_Service();
        org.foi.nwtis.ztintor.ws.serveri.MeteoWS port = service.getMeteoWSPort();
        return port.dajZadnjihNZipKodova(n, zip);
    }
}
