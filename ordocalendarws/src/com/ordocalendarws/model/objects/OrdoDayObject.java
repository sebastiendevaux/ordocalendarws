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
	private String _liturgy;
	
	@Persistent
	private String _class;
	
	@Persistent
	private String _color;
	
	@Persistent
	private String _epitre;
	
	@Persistent
	private String _evangile;
	
	
	public Date getDate() {
		return _date;
	}
	
	public String getLiturgy() {
		return _liturgy;
	}
	
	public String getClassOfDay() {
		return _class;
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
	 * @param liturgy Titre du jour
	 * @param day_class Classe du jour
	 * @param color Couleur liturgique
	 * @param epitre Référence du texte de l'Epitre
	 * @param evangile Référence du texte de l'Evangile
	 */
	public OrdoDayObject(String date, String liturgy, String day_class, String color, String epitre, String evangile) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			_sdate = date;
			_date = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		_liturgy = liturgy;
		_class = day_class;
		_color = color;
		_epitre = epitre;
		_evangile = evangile;
	}
	
}
