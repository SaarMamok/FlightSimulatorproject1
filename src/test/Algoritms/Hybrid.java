package test.Algoritms;

import test.AnomalyReport;
import test.SimpleAnomalyDetector;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static test.StatLib.pearson;

public class Hybrid implements TimeSeriesAnomalyDetector {
    public ArrayList<SimpleAnomalyDetector>simple=new ArrayList<>();
    public ArrayList<Zscore>zscorelist=new ArrayList<>();
    public ArrayList<Welzl>welzllist=new ArrayList<>();

    public void learnNormal(TimeSeries ts) {
        String coreFeature = null;
        float CurrentCorrlation,bestcor;
        int i, j;
        int indexfeature=0;
        int numOfFeatures = ts.getDataTable().size();
        for (i = 0; i < (numOfFeatures - 1); i++) {
            bestcor=0;
            for (j = i + 1; j < numOfFeatures; j++) {
                float value = pearson(SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(i).valuesList), SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(j).valuesList));
                CurrentCorrlation = abs(value);
                if(bestcor<CurrentCorrlation) {
                    bestcor = CurrentCorrlation;
                    indexfeature=j;
                }
            }
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
        }
    }
    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> l=new ArrayList<>();
        int i;
        for(i=0; i< this.simple.size(); i++) {
            l.addAll(simple.get(i).detect(ts));
        }
        for(i=0;i<zscorelist.size();i++){
            l.addAll(zscorelist.get(i).detect(ts));
        }
        for(i=0;i<welzllist.size();i++){
            l.addAll(welzllist.get(i).detect(ts));
        }
        return l;
    }
}