package test.Algoritms;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import test.*;
import test.Algoritms.isAnomaly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static test.StatLib.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	public static float  threshold=0.8f;
	public XYChart.Series series;
	public SimpleAnomalyDetector() {
		series=new XYChart.Series();
	}
	XYChart.Data p;




	public static class Pointanomaly{
		private Point p;
		boolean aberrant;

		public Pointanomaly(Point p, boolean aberrant) {
			this.p = p;
			this.aberrant = aberrant;
		}

		public Point getP() {
			return p;
		}

		public void setP(Point p) {
			this.p = p;
		}

		public boolean isAberrant() {
			return aberrant;
		}

		public void setAberrant(boolean aberrant) {
			this.aberrant = aberrant;
		}
	}
	public SimpleAnomalyDetector(float newVal) {
		SetThreshold(newVal);
	}

	public  float GetThreshold(){
		return this.threshold;
	}

	public  void SetThreshold (float newVal){
		this.threshold = newVal;
	}

	public HashMap<Integer,CorrelatedFeatures> corFeatures= new HashMap<>();
	private HashMap<Integer,ArrayList<Pointanomaly>> anomalymap;

	public HashMap<Integer, CorrelatedFeatures> getCorFeatures() {
		return corFeatures;
	}

	public void setCorFeatures(HashMap<Integer, CorrelatedFeatures> corFeatures) {
		this.corFeatures = corFeatures;
	}

	public static Point[] CreatPointsArr (List<Float> valuesList1, List<Float> valuesList2)
	{
		Point[] pointsArray = new Point[valuesList1.size()];
		for(int i=0; i< valuesList1.size(); i++) {
			pointsArray[i] = new Point(valuesList1.get(i), valuesList2.get(i));
		}
		return  pointsArray;
	}
	public static Float getFeaturesThreshold (Point[] pointsArray, Line curLine)
	{
		// Line curLine;
		float maxDistance=0;
		//curLine = linear_reg(pointsArray);
		for(int i=0; i< pointsArray.length; i++)
		{
			float currentDistance = dev(pointsArray[i],curLine);
			if(currentDistance > maxDistance)
				maxDistance = currentDistance;
		}
		return maxDistance;
	}

	public static float[] ListToArray (List<Float> valuesList)
	{
		float[] newArr = new float[valuesList.size()];
		for(int i=0; i< newArr.length; i++)
			newArr[i] = valuesList.get(i);
		return newArr;
	}
	@Override
	public void learnNormal(TimeSeries ts) {
		String tempFeature;
		String coreFeature = null;
		float CurrentCorrlation;
		float bestCor;
		int i,j;
		int index = 0;

		int numOfFeatures=ts.dataTable.size();
		for (i=0;i<numOfFeatures;i++)
		{
			tempFeature =ts.dataTable.get(i).featureName;
			bestCor = 0;
			for (j=0;j<numOfFeatures; j++) {
				if (i != j) {
					float value = pearson(ListToArray(ts.dataTable.get(i).valuesList), ListToArray(ts.dataTable.get(j).valuesList));
					CurrentCorrlation = abs(value);
					if (CurrentCorrlation > bestCor) //// I was changed from >= to >
					{
						index = j;
						bestCor = CurrentCorrlation;
						coreFeature = ts.dataTable.get(j).featureName;
					}

				}

			}
			if (bestCor >= threshold) {
				Point[] pointsArray = CreatPointsArr(ts.dataTable.get(i).valuesList, ts.dataTable.get(index).valuesList);
				Line tempLine = linear_reg(pointsArray);
				Float tempThresuld = getFeaturesThreshold(pointsArray, tempLine);
				CorrelatedFeatures newCorrelate = new CorrelatedFeatures(tempFeature, coreFeature, bestCor, tempLine, tempThresuld + 0.389f);
				this.corFeatures.put(i,newCorrelate);
			}
		}
	}

	public static int getFeatureIndex (String featureName, TimeSeries ts){
		for(int i = 0; i< ts.dataTable.size();i++)
		{
			if (featureName.equals(ts.dataTable.get(i).featureName))
				return i;
		}

		return -1;
	}
	public int getcorindex(int index,TimeSeries ts){
		if(corFeatures.containsKey(index)==true)
			return getFeatureIndex(corFeatures.get(index).feature2,ts);

		return -1;
	}


	public static long getTime (Point[] pointsArray, Line curLine,TimeSeries ts,int feature1, int feature2 )
	{
		float maxDistance = 0;
		int index = 0;
		for(int i=0; i< pointsArray.length; i++)
		{
			float currentDistance = dev(pointsArray[i],curLine);
			if(currentDistance > maxDistance)
			{
				maxDistance = currentDistance;
				index = i;
			}
		}
		return index;
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> AnomalyReportList = new ArrayList<>();
		anomalymap=new HashMap<>();
		ArrayList<Pointanomaly> temp;
		// Check if the correlated features are normal
		for(Integer key:corFeatures.keySet()) {
			temp=new ArrayList<>();
			int feature1Index = getFeatureIndex(this.corFeatures.get(key).feature1, ts);
			int feature2Index = getFeatureIndex(this.corFeatures.get(key).feature2, ts);
			int size = ts.dataTable.get(feature1Index).valuesList.size();
			Point[] pointsArray = CreatPointsArr(ts.dataTable.get(feature1Index).valuesList,ts.dataTable.get(feature2Index).valuesList);
			for(int j=0; j< pointsArray.length; j++)
			{
				float currentDistance = dev(pointsArray[j],this.corFeatures.get(key).lin_reg);
				if(currentDistance > this.corFeatures.get(key).threshold) {
					String description = (this.corFeatures.get(key).feature1 +"-"+this.corFeatures.get(key).feature2);
					long time = j+1;
					AnomalyReport newReport = new AnomalyReport(description, time);
					AnomalyReportList.add(newReport);
					temp.add(new Pointanomaly(new Point(pointsArray[j].x,pointsArray[j].y),true));
				}
				else{
					temp.add(new Pointanomaly(new Point(pointsArray[j].x,pointsArray[j].y),false));
				}
			}
			anomalymap.put(key,temp);

		}
		return AnomalyReportList;
	}

	@Override
	public XYChart.Series paint(Object... objects) {
		return null;
	}


	@Override
	public boolean Paintlearn(TimeSeries ts, int index, ScatterChart scatterChart) {
		XYChart.Series series=new XYChart.Series();
		XYChart.Series serline=new XYChart.Series();
		int corindex=getcorindex(index,ts);

		if(corindex==-1) {
			return false;
		}
		else{
			Point[]arr=new Point[ts.getDataTable().get(0).valuesList.size()];
			arr=CreatPointsArr(ts.getDataTable().get(index).valuesList,ts.getDataTable().get(corindex).valuesList);
			Point[] finalArr = arr;
			Platform.runLater(() -> {
				float minx=Float.MAX_VALUE,maxx=0;
				for(Point val: finalArr){
					series.getData().add(new XYChart.Data((val).x, (val).y));
					if((val).x<minx) {
						minx = val.x;
					}
					if((val).x>maxx) {
						maxx = val.x;
					}
				}


					for(int i=(int)minx-40;i<(int)maxx+40;i++) {
					float x= (float) i;
					float y= (float) this.corFeatures.get(index).lin_reg.f(x);
					serline.getData().add(new XYChart.Data(x, y));

				}
				//series.setData(serline.getData());
				scatterChart.getData().addAll(series,serline);
			});
		}
		return true;
	}
	@Override
	public void Paintdetect(XYChart.Series series,int att,int time) {

	}



	@Override
	public String getname() {
		return "Simple Anomaly";
	}



	public HashMap<Integer, ArrayList<Pointanomaly>> getAnomalymap() {
		return anomalymap;
	}

	public void setAnomalymap(HashMap<Integer, ArrayList<Pointanomaly>> anomalymap) {
		this.anomalymap = anomalymap;
	}
}
