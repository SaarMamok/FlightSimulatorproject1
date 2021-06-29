package test.Algoritms;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import test.AnomalyReport;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.lang.Math.abs;
import static test.StatLib.pearson;

public class Hybrid implements TimeSeriesAnomalyDetector {

        private TimeSeriesAnomalyDetector anomalyalg;



    public class HybridData{
        int index;
        String algo;

        public HybridData(int index, String algo) {
            this.index = index;
            this.algo = algo;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getAlgo() {
            return algo;
        }

        public void setAlgo(String algo) {
            this.algo = algo;
        }
    }

    public HashMap<Integer,SimpleAnomalyDetector>simple=new HashMap<>();
    public HashMap<Integer,Zscore>zscorelist=new HashMap<>();
    public HashMap<Integer,Welzl>welzllist=new HashMap<>();
    public HashMap<Integer,HybridData>corvalues=new HashMap<>();
    private HashMap<String,Integer> Hashvalues=new HashMap<>();

    public Hybrid() {
    }

    public HashMap<Integer, HybridData> getCorvalues() {
        return corvalues;
    }

    public void setCorvalues(HashMap<Integer, HybridData> corvalues) {
        this.corvalues = corvalues;
    }

    public HashMap<String, Integer> getHashvalues() {
        return Hashvalues;
    }

    public void setHashvalues(HashMap<String, Integer> hashvalues) {
        Hashvalues = hashvalues;
    }

    public void learnNormal(TimeSeries ts) {
        String coreFeature = null;
        float CurrentCorrlation,bestcor;
        int i, j;
        int countl=0,countz=0,countw=0;
        int indexfeature=0;
        int numOfFeatures = ts.getDataTable().size();
        for (i = 0; i < numOfFeatures; i++) {
            bestcor=0;
            for (j = 0; j < numOfFeatures; j++) {
                if(i!=j)
                {
                    float value = pearson(SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(i).valuesList), SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(j).valuesList));
                    CurrentCorrlation = abs(value);
                    if(bestcor<CurrentCorrlation) {
                        bestcor = CurrentCorrlation;
                        indexfeature=j;
                    }
                }
            }

            if(bestcor>=0.95){
                corvalues.put(i,new HybridData(countl,"l"));
                countl++;
                TimeSeries linear=new TimeSeries(ts.getDataTable().get(i),ts.getDataTable().get(indexfeature));
                SimpleAnomalyDetector s= new SimpleAnomalyDetector(bestcor);
                s.learnNormal(linear);
                simple.put(i,s);
            }
            else if(bestcor<=0.5)
            {
                corvalues.put(i,new HybridData(countz,"z"));
                countz++;
                TimeSeries zs=new TimeSeries(ts.getDataTable().get(i));
                Zscore z=new Zscore();
                z.learnNormal(zs);
                zscorelist.put(i,z);
            }
            else{
                corvalues.put(i,new HybridData(countw,"w"));
                countw++;
                TimeSeries Wl=new TimeSeries(ts.getDataTable().get(i),ts.getDataTable().get(indexfeature));
                Welzl W=new Welzl();
                W.learnNormal(Wl);
                welzllist.put(i,W);
            }
            Hashvalues.put(ts.getDataTable().get(i).getFeatureName(),i);
        }
    }


    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> l=new ArrayList<>();
        int feat1,feat2;
        int i;
        for(int index:simple.keySet()) {
            feat1=Hashvalues.get(simple.get(index).corFeatures.get(0).feature1);
            feat2=Hashvalues.get(simple.get(index).corFeatures.get(0).feature2);
            l.addAll(simple.get(index).detect(new TimeSeries(ts.getDataTable().get(feat1),ts.getDataTable().get(feat2))));
        }
        for(int index:zscorelist.keySet()){
            feat1=Hashvalues.get(zscorelist.get(index).getCols_treshould().get(0).name);
            l.addAll(zscorelist.get(index).detect(new TimeSeries(ts.getDataTable().get(feat1))));
        }
        for(int index:welzllist.keySet()){
            feat1=Hashvalues.get(welzllist.get(index).getFeat1());
            feat2=Hashvalues.get(welzllist.get(index).getFeat2());
            l.addAll(welzllist.get(index).detect(new TimeSeries(ts.getDataTable().get(feat1),ts.getDataTable().get(feat2))));
        }
        return l;
    }

    @Override
    public boolean Paintlearn(TimeSeries ts, int index, ScatterChart scatterChart) {
            if(corvalues.get(index).algo.compareTo("l")==0){
                simple.get(index).Paintlearn(ts, 0, scatterChart);
            }
            else if (corvalues.get(index).algo.compareTo("z")==0) {
                zscorelist.get(index).Paintlearn(ts, 0, scatterChart);
            }
            else if(corvalues.get(index).algo.compareTo("w")==0) {
                welzllist.get(index).Paintlearn(ts, index, scatterChart);
            }
        return true;
    }


    @Override
    public boolean Paintdetect(XYChart.Series series,int att,int time) {
        if(corvalues.get(att).algo.compareTo("l")==0){
            return simple.get(att).Paintdetect(series, 0, time);
        }
        else if (corvalues.get(att).algo.compareTo("z")==0) {
           return zscorelist.get(att).Paintdetect(series, 0, time);
        }
        else if(corvalues.get(att).algo.compareTo("w")==0) {
         return  welzllist.get(att).Paintdetect(series, att, time);
        }
        return true;
    }

    @Override
    public String getname() {
       return "Hybrid";
    }
}