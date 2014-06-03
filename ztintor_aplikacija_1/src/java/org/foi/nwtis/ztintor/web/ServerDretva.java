/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.ztintor.db.Baza;
import org.foi.nwtis.ztintor.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ztintor.ws.klijenti.WeatherBugKlijent;

/**
 * Primitivni posljužitelj za upravljanje pozadinskom dretvom.
 *
 * @author zoran
 */
public class ServerDretva extends Thread {

    private BP_Konfiguracija bpKonfig;
    private Socket socket;
    private ServerSocket server;
    private String stringBuilder;
    private String korisnik;
    private String lozinka;
    private Pattern pattern;
    private Matcher m;
    private boolean status;
    private Connection konekcija;
    private Statement upit;
    private Statement upit_insert;
    private Baza baza;
    private String select_upit;
    private String insert_upit;
    private String poruka;
    private String izvrsavanje;
    private int zipKod;
    private int primljene = 0;
    private int neispravne = 0;
    private int ispravne = 0;
    private String odgovor = "";
    private String uspjeh = "";
    private boolean pauza = false;
    private boolean start = true;
    private boolean kraj = false;
    private boolean provjera;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
    private long pocetakStanja;
    private long trajanjeStanja;

    public ServerDretva(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
        pocetakStanja = new Date().getTime();
    }

    @Override
    public synchronized void start() {
        korisnik = bpKonfig.getProvjera_korisnika();
        lozinka = bpKonfig.getProvjera_lozinke();
        super.start();
    }

