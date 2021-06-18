package test.Algoritms;

import test.AnomalyReport;
import test.SimpleAnomalyDetector;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static test.StatLib.pearson;

public class Hybrid implements TimeSeriesAnomalyDetector {
    public class HybridData{
        int f1;
        int f2;
        String algo;

    }

    public ArrayList<SimpleAnomalyDetector>simple=new ArrayList<>();
    public ArrayList<Zscore>zscorelist=new ArrayList<>();
    public ArrayList<Welzl>welzllist=new ArrayList<>();
    public HashMap<Integer,Integer>corvalues=new HashMap<>();
    private HashMap<String,Integer> Hashvalues=new HashMap<>();

    public Hybrid() {
    }

    public HashMap<Integer, Integer> getCorvalues() {
        return corvalues;
    }

    public void setCorvalues(HashMap<Integer, Integer> corvalues) {
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
            corvalues.put(i,indexfeature);
            if(bestcor>=0.95){
                TimeSeries linear=new TimeSeries(ts.getDataTable().get(i),ts.getDataTable().get(indexfeature));
                SimpleAnomalyDetector s= new SimpleAnomalyDetector(bestcor);
                s.learnNormal(linear);
                simple.add(s);
            }
            else if(bestcor<=0.5)
            {
                TimeSeries zs=new TimeSeries(ts.getDataTable().get(i));
                Zscore z=new Zscore();
                z.learnNormal(zs);
                zscorelist.add(z);
            }
            else{
                TimeSeries Wl=new TimeSeries(ts.getDataTable().get(i),ts.getDataTable().get(indexfeature));
                Welzl W=new Welzl();
                W.learnNormal(Wl);
                welzllist.add(W);
            }
            Hashvalues.put(ts.getDataTable().get(i).getFeatureName(),i);
        }
    }


    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> l=new ArrayList<>();
        int feat1,feat2;
        int i;
        for(i=0; i< this.simple.size(); i++) {
            feat1=Hashvalues.get(simple.get(i).corFeatures.get(0).feature1);
            feat2=Hashvalues.get(simple.get(i).corFeatures.get(0).feature2);
            l.addAll(simple.get(i).detect(new TimeSeries(ts.getDataTable().get(feat1),ts.getDataTable().get(feat2))));
        }
        for(i=0;i<zscorelist.size();i++){
            feat1=Hashvalues.get(zscorelist.get(i).getCols_treshould().get(0).name);
            l.addAll(zscorelist.get(i).detect(new TimeSeries(ts.getDataTable().get(feat1))));
        }
        for(i=0;i<welzllist.size();i++){
            feat1=Hashvalues.get(welzllist.get(i).getFeat1());
            feat2=Hashvalues.get(welzllist.get(i).getFeat2());
            l.addAll(welzllist.get(i).detect(new TimeSeries(ts.getDataTable().get(feat1),ts.getDataTable().get(feat2))));
        }
        return l;
    }
}