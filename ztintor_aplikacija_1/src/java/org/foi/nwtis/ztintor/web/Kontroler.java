/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.ztintor.web.kontrole.Korisnik;

/**
 * Servlet za kontrolu preusjeravanja zahtjeva.
 *
 * @author zoran
 */
public class Kontroler extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String zahtjev = request.getServletPath();
        String odrediste = null;
        switch (zahtjev) {
            case "/Dokumentacija":
                odrediste = "/dokumentacija/dokumentacija.html";
                break;
            case "/PregledDnevnika":
                odrediste = "/privatno/dnevnik.jsp";
                break;
            case "/ProvjeraKorisnika":
                String korIme = request.getParameter("kor_ime");
                String lozinka = request.getParameter("lozinka");

                if (korIme == null || korIme.trim().length() == 0
                        || lozinka == null || lozinka.trim().length() == 0) {
                    throw new NeuspjesnaPrijava("Nije uneseno kor. ime ili lozinka");
                } else if (korIme.compareTo("ztintor") != 0
                        || lozinka.compareTo("123456") != 0) {
                    throw new NeuspjesnaPrijava("Pogresno kor. ime ili lozinka");
                }

                HttpSession sesija = request.getSession();
                Korisnik korisnik = new Korisnik(korIme, "Tintor", "Zoran",
                        request.getRemoteAddr(), sesija.getId(), 1);
                sesija.setAttribute("korisnik", korisnik);

                odrediste = "/privatno/index.jsp";

                break;
            case "/PrijavaKorisnika":
                odrediste = "/jsp/login.jsp";
                break;
            case "/PregledMeteoPodataka":
                odrediste = "/privatno/meteoPodaci.jsp";
                break;
            case "/PregledZahtjeva":
                odrediste = "/privatno/zahtjevi.jsp";
                break;
            case "/Index":
                odrediste = "/privatno/index.jsp";
                break;
            case "/OdjavaKorisnika":

                odrediste = "/jsp/odjava.jsp";
                break;
            default:
                ServletException up = new ServletException("Nepoznat zahtjev");
                throw up;
        }
        String kontekst = request.getContextPath();
        RequestDispatcher rd = this.getServletContext().getRequestDispatcher(odrediste);
        rd.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