    @Override
    public void run() {
        try {
            int port = Integer.parseInt(bpKonfig.getPort());
            server = new ServerSocket(port);
            while (!kraj) {
                System.out.println("***********  Čekam ****************");
                socket = server.accept();
                zahtjevi(socket);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerDretva.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Greška priradu s serverom");
        }

    }

    @Override
    public void interrupt() {
        super.interrupt();
        System.out.println("ServrDretva je zaustavljena!!!!!!");
    }

    /**
     * Metoda servera koja za ulazni argument ima socket. Funkcija obrađuje
     * zahtjeve koji stižu te na temelju njih vrši određene akcije kao što su
     * pauziranje preuzimanja meteo podataka, dohvaćanje podataka za zip kodove,
     * te njihovo dodavanje u bazu i testiranje.
     *
     * @param socket
     * @throws IOException
     */
    public void zahtjevi(Socket socket) throws IOException {
        InputStream input;
        OutputStream output;
        input = socket.getInputStream();
        output = socket.getOutputStream();
        StringBuilder builder = new StringBuilder();
        while (true) {
            int i = input.read();
            if (i == -1) {
                break;
            }
            builder.append((char) i);
        }
        stringBuilder = builder.toString().trim();
        izvrsavanje = sdf.format(new Date().getTime());
        primljene++;
        if (builder.indexOf("USER") != -1 && builder.indexOf("PASSWD") != -1) {
            String sintaksa = "^USER ([a-z0-9_-]{2,20}); PASSWD ([a-zA-Z0-9_-]{5,20});( (PAUSE|START|STOP|((ADD ZIP|TEST ZIP) ([0-9]{5})));)?$";
            pattern = Pattern.compile(sintaksa);
            m = pattern.matcher(stringBuilder);
            status = m.matches();
            if (status) {
                ispravne++;
                korisnik = m.group(1);
                lozinka = m.group(2);
                if (provjeraKorisnika(korisnik, lozinka)) {
                    if (builder.indexOf("PAUSE") != -1) {
                        if (pauza == false) {
                            WbDretva.pauza = true;
                            pauza = true;
                            start = false;
                            uspjeh = "true";
                            odgovor = "OK 10";
                            System.out.println("Pauzirano preuzimanje meteo podataka.");
                        } else {
                            uspjeh = "false";
                            odgovor = "OK 40";
                            System.out.println("Dretva je već pauzirana.");
                        }
                    } else if (builder.indexOf("START") != -1) {
                        if (start == false) {
                            WbDretva.pauza = false;
                            start = true;
                            pauza = false;
                            uspjeh = "true";
                            odgovor = "OK 10";
                            System.out.println("Startano preuzimanje meteo podataka.");
                        } else {
                            uspjeh = "false";
                            odgovor = "OK 41";
                            System.out.println("Dretva već radi");
                        }
                    } else if (builder.indexOf("STOP") != -1) {
                        if (kraj == false) {
                            WbDretva.pauza = true;
                            kraj = true;
                            uspjeh = "true";
                            odgovor = "OK 10";
                            System.out.println("Server zaustavljen");
                        } else {
                            uspjeh = "false";
                            odgovor = "OK 42";
                            System.out.println("Server već zaustavljen");
                        }
                    } else if (builder.indexOf("ADD ZIP") != -1) {
                        zipKod = Integer.parseInt(m.group(7));
                        try {
                            baza = new Baza();
                            konekcija = baza.spajanjeNaBazu();
                            upit = konekcija.createStatement();
                            select_upit = "SELECT ZIP_kodovi FROM ztintor_activezipcodes WHERE ZIP_kodovi='" + zipKod + "'";
                            ResultSet rs = upit.executeQuery(select_upit);
                            if (rs.next()) {
                                uspjeh = "false";
                                odgovor = "OK 42";
                                System.out.println("Zip kod već postoji u bazi ");
                            } else {
                                upit_insert = konekcija.createStatement();
                                insert_upit = "INSERT INTO  ztintor_activezipcodes (ZIP_kodovi, ime_korisnika) VALUES('" + zipKod + "','zoran')";
                                upit_insert.execute(insert_upit);
                                uspjeh = "true";
                                odgovor = "OK 10";
                                System.out.println("Novi zip kod dodan je u bazu ");
                            }
                            rs.close();
                            upit.close();
                            upit_insert.close();
                            konekcija.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(WbDretva.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (builder.indexOf("TEST ZIP") != -1) {
                        zipKod = Integer.parseInt(m.group(7));
                        try {
                            baza = new Baza();
                            konekcija = baza.spajanjeNaBazu();
                            upit = konekcija.createStatement();
                            select_upit = "SELECT ZIP_kodovi FROM ztintor_activezipcodes WHERE ZIP_kodovi='" + zipKod + "'";
                            ResultSet rs = upit.executeQuery(select_upit);
                            uspjeh = "true";
                            if (rs.next()) {
                                {
                                    odgovor = "OK 10";
                                    System.out.println("Zip kod postoji u evidenciji");
                                }
                            } else {
                                odgovor = "OK 44";
                                System.out.println("Zip kod  NE postoji u evidenciji");
                            }
                            rs.close();
                            upit.close();
                            konekcija.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(WbDretva.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        neispravne++;
                        ispravne--;
                        uspjeh = "false";
                        odgovor = "OK 43";
                        System.out.println("Neisprava komanda.");
                    }
                } else {
                    uspjeh = "true";
                    odgovor = "OK 30";
                    System.out.println("Pogrešno korisničko ime ili lozinka.");
                }
            } else {
                neispravne++;
                uspjeh = "false";
                odgovor = "OK 43";
                System.out.println("Neisprava komanda.");
            }
        } else if (builder.indexOf("USER") != -1 && builder.indexOf("GET ZIP") != -1) {
            String sintaksa = "^USER [a-z0-9_-]{5,20}; GET ZIP ([0-9]{5});$";
            pattern = Pattern.compile(sintaksa);
            m = pattern.matcher(stringBuilder);
            status = m.matches();
            if (status) {
                ispravne++;
                try {
                    baza = new Baza();
                    konekcija = baza.spajanjeNaBazu();
                    upit = konekcija.createStatement();
                    select_upit = "SELECT * FROM ztintor_activezipcodes WHERE ZIP_kodovi='" + m.group(1) + "'";
                    ResultSet rs = upit.executeQuery(select_upit);
                    String zipKod;
                    if (rs.next()) {
                        zipKod = rs.getString("ZIP_kodovi");
                        WeatherBugKlijent wbk = new WeatherBugKlijent();
                        LiveWeatherData lwd = wbk.dajMeteoPodatke(zipKod);
                        String temp = "";
                        String vlaga;
                        String tlak;
                        String geosir;
                        String geoduz;
                        String vrijednost = lwd.getTemperature();
                        if (Double.parseDouble(vrijednost) < -9) {
                            temp = vrijednost.substring(0, 4) + "0";
                        } else if (Double.parseDouble(vrijednost) > -9 && Double.parseDouble(vrijednost) < 0) {
                            temp = "-0" + vrijednost.substring(1, 3) + "0";
                        } else if (Double.parseDouble(vrijednost) > 0 && Double.parseDouble(vrijednost) < 9) {
                            temp = "0" + vrijednost.substring(0, 2) + "0";
                        } else if (Double.parseDouble(vrijednost) > 9) {
                            temp = vrijednost.substring(0, 3) + "0";
                        }
                        vlaga = lwd.getHumidity() + ".00";
                        tlak = (Double.parseDouble(vrijednost) < 1000) ? lwd.getPressure() : "0" + lwd.getPressure();
                        geosir = "0" + Double.toString(lwd.getLatitude()).substring(0, 9);
                        geoduz = (lwd.getLongitude() < -100) ? Double.toString(lwd.getLongitude()).substring(0, 8)
                                : ("-0" + Double.toString(lwd.getLongitude()).substring(1, 9));
                        uspjeh = "true";
                        odgovor = "OK 10 " + "TEMP " + temp
                                + " VLAGA " + vlaga + " TLAK " + tlak
                                + " GEOSIR " + geosir + " GEODUZ " + geoduz;
                    } else {
                        uspjeh = "true";
                        odgovor = "OK 43";
                    }
                    rs.close();
                    upit.close();
                    konekcija.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WbDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                neispravne++;
                uspjeh = "false";
                odgovor = "OK 43";
                System.out.println("Neisprava komanda.");
            }
        } else {
            neispravne++;
            uspjeh = "false";
            odgovor = "OK 43";
            System.out.println("Neisprava komanda.");
        }
        try {
            output.write(odgovor.getBytes());
            output.flush();
            output.close();
            trajanjeStanja = new Date().getTime() - pocetakStanja;
            pocetakStanja = new Date().getTime();
            poruka = "Vrijeme izvršavanja: " + izvrsavanje
                    + ", Trajanje prethodnog stanja: " + trajanjeStanja
                    + ", Primljene komande: " + primljene
                    + ", Neispravne komande " + neispravne
                    + ", Ispravne komande: " + ispravne;
            System.out.println(poruka);
            sendMail(poruka);

        } catch (IOException ex) {
            Logger.getLogger(ServerDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        upisiUDnevnik(stringBuilder, sdf.format(new Date().getTime()));
        input.close();
    }

    /**
     * Metoda pomoću koje se vrši provjera korisnika. Vraća true ako se korisnik
     * nalazi u bazi, a false ako se korisnik ne nalazi u bazi.
     *
     * @param user
     * @param passwd
     * @return
     */
    private boolean provjeraKorisnika(String user, String passwd) {
        try {
            baza = new Baza();
            konekcija = baza.spajanjeNaBazu();
            upit = konekcija.createStatement();
            select_upit = "SELECT username,password FROM ztintor_users where " + "username=" + "'" + user + "' AND password=" + "'" + passwd + "'";
            ResultSet rs = upit.executeQuery(select_upit);
            provjera = false;
            if (rs.next()) {
                provjera = true;
            }
            rs.close();
            upit.close();
            konekcija.close();
        } catch (SQLException ex) {
            Logger.getLogger(WbDretva.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return provjera;
    }

    /**
     * Metoda pomoću koje se vrši upis podataka o zahtjevima u bazu.
     *
     * @param komanda
     * @param vrijeme
     */
    private void upisiUDnevnik(String komanda, String vrijeme) {
        try {
            baza = new Baza();
            konekcija = baza.spajanjeNaBazu();
            upit = konekcija.createStatement();
            select_upit = "SELECT * FROM ztintor_zahtjevi";
            ResultSet rs = upit.executeQuery(select_upit);
            int brojac = 0;
            while (rs.next()) {
                brojac++;
            }
            brojac++;
            upit_insert = konekcija.createStatement();
            insert_upit = "INSERT INTO ztintor_zahtjevi (id, datum, komanda, uspjeh) VALUES('" + brojac + "','" + vrijeme + "','" + komanda + "','" + uspjeh + "')";
            upit_insert.execute(insert_upit);

            rs.close();
            upit.close();
            upit_insert.close();
            konekcija.close();
        } catch (SQLException ex) {
            Logger.getLogger(WbDretva.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za slanje mail poruke.
     *
     * @param poruka
     */
    public void sendMail(String poruka) {
        try {
            String primatelj = bpKonfig.getPrimatelj();
            String posiljatelj = bpKonfig.getPosiljatelj();
            String naslov = bpKonfig.getPredmet();
            String smtp_server = bpKonfig.getSmtp_server();
            String smtp_port = bpKonfig.getSmtp_port();

            System.out.println(primatelj + "  " + posiljatelj + "  " + naslov + "  " + smtp_server + "  " + smtp_port);

            InternetAddress to = new InternetAddress(primatelj);
            InternetAddress from = new InternetAddress(posiljatelj);
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", smtp_server);
            properties.setProperty("mail.smtp.port", smtp_port);
            Session session = Session.getDefaultInstance(properties);

            MimeMessage message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setSender(from);
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(naslov);
            message.setContent(poruka, "text/plain;charset=utf-8");

            Transport.send(message);
        } catch (MessagingException ex) {
            System.out.println("Mail nije poslan");
        }
    }
}
