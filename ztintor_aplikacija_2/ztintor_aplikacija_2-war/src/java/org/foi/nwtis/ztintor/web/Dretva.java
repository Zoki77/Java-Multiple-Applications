/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.ztintor.jms.JmsPoruka;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;

import org.foi.nwtis.ztintor.web.slusaci.SlusacAplikacije;

/**
 * Dretva koja provjerava pristiglu poštu u sandučiću te ju razvrstava po
 * željenim kriterijima. Također, nakon svake iteracije šalje JMS poruke.
 *
 * @author zoran
 */
public class Dretva extends Thread {

    private BP_Konfiguracija bpKonfig;
    private String emailPosluzitelj;
    private String emailKorisnik;
    private String emailLozinka;
    private String ispravniFolder;
    private String neispravniFolder;
    private int interval;
    private int ukupnoPoruka = 0;
    private int ispravnoPoruka = 0;
    private int neispravnoPoruka = 0;
    private Date pocetak;
    private Date zavrsetak;
    private long start;
    private long kraj;
    private long razlika;

    public Dretva(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
    }

    @Override
    public synchronized void start() {

        emailPosluzitelj = bpKonfig.getEmail_posluzitelj();
        emailKorisnik = bpKonfig.getEmail_korisnik();
        emailLozinka = bpKonfig.getEmail_lozinka();
        ispravniFolder = bpKonfig.getEmail_ispravni();
        neispravniFolder = bpKonfig.getEmail_neispravni();
        interval = Integer.parseInt(bpKonfig.getInterval());
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            start = new Date().getTime();
            if (SlusacAplikacije.count != 0) {
                Thread.currentThread().interrupt();
                break;
            }
            processMail();
            kraj = new Date().getTime();
            razlika = kraj - start;
            if (interval * 1000 >= razlika) {
                try {
                    this.sleep((interval * 1000) - razlika);
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException Dretva");
                }
            }
        }
    }

    @Override
    public void interrupt() {
        System.out.println("Dretva završila s radom");
        super.interrupt();

    }

    /**
     * Metoda za obradu e-maila.
     *
     * @return
     */
    public Session processMail() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;

        try {
            System.out.println("=========== Mail Dretva  ===============");
            pocetak = new Date();

            session = Session.getDefaultInstance(System.getProperties(), null);

            store = session.getStore("imap");

            store.connect(emailPosluzitelj, emailKorisnik, emailLozinka);

            // Get a handle on the default folder
            folder = store.getDefaultFolder();

            for (Folder f : folder.list()) {
            }

            // Retrieve the "Inbox"
            folder = folder.getFolder("inbox");

            //Reading the Email Index in Read / Write Mode
            folder.open(Folder.READ_WRITE);

            // Retrieve the messages
            messages = folder.getMessages();

            // Loop over all of the messages
            for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                ukupnoPoruka++;

                // Retrieve the next message to be read
                message = messages[messageNumber];

                String vrstaPoruke = message.getContentType();

                if (vrstaPoruke.equals("TEXT/PLAIN; charset=utf-8") && ispravnostPoruke(message)) {
                    ispravnoPoruka++;
                    premjestanjePoruke(ispravniFolder, store, message, folder);
                } else {
                    neispravnoPoruka++;
                    premjestanjePoruke(neispravniFolder, store, message, folder);
                }
            }

            // Close the folder
            folder.close(true);

            // Close the message store
            store.close();
            zavrsetak = new Date();
            JmsPoruka p = new JmsPoruka(pocetak, zavrsetak, ukupnoPoruka, ispravnoPoruka, neispravnoPoruka);
            sendJMSMessageToNWTiS_ztintor_1(p);

        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (FolderClosedException e) {
            e.printStackTrace();
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (ReadOnlyFolderException e) {
            e.printStackTrace();
        } catch (StoreClosedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    /**
     * Metoda za premještanje e-mail poruka u razne direktorije.
     *
     * @param nazivNovogDirektorija
     * @param store
     * @param message
     * @param folder
     * @throws MessagingException
     */
    private void premjestanjePoruke(String nazivNovogDirektorija, Store store, Message message, Folder folder) throws MessagingException {

        Folder noviFolder = store.getFolder(nazivNovogDirektorija);
        if (!noviFolder.exists()) {
            noviFolder.create(Folder.HOLDS_MESSAGES);
        }
        noviFolder.open(folder.READ_WRITE);
        Message[] zaKopiranje = new Message[1];
        zaKopiranje[0] = message;
        folder.copyMessages(zaKopiranje, noviFolder);
        noviFolder.close(false);
        message.setFlag(Flags.Flag.DELETED, true);
        System.out.println("Poruka je premjštena u mapu: " + noviFolder);

    }

    /**
     * Metoda za provjeru ispravnosti e-mail poruke. Vraća true ako je poruka
     * ispravna i false ako nije.
     *
     * @param message
     * @return
     * @throws MessagingException
     */
    private boolean ispravnostPoruke(Message message) throws MessagingException {
        if (message.getSubject().startsWith("NWTiS")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metoda za kreiranje JMS poruke.Vraća objekt potreban za slanje JMS
     * poruke.
     *
     * @param session
     * @param jmsPoruka
     * @return
     * @throws JMSException
     */
    private javax.jms.Message createJMSMessageForjmsNWTiS_ztintor_1(javax.jms.Session session, JmsPoruka jmsPoruka) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage om = session.createObjectMessage();
        om.setObject(jmsPoruka);
        return om;
    }

    /**
     * Metoda za slanje JMS poruke.
     *
     * @param jmsPoruka
     * @throws NamingException
     * @throws JMSException
     */
    private void sendJMSMessageToNWTiS_ztintor_1(JmsPoruka jmsPoruka) throws NamingException, JMSException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("java:comp/env/jms/NWTiS_QF_ztintor_1");
        Connection conn = null;
        javax.jms.Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("java:comp/env/jms/NWTiS_ztintor_1");
            MessageProducer mp = s.createProducer(destination);
            mp.send(createJMSMessageForjmsNWTiS_ztintor_1(s, jmsPoruka));
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
