/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web.slusaci;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.ztintor.konfiguracije.Konfiguracija;
import org.foi.nwtis.ztintor.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ztintor.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.web.Dretva;

/**
 * Slušač aplikacije koji pokreće i zaustavlja dretvu za e-mail.
 *
 * @author zoran
 */
@WebListener()
public class SlusacAplikacije implements ServletContextListener {

    private Dretva dretva;
    public static int count = 0;
    private Konfiguracija conf;
    private BP_Konfiguracija bpKonfig;
    public static ServletContext kontekst;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        kontekst = sce.getServletContext();
        String path = sce.getServletContext().getRealPath("WEB-INF");
        String datoteka = path + File.separator
                + sce.getServletContext().getInitParameter("konfiguracija");
        System.out.println("Datoteka konfiguracije: " + datoteka + "<br/>");
        bpKonfig = new BP_Konfiguracija(datoteka);
        System.out.println("Konfiguracija ucitana");
        sce.getServletContext().setAttribute("BP_Konfiguracija", bpKonfig);
        try {
            conf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            System.out.println("Nema konfiguracije");
        }
        dretva = new Dretva(bpKonfig);
        dretva.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dretva != null) {
            dretva.interrupt();
            dretva = null;
            count++;
        }
        System.out.println("Aplikacija završila s radom " + sce.getServletContext().getContextPath());
    }
}
