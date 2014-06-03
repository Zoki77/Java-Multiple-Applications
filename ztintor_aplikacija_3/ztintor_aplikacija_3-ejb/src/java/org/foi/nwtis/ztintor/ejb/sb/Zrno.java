/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.foi.nwtis.ztintor.jms.JmsPoruka;

/**
 * Session Bean za dohvaćanje JMS podataka.
 *
 * @author zoran
 */
@Stateless
public class Zrno {

    private static JmsPoruka mail;
    private static List<JmsPoruka> list = new ArrayList<JmsPoruka>();

    public static JmsPoruka getMail() {
        return mail;
    }

    public static void setMail(JmsPoruka mail) {
        list.add(mail);
        Zrno.mail = mail;
    }

    public static List<JmsPoruka> getList() {
        return list;
    }

    public static void setList(List<JmsPoruka> list) {
        Zrno.list = list;
    }
}
