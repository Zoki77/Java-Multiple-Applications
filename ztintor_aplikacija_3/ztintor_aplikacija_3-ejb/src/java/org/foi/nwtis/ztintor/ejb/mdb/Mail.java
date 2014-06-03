/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ztintor.ejb.mdb;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.foi.nwtis.ztintor.ejb.sb.Zrno;
import org.foi.nwtis.ztintor.jms.JmsPoruka;

/**
 * Message driven bean koji privaća JMS poruke.
 *
 * @author zoran
 */
@MessageDriven(mappedName = "jms/NWTiS_ztintor_1", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class Mail implements MessageListener {

    public Mail() {
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage om = (ObjectMessage) message;
            try {
                System.out.println(om.getJMSMessageID());
                System.out.println(new Date(om.getJMSTimestamp()));
                JmsPoruka jms = (JmsPoruka) om.getObject();
                if (jms != null) {
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
                    Zrno.setMail(jms);
                }
            } catch (JMSException ex) {
                Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
