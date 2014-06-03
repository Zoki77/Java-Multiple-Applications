/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.NoSuchProviderException;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import org.foi.nwtis.ztintor.konfiguracije.Konfiguracija;

/**
 * Bean za odabir željenog foldera.
 *
 * @author zoran
 */
@ManagedBean
@SessionScoped
public class OdabirFoldera {

    private String emailPosluzitelj;
    private String emailKorisnik;
    private String emailLozinka;
    private List<String> fld = new ArrayList<String>();
    private String odabraniFolder = "inbox";
    private int pocetak;
    private int kraj;
    private int broj;
    private Konfiguracija konfig;

    /**
     * Creates a new instance of OdabirFoldera
     */
    public OdabirFoldera() {
        broj = 9;
    }

    public String getEmailPosluzitelj() {
        AdminPanel ep = (AdminPanel) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminPanel");
        this.emailPosluzitelj = ep.getEmailPosluzitelj();
        return emailPosluzitelj;
    }

    public void setEmailPosluzitelj(String emailPosluzitelj) {
        this.emailPosluzitelj = emailPosluzitelj;
    }

    public String getEmailKorisnik() {
        AdminPanel ep = (AdminPanel) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminPanel");
        this.emailKorisnik = ep.getEmailKorisnik();
        return emailKorisnik;
    }

    public void setEmailKorisnik(String emailKorisnik) {
        this.emailKorisnik = emailKorisnik;
    }

    public String getEmailLozinka() {
        AdminPanel ep = (AdminPanel) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminPanel");
        this.emailLozinka = ep.getEmailLozinka();
        return emailLozinka;
    }

    public void setEmailLozinka(String emailLozinka) {
        this.emailLozinka = emailLozinka;
    }

    public List<String> getFld() {
        return fld;
    }

    public void setFld(List<String> fld) {
        this.fld = fld;
    }

    public String getOdabraniFolder() {
        return odabraniFolder;
    }

    public void setOdabraniFolder(String odabraniFolder) {
        this.odabraniFolder = odabraniFolder;
    }

    public int getPocetak() {
        return pocetak;
    }

    public void setPocetak(int pocetak) {
        this.pocetak = pocetak;
    }

    public int getKraj() {
        return kraj;
    }

    public void setKraj(int kraj) {
        this.kraj = kraj;
    }

    /**
     * Metoda koja se poziva pritiskom na određeni gumb i preusmjerava na
     * temelju povratne vrijednosti na željenu stranicu.
     *
     * @return
     */
    public String saljiOdabir() {
        PregledSvihPoruka.promjeniPocetnoStanje();
        return "OK";

    }

    public void dohvatiZaIspis() {
        dohvatiPoruke();
    }

    /**
     * Metoda koja dohvaća poruke iz odabrane mape.
     */
    private void dohvatiPoruke() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        try {
            session = Session.getDefaultInstance(System.getProperties(), null);
            store = session.getStore("imap");
            store.connect(this.getEmailPosluzitelj(), this.getEmailKorisnik(), this.getEmailLozinka());
            // Get a handle on the default folder
            folder = store.getDefaultFolder();
            fld = new ArrayList<String>();
            System.out.println("************Lista foldera*****************:");
            for (Folder f : folder.list()) {
                fld.add(f.getName());
                System.out.println(f.getName());
            }
            // Close the message store
            store.close();
        } catch (AuthenticationFailedException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (FolderClosedException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (FolderNotFoundException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (NoSuchProviderException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (ReadOnlyFolderException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (StoreClosedException e) {
            System.out.println("Not able to process the mail reading.");
        } catch (Exception e) {
            System.out.println("Not able to process the mail reading.");
        }
    }
}
