package com.ordocalendarws.controler.cron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import au.com.bytecode.opencsv.CSVReader;

import com.ordocalendarws.controler.rest.OrdoDayRessource;
import com.ordocalendarws.model.OrdoDayModel;
import com.ordocalendarws.model.objects.OrdoDayObject;

public class CreateYear extends ServerResource {
	
	Logger logger = Logger.getLogger("com.ordo.view.rest");
	private static URL CSV_URL;
	
	@Get
	public Representation toXml() {
		String s = represent();
		return OrdoDayRessource.generateSuccessRepresentation(s);
	}
	
	private String represent() {
		String year = (String) getRequest().getAttributes().get("year");
		if(year == null) {
			return "nothing to do: no date specified!";
		} else {
			if(year.isEmpty()) return "nothing to do: no date specified!";
		}
		createOrdoYear(year);
		return "success: year created!";
	}
	
	private void createOrdoYear(String year) {
		try {
			CSV_URL = new URL("http://ordocalendarws.s3.amazonaws.com/"+year+".csv");
			ArrayList<OrdoDayObject> pd = getCSVYear();
			OrdoDayModel.insertDays(pd);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<OrdoDayObject> getCSVYear() throws IOException {
		BufferedReader in = null;
		CSVReader reader = null;
		ArrayList<OrdoDayObject> al = new ArrayList<OrdoDayObject>(366);
		try {
			in = new BufferedReader(new InputStreamReader(CSV_URL.openStream(), "UTF-8"));
			reader = new CSVReader(in, '|');
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				OrdoDayObject od = new OrdoDayObject(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
				//logger.info(nextLine[0]+";"+nextLine[1]+";"+nextLine[2]+";"+nextLine[3]+";"+nextLine[4]+";"+nextLine[5]);
				al.add(od);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) in.close();
			if(reader != null) reader.close();
		}
		return al;
	}
}
