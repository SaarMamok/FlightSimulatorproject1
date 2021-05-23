package test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static test.StatLib.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	public static float  threshold;

	public SimpleAnomalyDetector(float newVal) {
		SetThreshold(newVal);
	}

	public  float GetThreshold(){
		return this.threshold;
	}

	public  void SetThreshold (float newVal){
		this.threshold = newVal;
	}

	public ArrayList<CorrelatedFeatures> corFeatures= new ArrayList<CorrelatedFeatures>();

	public ArrayList<CorrelatedFeatures> getCorFeatures() {
		return corFeatures;
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
		float threshold = GetThreshold();
		int numOfFeatures=ts.dataTable.size();
		for (i=0;i<(numOfFeatures-1);i++)
		{
			tempFeature =ts.dataTable.get(i).featureName;
			bestCor = 0;
			for (j=i+1;j<numOfFeatures; j++)
			{
				float value =pearson(ListToArray(ts.dataTable.get(i).valuesList),ListToArray(ts.dataTable.get(j).valuesList));
				CurrentCorrlation = abs(value);
				if(CurrentCorrlation > bestCor) //// I was changed from >= to >
				{
					index = j;
					bestCor = CurrentCorrlation;
					coreFeature = ts.dataTable.get(j).featureName;
				}
			}
			if(bestCor >= threshold) {
				Point[] pointsArray = CreatPointsArr(ts.dataTable.get(i).valuesList, ts.dataTable.get(index).valuesList);
				Line tempLine = linear_reg(pointsArray);
				Float tempThresuld = getFeaturesThreshold(pointsArray, tempLine);
				CorrelatedFeatures newCorrelate = new CorrelatedFeatures(tempFeature,coreFeature,bestCor,tempLine,tempThresuld+0.389f);
				this.corFeatures.add(newCorrelate);
			}
		}
	}

	public static int getFeatureIndex (String featureName, TimeSeries ts){
		for(int i = 0; i< ts.dataTable.size();i++)
		{
			if (featureName.equals(ts.dataTable.get(i).featureName))
				return i;
		}
		return 0;
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
		// Check if the correlated features are normal
		for(int i=0; i< this.corFeatures.size(); i++) {
			int feature1Index = getFeatureIndex(this.corFeatures.get(i).feature1, ts);
			int feature2Index = getFeatureIndex(this.corFeatures.get(i).feature2, ts);
			int size = ts.dataTable.get(feature1Index).valuesList.size();
			Point[] pointsArray = CreatPointsArr(ts.dataTable.get(feature1Index).valuesList,ts.dataTable.get(feature2Index).valuesList);
			////////////////////
			for(int j=0; j< pointsArray.length; j++)
			{
				float currentDistance = dev(pointsArray[j],this.corFeatures.get(i).lin_reg);
				if(currentDistance > this.corFeatures.get(i).threshold) {
					String description = (this.corFeatures.get(i).feature1 +"-"+this.corFeatures.get(i).feature2);
					long time = j+1;
					AnomalyReport newReport = new AnomalyReport(description, time);
					AnomalyReportList.add(newReport);

				}
			}

		}
		return AnomalyReportList;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return corFeatures;
	}
}
