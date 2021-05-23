package test.Algoritms;

import test.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Welzl implements TimeSeriesAnomalyDetector {
    public class Circle{
        Point p;
        float R;
        public Circle(Point p, float R){
            this.p=p;
            this.R=R;
        }
        public Circle(Circle c){
            this.p=c.p;
            this.R=c.R;
        }
    }
    private Circle c;

    public Circle getC() {
        return c;
    }

    private float dist(Point p1, Point p2){
        return (float)Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow((p1.y-p2.y),2));
    }
    private Boolean is_inside(Circle c, Point p1){
        return dist(c.p,p1)<=c.R;

    }
    public Point get_circle_center(float bx, float by, float cx, float cy)
    {
        float B = bx * bx + by * by;
        float C = cx * cx + cy * cy;
        float D = bx * cy - by * cx;
        return  new Point(((cy * B - by * C) / (2 * D)),((bx * C - cx * B) / (2 * D)));
    }
    public Circle circle_from(Point A, Point B,Point C){
        Point I = new Point(get_circle_center(B.x - A.x, B.y - A.y,
                C.x - A.x, C.y - A.y).x,get_circle_center(B.x - A.x, B.y - A.y,
                C.x - A.x, C.y - A.y).y);
        Point f=new Point(I.x + A.x,I.y + A.y);
        return new Circle(f, dist(f, A)) ;
    }
    public Circle circle_from(Point A, Point B) {
        Point c= new Point((A.x+B.x)/2.0f,(A.y+B.y)/2.0f);
        return new Circle(c,dist(A,B)/2.0f);
    }
    public boolean is_valid_circle(Circle c,ArrayList<Point>arr){
        for(Point p:arr){
            if(!is_inside(c,p))
                return false;
        }
        return true;
    }
    public Circle min_circle(ArrayList<Point>arr){
        if(arr.size()==0)
            return new Circle(new Point(0,0),0);
        else if(arr.size()==1){
            return new Circle(arr.get(0),0);
        }
        else if(arr.size()==2){
            return circle_from(arr.get(0),arr.get(1));
        }
        int i,j;
        for(i=0;i<3;i++){
            for(j=i+1;j<3;j++){
                Circle c =new Circle(circle_from(arr.get(i),arr.get(j)));
                if(is_valid_circle(c,arr))
                    return c;
            }
        }
        return new Circle(circle_from(arr.get(0),arr.get(1),arr.get(2)));
    }
    public Circle welzl_help(ArrayList<Point> arr1,ArrayList<Point> arr2,int size){
        if(size==0||arr2.size()==3)
            return min_circle(arr2);
        Random r=new Random(size+1);
        int index=r.nextInt();
        Point p=new Point(arr1.get(index).x,arr1.get(index).y);
        Collections.swap(arr1,index,size-1);
        Circle d =new Circle(welzl_help(arr1,arr2,size-1));
        if(is_inside(d,p))
            return d;
        arr2.add(p);
        return welzl_help(arr1,arr2,size-1);
    }
    public Circle welzl(ArrayList<Point>p){
        ArrayList<Point>p_copy=new ArrayList<>();
        for(Point p1:p){
            p_copy.add(p1);
        }
        Collections.shuffle(p_copy);
        return welzl_help(p_copy,null,p.size());
    }
    public void learnNormal(TimeSeries ts){
        Point[]points=new Point[ts.getDataTable().get(0).valuesList.size()];
        ArrayList<Point>col1=new ArrayList<>();
        Collections.addAll(col1, SimpleAnomalyDetector.CreatPointsArr(ts.getDataTable().get(0).valuesList,ts.getDataTable().get(1).valuesList));
        c= new Circle(welzl(col1));
    }
    public List<AnomalyReport> detect(TimeSeries ts){
        List<AnomalyReport> l=new ArrayList<>();
        Point[]points=new Point[ts.getDataTable().get(0).valuesList.size()];
        SimpleAnomalyDetector.CreatPointsArr(ts.getDataTable().get(0).valuesList,ts.getDataTable().get(1).valuesList);
        long time=1;
        for(Point p:points){
            if(!is_inside(c,p)){
                AnomalyReport a=new AnomalyReport(ts.getDataTable().get(0).featureName+"-"+ts.getDataTable().get(1).featureName,time);
                l.add(a);
            }
            time++;
        }
        return l;
    }

}
