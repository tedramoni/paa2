package rest;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import ejb.entity.*;
import ejb.service.IMessagerie;

@Path("/compte")
public class RessourceMessage {
	// EJB
	private static IMessagerie messagerie;

	public RessourceMessage() {
		try {
			//CONNEXION GLASSFISH
			Properties properties = new Properties();
			properties.put("java.naming.factory.initial","com.sun.enterprise.naming.SerialInitContextFactory");
			properties.put("java.naming.factory.url.pkgs","com.sun.enterprise.naming");
			properties.put("java.naming.factory.state","com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			properties.put("org.omg.CORBA.ORBInitialHost", "localhost");
			properties.put("org.omg.CORBA.ORBInitialPort","3700");
			//LOOKUP EJB
			RessourceMessage.setMessagerie((IMessagerie) new InitialContext(properties).lookup("ejb/messagerie"));
		} catch (NamingException e) {e.printStackTrace();}
	}

	@POST
	@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void creerCompte(Compte c){
		try {
			RessourceMessage.getMessagerie().creerCompte(c.getLogin(), c.getNom(), c.getMotDePasse(), c.getDateNaissance(), c.getDateInscription);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@GET
	@Path("/{login}")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Compte consulterCompte(@PathParam("login") String login) {
		Compte c = null;
		try {
			c = RessourceMessage.getMessagerie().consulterCompte(login);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	@POST
	@Path("/{login}/msg")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void envoyerMessage(@PathParam("login") String login, Message m) throws Exception {
		RessourceMessage.getMessagerie().envoyerMessage(m.getObjet(), m.getCorps(), login, m.getDestinataire().getLogin());
	}
	
	@GET
	@Path("/{login}/msg")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void envoyerMessage(@PathParam("login") String login, Message m) throws Exception {
		RessourceMessage.getMessagerie().recupererMessage();
	}
	
	@DELETE
	@Path("/{login}/msg")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void envoyerMessage(@PathParam("login") String login, Message m) throws Exception {
		RessourceMessage.getMessagerie().suprimmerMessageLus(login);
	}
	
	/*@GET
	@Path("/{login}/msg/lu")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void envoyerMessage(Message m) throws Exception {
		RessourceMessage.getMessagerie().getReadMessages();
	}
	
	@GET
	@Path("/{login}/msg/nonlu")
	@Produces ({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void envoyerMessage(Message m) throws Exception {
		RessourceMessage.getMessagerie().getUnreadMessages();
	}*/
	
	
	
	public static IMessagerie getMessagerie() {
		return messagerie;
	}

	public static void setMessagerie(IMessagerie messagerie) {
		RessourceMessage.messagerie = messagerie;
	}
	
}