package com.ordocalendarws.controler.rest;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;  
import org.restlet.resource.ServerResource;

import org.w3c.dom.Document;  
import org.w3c.dom.Element; 

import com.ordocalendarws.model.OrdoDayModel;
import com.ordocalendarws.model.objects.OrdoDayObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
//import java.util.logging.Logger;


public class OrdoDayRessource extends ServerResource {
	//Logger logger = Logger.getLogger("com.ordo.view.rest");

	@Get("xml")
	public DomRepresentation toXml() {
		//logger.info("toXml");
		String date = (String) getRequest().getAttributes().get("date");
		//logger.info("parametre date : " + date);
		DomRepresentation r = null;
		
		if(date != null) {
			if(date.isEmpty()) {
				r = getXMLDay(getCurrentDate());
			} else {
				r = getXMLDay(date);
			}
		} else {
			r = getXMLDay(getCurrentDate());
		}
		
		return r;
	}
	
	/**
	 * Return the date of today
	 * @return Date of today
	 */
	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
		return df.format(c.getTime());
	}
	
	/**
	 * Return the date of today in full format
	 * @return Date of today in full format
	 */
	public static String getCurrentLongDate() {
		DateFormat df_fr = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
		return df_fr.format(Calendar.getInstance().getTime());
	}

	
	/**
	 * Generate the XML representation for the specified day
	 * @param date The day to represent
	 * @return XML representation
	 */
	private DomRepresentation getXMLDay(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		//logger.info("getXMLDay -- date : " + date);		
		
		try {
			// Generate the right representation according to its media type.
			DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
			representation.setCharacterSet(CharacterSet.UTF_8);
			
			// Get the data
			OrdoDayObject od = OrdoDayModel.getDay(date);
			
			if(od == null) {
				return generateErrorRepresentation("date pas trouvée:"+date+" !");
			}
			
			// Generate a DOM document
			Document d = representation.getDocument(); 
			Element r = d.createElement("jour");
			
			// Date
			r.setAttribute("date", formatter.format(od.getDate()));
			d.appendChild(r);
			
			// Titre du jour
			Element eltFeast = d.createElement("liturgie");
			eltFeast.appendChild(d.createTextNode(od.getLiturgy()));
			r.appendChild(eltFeast);
			
			// Classe
			Element eltClass = d.createElement("classe");
			eltClass.appendChild(d.createTextNode(od.getClassOfDay()));
			r.appendChild(eltClass);
			
			// Couleur
			Element eltColor = d.createElement("couleur");
			eltColor.appendChild(d.createTextNode(od.getColor()));
			r.appendChild(eltColor);
			
			// Epitre
			Element eltEpitre = d.createElement("epitre");
			eltEpitre.appendChild(d.createTextNode(od.getEpitre()));
			r.appendChild(eltEpitre);

			// Evangile
			Element eltEvangile = d.createElement("evangile");
			eltEvangile.appendChild(d.createTextNode(od.getEvangile()));
			r.appendChild(eltEvangile);
			
			d.normalizeDocument();  
			// Returns the XML representation of this document.  
			return representation; 
			
		} catch (IOException e) {  
			e.printStackTrace();  
		}
		
		return null;
	}
	
	/** 
	 * Generate an XML representation of an error response. 
	 *  
	 * @param errorMessage the error message.  
	 */  
	private DomRepresentation generateErrorRepresentation(String errorMessage) {  
		DomRepresentation result = null;  
		// This is an error  
		// Generate the output representation  
		try {  
			result = new DomRepresentation(MediaType.TEXT_XML);  
			// Generate a DOM document representing the list of  items.  
			Document d = result.getDocument();  

			Element eltError = d.createElement("error");
			d.appendChild(eltError);

			Element eltMessage = d.createElement("message");  
			eltMessage.appendChild(d.createTextNode(errorMessage));  
			eltError.appendChild(eltMessage);  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  

		return result;  
	}
	
	/** 
	 * Generate an XML representation of an successful response. 
	 *  
	 * @param Message the success message.  
	 */  
	static public Representation generateSuccessRepresentation(String Message) {  
		DomRepresentation result = null;  
		try {  
			result = new DomRepresentation(MediaType.TEXT_XML);  
			result.setCharacterSet(CharacterSet.UTF_8);
			// Generate a DOM document representing the list of  items.  
			Document d = result.getDocument();  

			Element eltError = d.createElement("success");
			d.appendChild(eltError);

			Element eltMessage = d.createElement("message");  
			eltMessage.appendChild(d.createTextNode(Message));  
			eltError.appendChild(eltMessage);  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  

		return result;  
	}  
}
