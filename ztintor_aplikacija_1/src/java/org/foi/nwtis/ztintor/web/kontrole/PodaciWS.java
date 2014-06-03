/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web.kontrole;

/**
 * Klasa potrebna za spremanje podataka u obliku koji omogućava prikaz meteo
 * podataka u aplikaciji 2.
 *
 * @author zoran
 */
public class PodaciWS {

    private String zip;
    private String Wbzip;
    private String grad;
    private String temperatura;
    private String tlak;
    private String vlaga;
    private String brzinaVjetra;
    private String smjerVjetra;
    private String datum;
    private String broj;

    public PodaciWS() {
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getWbzip() {
        return Wbzip;
    }

    public void setWbzip(String Wbzip) {
        this.Wbzip = Wbzip;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getTlak() {
        return tlak;
    }

    public void setTlak(String tlak) {
        this.tlak = tlak;
    }

    public String getVlaga() {
        return vlaga;
    }

    public void setVlaga(String vlaga) {
        this.vlaga = vlaga;
    }

    public String getBrzinaVjetra() {
        return brzinaVjetra;
    }

    public void setBrzinaVjetra(String brzinaVjetra) {
        this.brzinaVjetra = brzinaVjetra;
    }

    public String getSmjerVjetra() {
        return smjerVjetra;
    }

    public void setSmjerVjetra(String smjerVjetra) {
        this.smjerVjetra = smjerVjetra;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }
}
