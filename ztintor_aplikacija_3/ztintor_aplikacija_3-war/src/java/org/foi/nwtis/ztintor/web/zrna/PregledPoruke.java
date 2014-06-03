/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.ztintor.ejb.sb.Zrno;
import org.foi.nwtis.ztintor.jms.JmsPoruka;

/**
 * Bean za prikaz JMS poruka.
 *
 * @author zoran
 */
@Named(value = "pregledPoruke")
@SessionScoped
public class PregledPoruke implements Serializable {

    @EJB
    private Zrno zrno;
    private List<JmsPoruka> list = new ArrayList<JmsPoruka>();
    private String porukaID;
    private JmsPoruka jms;

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
    }

    public List<JmsPoruka> getList() {
        list = Zrno.getList();
        return list;
    }

    public void setList(List<JmsPoruka> list) {
        this.list = list;
    }

    public Zrno getZrno() {
        return zrno;
    }

    public void setZrno(Zrno zrno) {
        this.zrno = zrno;
    }

    public String getPorukaID() {
        return porukaID;
    }

    public void setPorukaID(String porukaID) {
        this.porukaID = porukaID;
    }

    public JmsPoruka getJms() {
        return jms;
    }

    public void setJms(JmsPoruka jms) {
        this.jms = jms;
    }

    public String ulaz() {
        return "OK";
    }

    public String brisi() {
        for (JmsPoruka p : list) {
            if (p.getPocetak().toString().equals(porukaID)) {
                this.jms = p;
                break;
            }
        }
        Zrno.setList(list);
        return "";
    }
}
