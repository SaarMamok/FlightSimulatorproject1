package test.Algoritms;

import test.*;

import java.util.ArrayList;
import java.util.List;

public class Zscore implements TimeSeriesAnomalyDetector {
    List<Float> cols_treshould = new ArrayList();

    public float[] madeARR(float[] arr, int size) {
        float[] a = new float[size];
        for (int i = 0; i < size; i++) {
            a[i] = arr[i];
        }
        return a;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        int size = ts.getDataTable().size();
        float[] cols_Avg = new float[size];
        float avg, avgsum,zs,dev,max;
        for (int i = 0; i < size; i++) {
            List<Float> currentList = ts.getDataTable().get(i).valuesList;
            max = Math.abs(currentList.get(0));//the first number
            avg = 0;
            avgsum = 0;
            for (int j = 1; j < currentList.size(); j++) {
                avgsum += currentList.get(j - 1);
                avg = avgsum / j;
                float variance = StatLib.var(madeARR(SimpleAnomalyDetector.ListToArray(currentList), j));
                dev = (float) Math.sqrt(variance);
                zs = (float) (Math.abs(currentList.get(j) - avg) / dev);
                if (zs > max)
                    max = zs;
            }
            cols_treshould.add(max);

        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> AnomalyReportList = new ArrayList<>();
        int size = ts.getDataTable().size();
        float[] cols_Avg = new float[size];
        float avg, avgsum,max;
        for (int i = 0; i < size; i++) {
            List<Float> currentList = ts.getDataTable().get(i).valuesList;
            max = Math.abs(currentList.get(0));//the first number
            avg = 0;
            avgsum = 0;
            for (int j = 1; j < currentList.size(); j++) {
                avgsum += currentList.get(j - 1);
                avg = avgsum / j;
                float variance = StatLib.var(madeARR(SimpleAnomalyDetector.ListToArray(currentList), j));
                float dev = (float) Math.sqrt(variance);
                float zs = (float) (Math.abs(currentList.get(j) - avg) / dev);
                if (zs > cols_treshould.get(i)) {
                    String description = ts.getDataTable().get(i).featureName;
                    long time = j + 1;
                    AnomalyReport newReport = new AnomalyReport(description, time);
                    AnomalyReportList.add(newReport);
                }
            }

        }
        return AnomalyReportList;
    }
}
