package test;

import javafx.beans.property.StringProperty;
import javafx.scene.chart.XYChart;

import java.util.List;

public interface TimeSeriesAnomalyDetector extends Painter {

	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
	public String getname();

}
