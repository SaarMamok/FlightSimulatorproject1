package test;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public interface Painter {
    public boolean Paintlearn(TimeSeries ts, int index, ScatterChart<Number,Number>scatterChart);
    public boolean Paintdetect(XYChart.Series series,int att,int time) ;
}
