package test.Algoritms;

import javafx.application.Platform;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import test.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Zscore implements TimeSeriesAnomalyDetector {
    public Zscore() {
        zhash=new HashMap<>();
        anomalymap=new HashMap<>();
    }







    public class Title {
        String name;
        float val;

        public Title(String name, float val) {
            this.name = name;
            this.val = val;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            this.val = val;
        }
    }

    private HashMap<Integer, Title> cols_treshould = new HashMap<>();

    public HashMap<Integer, Title> getCols_treshould() {
        return cols_treshould;
    }

    public void setCols_treshould(HashMap<Integer, Title> cols_treshould) {
        this.cols_treshould = cols_treshould;
    }
    private HashMap<Integer,ArrayList<isAnomaly>> anomalymap;
    private HashMap<Integer,ArrayList<Float>>zhash;

    public float[] madeARR(float[] arr, int size) {
        float[] a = new float[size];
        for (int i = 0; i < size; i++) {
            a[i] = arr[i];
        }
        return a;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        ArrayList<Float>temp;
        int size = ts.getDataTable().size();
        float sd, avg, zs, max;
        for (int i = 0; i < size; i++) {
            temp=new ArrayList<>();
            List<Float> currentList = ts.getDataTable().get(i).valuesList;
            avg = StatLib.avg(SimpleAnomalyDetector.ListToArray(currentList));
            if ((StatLib.var(SimpleAnomalyDetector.ListToArray(currentList))) <= 0)
                sd = 0;
            else
                sd = (float) Math.sqrt(StatLib.var(SimpleAnomalyDetector.ListToArray(currentList)));
            if (sd == 0)
                max = 0;
            else
                max = Math.abs((currentList.get(0) - avg) / sd);//the first number
            temp.add(max);
            for (int j = 1; j < currentList.size(); j++) {
                if (sd == 0)
                    zs = 0;
                else
                    zs = Math.abs((currentList.get(j) - avg) / sd);
                if (zs > max)
                    max = zs;
                temp.add(zs);
            }
            zhash.put(i,temp);
            cols_treshould.put(i, new Title(ts.getDataTable().get(i).getFeatureName(), max));

        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> AnomalyReportList = new ArrayList<>();
        ArrayList<isAnomaly> temp;
        int size = ts.getDataTable().size();
        float sd, avg, zs, max;
        for (int i = 0; i < size; i++) {
            temp=new ArrayList<>();
            List<Float> currentList = ts.getDataTable().get(i).valuesList;
            avg = StatLib.avg(SimpleAnomalyDetector.ListToArray(currentList));
            sd = (float) Math.sqrt(StatLib.var(SimpleAnomalyDetector.ListToArray(currentList)));
            for (int j = 0; j < currentList.size(); j++) {
                if (sd == 0)
                    zs = 0;
                else
                    zs = Math.abs((currentList.get(i) - avg) / sd);

                if (zs > cols_treshould.get(i).val) {

                    String description = ts.getDataTable().get(i).featureName;
                    long time = j + 1;
                    AnomalyReport newReport = new AnomalyReport(description, time);
                    AnomalyReportList.add(newReport);
                    temp.add(new isAnomaly(zs,true));
                }
                else
                    temp.add(new isAnomaly(zs,false));
            }
            anomalymap.put(i,temp);
        }
        return AnomalyReportList;
    }



    @Override
    public boolean Paintlearn(TimeSeries ts, int index, ScatterChart scatterChart) {

        XYChart.Series series=new XYChart.Series();
        Platform.runLater(() -> {
            //for(Float val: zhash.get(index))
           for(int i=0;i<this.zhash.get(index).size();i++)
           {
               series.getData().add(new XYChart.Data(i, this.zhash.get(index).get(i)));
           }
            //series.setData(serline.getData());

            scatterChart.getData().addAll(series);
        });
        return true;

    }
    @Override
    public void Paintdetect(XYChart.Series series,int att,int time) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<Number, Number>(time, zhash.get(att).get(time)));
        });
    }

    @Override
    public String getname() {
        return null;
    }

    public HashMap<Integer, ArrayList<Float>> getZhash() {
        return zhash;
    }

    public HashMap<Integer, ArrayList<isAnomaly>> getAnomalymap() {
        return anomalymap;
    }

    public void setAnomalymap(HashMap<Integer, ArrayList<isAnomaly>> anomalymap) {
        this.anomalymap = anomalymap;
    }
}
