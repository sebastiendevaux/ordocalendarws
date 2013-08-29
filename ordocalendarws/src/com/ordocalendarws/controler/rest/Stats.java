package com.ordocalendarws.controler.rest;

import java.util.ArrayList;
import java.util.Calendar;

import org.restlet.resource.Get;  
import org.restlet.resource.ServerResource;

import com.ordocalendarws.model.AccessLogModel;

import de.toolforge.googlechartwrapper.*;
import de.toolforge.googlechartwrapper.coder.*;
import de.toolforge.googlechartwrapper.data.*;
import de.toolforge.googlechartwrapper.label.*;

public class Stats extends ServerResource {
	
	@Get("html")
	public String DrawGraphic() {
		
		String ret = null;
		int max_y = 0;
		ArrayList<Float> al_valeurs  = new ArrayList<Float>(31);
		String exception = "no exception";
		
		// Chart
		//LineChart chart = new LineChart(new Dimension(600, 300));
		LineChart chart = new LineChart(new Dimension(600, 500));
		chart.setChartTitle(new ChartTitle("Visites sur le mois"));
        chart.setEncoder(EncoderFactory.getEncoder(EncodingType.TextEncoding));
		AxisLabelContainer x_axis = CreateXAxis(31);
		
		// Calendars
		Calendar cal1 = MidnightAtDate(-30);
		Calendar cal2 = (Calendar)cal1.clone();
		cal2.add(Calendar.DATE, 1);

		try {
			for(int i=1 ; i<=31 ; i++) {
				Float nb = AccessLogModel.getNbDays(cal1.getTime(), cal2.getTime());
				al_valeurs.add(nb);
				if (nb > max_y) max_y = Math.round(nb.intValue());
				
				// Label
				AxisLabel al = new AxisLabel(Integer.toString(cal1.get(Calendar.DAY_OF_MONTH)));
				al.setPos(i);
				x_axis.addLabel(al);
				
				cal1.add(Calendar.DATE, 1);
				cal2.add(Calendar.DATE, 1);
			}
			
			chart.addLineChartData(new LineChartData.LineChartDataBuilder(al_valeurs).build());
			chart.addDataScalingSet(new DataScalingSet(0, max_y+1));
			chart.addAxisLabelContainer(x_axis);
			AxisLabelContainer y_axis = new AxisLabelContainer(AxisType.YAxis);
			y_axis.setAxisRange(new AxisRange(0, max_y+1, 1));
			chart.addAxisLabelContainer(y_axis);
			
			//chart.addDataPointLabel(new DataPointLabel(new DataPointLabel.PercentageValueNumberBuilder(1).build(), Color.BLACK, 0, DataPointLabel.DataPoint.newDrawNPoint(1), 10, de.toolforge.googlechartwrapper.label.DataPointLabel.Priority.First));
			
			ret = chart.getUrl();
			
		} catch(Exception e) {
			exception = e.getMessage();
			e.printStackTrace();
		}
		
		return (ret!=null)?String.format("<html><head><title>Statistiques du flux RSS</title></head><body><img src=\"%s\" /></html>", ret):String.format("<html><head><title>Statistiques du flux RSS</title></head><body>No graphic<br>Exception occured: %s</body>", exception);
	}
	
	/**
	 * Return the date of a day in the past at midnight
	 * @param amount Number of day in the past
	 * @return The calendar at midnight
	 */
	private Calendar MidnightAtDate(int amount) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, amount);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}
	
	private AxisLabelContainer CreateXAxis(int upper) {
		AxisLabelContainer axis = new AxisLabelContainer(AxisType.XAxis);
		axis.setAxisRange(new AxisRange(1, upper, 1));
		axis.setUseLabels(true);
		
		return axis;
	}
}
