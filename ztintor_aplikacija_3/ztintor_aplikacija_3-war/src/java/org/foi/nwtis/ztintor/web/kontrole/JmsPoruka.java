/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web.kontrole;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasa za rad s JMS porukama.
 *
 * @author zoran
 */
public class JmsPoruka implements Serializable {

    private Date pocetak;
    private Date zavrsetak;
    private int ukupnoPoruka;
    private int ispravnoPoruka;
    private int neispravnoPoruka;

    public JmsPoruka(Date pocetak, Date zavrsetak, int ukupnoPoruka, int ispravnoPoruka, int neispravnoPoruka) {
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
        this.ukupnoPoruka = ukupnoPoruka;
        this.ispravnoPoruka = ispravnoPoruka;
        this.neispravnoPoruka = neispravnoPoruka;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    public Date getZavrsetak() {
        return zavrsetak;
    }

    public void setZavrsetak(Date zavrsetak) {
        this.zavrsetak = zavrsetak;
    }

    public int getUkupnoPoruka() {
        return ukupnoPoruka;
    }

    public void setUkupnoPoruka(int ukupnoPoruka) {
        this.ukupnoPoruka = ukupnoPoruka;
    }

    public int getIspravnoPoruka() {
        return ispravnoPoruka;
    }

    public void setIspravnoPoruka(int ispravnoPoruka) {
        this.ispravnoPoruka = ispravnoPoruka;
    }

    public int getNeispravnoPoruka() {
        return neispravnoPoruka;
    }

    public void setNeispravnoPoruka(int neispravnoPoruka) {
        this.neispravnoPoruka = neispravnoPoruka;
    }
}
