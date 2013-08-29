package com.ordocalendarws.model.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class OrdoDayObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @PrimaryKey
    private String _sdate;
    
    @Persistent
	private Date _date;
	
	@Persistent
	private String _feast;
	
	@Persistent
	private String _feast_class;
	
	@Persistent
	private String _color;
	
	@Persistent
	private String _epitre;
	
	@Persistent
	private String _evangile;
	
	
	public Date getDate() {
		return _date;
	}
	
	public String getFeast() {
		return _feast;
	}
	
	public String getFeastClass() {
		return _feast_class;
	}
	
	public String getColor() {
		return _color;
	}
	
	public String getEpitre() {
		return _epitre;
	}
	
	public String getEvangile() {
		return _evangile;
	}
	
	/**
	 * Constructeur de la classe
	 * @param date Date du jour
	 * @param feast Titre du jour
	 * @param feast_class Classe du jour
	 * @param color Couleur liturgique
	 * @param epitre Référence du texte de l'Epitre
	 * @param evangile Référence du texte de l'Evangile
	 */
	public OrdoDayObject(String date, String feast, String feast_class, String color, String epitre, String evangile) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			_sdate = date;
			_date = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		_feast = feast;
		_feast_class = feast_class;
		_color = color;
		_epitre = epitre;
		_evangile = evangile;
	}
	
}
