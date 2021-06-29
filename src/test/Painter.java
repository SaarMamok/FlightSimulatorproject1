package test;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public interface Painter {
    public boolean Paintlearn(TimeSeries ts, int index, ScatterChart<Number,Number>scatterChart);
    public XYChart.Series Paintdetect();
}
