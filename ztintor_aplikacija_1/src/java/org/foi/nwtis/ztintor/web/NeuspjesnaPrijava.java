/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.web;

import javax.servlet.ServletException;

/**
 * Klasa za obradu iznimke Servleta kontroler.
 *
 * @author zoran
 */
public class NeuspjesnaPrijava extends ServletException {

    public NeuspjesnaPrijava(String message) {
        super("NWTiS: " + message);
    }
}
