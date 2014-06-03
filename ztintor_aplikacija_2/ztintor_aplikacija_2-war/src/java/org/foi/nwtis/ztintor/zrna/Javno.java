/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.ztintor.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.ztintor.ws.serveri.PodaciWS;

/**
 * Zrno za obradu podataka koji se prikazuju u javnom dijelu druge aplikacije.
 *
 * @author zoran
 */
@ManagedBean
@RequestScoped
public class Javno {

    private String zip;
    private String zadnji;
    private String najcesci;
    private String od;
    private String doo;
    private List<PodaciWS> lista = new ArrayList();
    private LiveWeatherData meteoPodatak;
    private static int pocetakZadnji = 0;
    private static int rasponZadnji = 5;
    private static int pocetniRasponZadnji = 5;
    private static int pocetakSvi = 0;
    private static int rasponSvi = 5;
    private static int pocetniRasponSvi = 5;
    private static int pocetakNaj = 0;
    private static int rasponNaj = 5;
    private static int pocetniRasponNaj = 5;
    private static int pocetakInterval = 0;
    private static int rasponInterval = 5;
    private static int pocetniRasponInterval = 5;

    /**
     * Creates a new instance of Javno
     */
    public Javno() {
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public LiveWeatherData getMeteoPodatak() {
        return meteoPodatak;
    }

    public void setMeteoPodatak(LiveWeatherData meteoPodatak) {
        this.meteoPodatak = meteoPodatak;
    }

    public String getZadnji() {
        return zadnji;
    }

    public void setZadnji(String zadnji) {
        this.zadnji = zadnji;
    }

    public List<PodaciWS> getLista() {
        return lista;
    }

    public void setLista(List<PodaciWS> lista) {
        this.lista = lista;
    }

    public String getNajcesci() {
        return najcesci;
    }

    public void setNajcesci(String najcesci) {
        this.najcesci = najcesci;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getDoo() {
        return doo;
    }

    public void setDoo(String doo) {
        this.doo = doo;
    }

    public int getPocetakZadnji() {
        return pocetakZadnji;
    }

    public int getRasponZadnji() {
        return rasponZadnji;
    }

    public int getPocetniRasponZadnji() {
        return pocetniRasponZadnji;
    }

    public int getPocetakSvi() {
        return pocetakSvi;
    }

    public int getRasponSvi() {
        return rasponSvi;
    }

    public int getPocetniRasponSvi() {
        return pocetniRasponSvi;
    }

    public int getPocetakNaj() {
        return pocetakNaj;
    }

    public int getRasponNaj() {
        return rasponNaj;
    }

    public int getPocetniRasponNaj() {
        return pocetniRasponNaj;
    }

    public int getPocetakInterval() {
        return pocetakInterval;
    }

    public int getRasponInterval() {
        return rasponInterval;
    }

    public int getPocetniRasponInterval() {
        return pocetniRasponInterval;
    }

    /**
     * Metoda za dohvaćanje meteo podataka pomoću WeatherBug servisa..
     *
     * @return
     */
    public String dajMeteoWSPodatke() {
        meteoPodatak = MeteoWSKlijent.dajMeteoWSPodatkeZaZip(zip);
        return "";
    }

    /**
     * Metoda za dohvaćanje zadnjih podataka iz baze.
     *
     * @return
     */
    public String dajMeteoWSPodatkeZaZadnje() {
        lista = MeteoWSKlijent.dajZadnjihNZipKodova(zadnji, zip);
        return "";
    }

    /**
     * Metoda za dohvaćanje svih zip kodova iz baze..
     *
     * @return
     */
    public String dajSveZipKodove() {
        lista = MeteoWSKlijent.dajSveZipKodove();
        return "";
    }

    /**
     * Metoda za dohvaćanje najčešćih zip kodova iz baze.
     *
     * @return
     */
    public String dajNajcescihN() {
        lista = MeteoWSKlijent.dajNajcescihNZipKodova(najcesci);
        return "";
    }

    /**
     * Metoda za dohvaćanje podataka dodanih u određenom intervalu u bazu.
     *
     * @return
     */
    public String dajZaInterval() {
        lista = MeteoWSKlijent.dajZipKodoveZaInterval(od, doo, zip);
        return "";
    }

    /**
     * Mrtoda za prikaz prethodnih podataka određenog upita.
     */
    public void prethodniZadnji() {
        dajMeteoWSPodatkeZaZadnje();
        if (rasponZadnji != pocetniRasponZadnji) {
            rasponZadnji = pocetniRasponZadnji;
        }
        if (pocetakZadnji >= rasponZadnji) {
            pocetakZadnji = pocetakZadnji - rasponZadnji;
        }
    }

    /**
     * Mrtoda za prikaz sljedećih podataka određenog upita.
     */
    public void sljedeciZadnji() {
        dajMeteoWSPodatkeZaZadnje();
        if (pocetakZadnji < lista.size() - rasponZadnji) {
            pocetakZadnji = pocetakZadnji + rasponZadnji;

        } else if ((lista.size() - pocetakZadnji - 1) != rasponZadnji && lista.size() > pocetniRasponZadnji) {
            rasponZadnji = lista.size() - pocetakZadnji - 1;
        }
    }

    /**
     * Mrtoda za prikaz prethodnih podataka određenog upita.
     */
    public void prethodniSvi() {
        dajSveZipKodove();
        if (rasponSvi != pocetniRasponSvi) {
            rasponSvi = pocetniRasponSvi;
        }
        if (pocetakSvi >= rasponSvi) {
            pocetakSvi = pocetakSvi - rasponSvi;
        }
    }

    /**
     * Mrtoda za prikaz sljedećih podataka određenog upita.
     */
    public void sljedeciSvi() {
        dajSveZipKodove();
        if (pocetakSvi < lista.size() - rasponSvi) {
            pocetakSvi = pocetakSvi + rasponSvi;

        } else if ((lista.size() - pocetakSvi - 1) != rasponSvi && lista.size() > pocetniRasponSvi) {
            rasponSvi = lista.size() - pocetakSvi - 1;
        }
    }

    /**
     * Mrtoda za prikaz prethodnih podataka određenog upita.
     */
    public void prethodniNaj() {
        dajNajcescihN();
        if (rasponNaj != pocetniRasponNaj) {
            rasponNaj = pocetniRasponNaj;
        }
        if (pocetakNaj >= rasponNaj) {
            pocetakNaj = pocetakNaj - rasponNaj;
        }
    }

    /**
     * Mrtoda za prikaz sljedećih podataka određenog upita.
     */
    public void sljedeciNaj() {
        dajNajcescihN();
        if (pocetakNaj < lista.size() - rasponNaj) {
            pocetakNaj = pocetakNaj + rasponNaj;

        } else if ((lista.size() - pocetakNaj - 1) != rasponNaj && lista.size() > pocetniRasponNaj) {
            rasponNaj = lista.size() - pocetakNaj - 1;
        }
    }

    /**
     * Mrtoda za prikaz prethodnih podataka određenog upita.
     */
    public void prethodniInterval() {
        dajZaInterval();
        if (rasponInterval != pocetniRasponInterval) {
            rasponInterval = pocetniRasponInterval;
        }
        if (pocetakInterval >= rasponInterval) {
            pocetakInterval = pocetakInterval - rasponInterval;
        }
    }

    /**
     * Mrtoda za prikaz sljedećih podataka određenog upita.
     */
    public void sljedeciInterval() {
        dajZaInterval();
        if (pocetakInterval < lista.size() - rasponInterval) {
            pocetakInterval = pocetakInterval + rasponInterval;

        } else if ((lista.size() - pocetakInterval - 1) != rasponInterval && lista.size() > pocetniRasponInterval) {
            rasponInterval = lista.size() - pocetakInterval - 1;
        }
    }
}
