/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.NoSuchProviderException;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.web.slusaci.SlusacAplikacije;

/**
 * Bean za spajanje na mail server.
 *
 * @author zoran
 */
@ManagedBean
@SessionScoped
public class AdminPanel {

    private BP_Konfiguracija bpKonfig;
    private String emailPosluzitelj;
    private String emailKorisnik;
    private String emailLozinka;
    private Boolean uspjesnostAutentifikacije;

    /**
     * Creates a new instance of AdminPanel
     */
    public AdminPanel() {
        bpKonfig = (BP_Konfiguracija) SlusacAplikacije.kontekst.getAttribute("BP_Konfiguracija");
    }

    /**
     * Metoda koja se poziva pritiskom na gumb i preusmjerava na temelju
     * povratne vrijednosti na željenu stranicu. Provjerava da li su podaci za
     * spajanje na mail server točni.
     *
     * @return
     */
    public Boolean citajPoruke() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        emailPosluzitelj = bpKonfig.getEmail_posluzitelj();
        emailKorisnik = bpKonfig.getEmail_korisnik();
        emailLozinka = bpKonfig.getEmail_lozinka();

        session = Session.getDefaultInstance(System.getProperties(), null);
        try {
            store = session.getStore("imap");
            store.connect(this.getEmailPosluzitelj(), this.getEmailKorisnik(), this.getEmailLozinka());
            uspjesnostAutentifikacije = true;
            return uspjesnostAutentifikacije;

        } catch (AuthenticationFailedException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (FolderClosedException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (FolderNotFoundException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (NoSuchProviderException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (ReadOnlyFolderException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (StoreClosedException e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        } catch (Exception e) {
            uspjesnostAutentifikacije = false;
            return uspjesnostAutentifikacije;
        }
    }

    public String getEmailPosluzitelj() {
        return emailPosluzitelj;
    }

    public void setEmailPosluzitelj(String emailPosluzitelj) {
        this.emailPosluzitelj = emailPosluzitelj;
    }

    public String getEmailKorisnik() {
        return emailKorisnik;
    }

    public void setEmailKorisnik(String emailKorisnik) {
        this.emailKorisnik = emailKorisnik;
    }

    public String getEmailLozinka() {
        return emailLozinka;
    }

    public void setEmailLozinka(String emailLozinka) {
        this.emailLozinka = emailLozinka;
    }

    public Boolean getUspjesnostAutentifikacije() {
        return uspjesnostAutentifikacije;
    }

    public void setUspjesnostAutentifikacije(Boolean uspjesnostAutentifikacije) {
        this.uspjesnostAutentifikacije = uspjesnostAutentifikacije;
    }
}
