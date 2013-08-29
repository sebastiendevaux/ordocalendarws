package com.ordocalendarws.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ordocalendarws.controler.rest.OrdoDayRessource;
import com.ordocalendarws.model.AccessLogModel;
import com.ordocalendarws.model.OrdoDayModel;
import com.ordocalendarws.model.objects.AccessLog;
import com.ordocalendarws.model.objects.OrdoDayObject;

public class RSSFeed extends ServerResource {
	
	private Document d = null;
	private static SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 	
	
	@Get("xml")
	public DomRepresentation getRSSFeed() {

		// Get the data
		OrdoDayObject od = OrdoDayModel.getDay(OrdoDayRessource.getCurrentDate());
		DomRepresentation r_rss = null;
		
		try {
			// **** 1. Logging ****
			Request request = this.getRequest();
			String referer = (request.getReferrerRef() == null) ? null : request.getReferrerRef().getIdentifier();
			String host = (request.getHostRef() == null) ? null: request.getHostRef().toString();
			AccessLog log = new AccessLog(request.getResourceRef().getPath(), referer, host, request.getClientInfo().getAddress(), request.getClientInfo().getAgent());
			AccessLogModel.storeToken(log);

			// **** 2. Build representation ****
			r_rss = new DomRepresentation(MediaType.TEXT_XML);
			r_rss.setCharacterSet(CharacterSet.UTF_8);
			d = r_rss.getDocument();
			
			// rss
			Element elt_rss = d.createElement("rss");
			elt_rss.setAttribute("version", "2.0");
			d.appendChild(elt_rss);
			
			// channel
			Element elt_channel = d.createElement("channel");
			elt_rss.appendChild(elt_channel);
			
			// title - description - lastBuildDate - language
			elt_channel.appendChild(createNode("title", "Calendrier catholique traditionnel"));
			elt_channel.appendChild(createNode("description", "Détails de la fête et des offices liturgiques dans le calendrier catholique traditionnel."));
			elt_channel.appendChild(createNode("lastBuildDate", RFC822DATEFORMAT.format(Calendar.getInstance().getTime())));
			elt_channel.appendChild(createNode("link", "https://ordocalendarws.appspot.com/"));
			elt_channel.appendChild(createNode("language", "fr-fr"));
			Element atom_link = d.createElementNS("http://www.w3.org/2005/Atom", "link");
			atom_link.setAttribute("href", "https://ordocalendarws.appspot.com/rss/");
			atom_link.setAttribute("rel", "self");
			atom_link.setAttribute("type", "application/rss+xml");
			elt_channel.appendChild(atom_link);
			
			// item
			Element elt_item = d.createElement("item");
			elt_channel.appendChild(elt_item);
			
			// title - description
			elt_item.appendChild(createNode("title", od.getFeast()));
			Element des = d.createElement("description");
			des.appendChild(d.createCDATASection(String.format("<i>%s</i><br/>%n<u>%s</u>, %s<br/>%nEpitre : %s<br/>%nEvangile : %s", OrdoDayRessource.getCurrentLongDate(),  od.getFeastClass(), od.getColor(), od.getEpitre(), od.getEvangile())));
			elt_item.appendChild(des);
			Element guid = d.createElement("guid");
			guid.setAttribute("isPermaLink", "false");
			guid.appendChild(d.createTextNode(formatter.format(od.getDate())));
			elt_item.appendChild(guid);
			d.normalizeDocument();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return r_rss;
		
	}
	
	private Element createNode(String title, String description) throws Exception {
		Element elt = null;
		if(d != null) {
			elt =  d.createElement(title);
			elt.appendChild(d.createTextNode(description));
		} else {
			throw new Exception("document null");
		}
		return elt;
	}
}
