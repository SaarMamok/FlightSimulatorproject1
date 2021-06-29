package test.Algoritms;

import javafx.application.Platform;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import test.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Welzl implements TimeSeriesAnomalyDetector {
public  Circle cir1;
private String feat1,feat2;
private ArrayList<Point> col1;
private Point[] points;

    public  Circle makeCircle(List<Point> points) {
        // Clone list to preserve the caller's data, randomize order
        List<Point> shuffled = new ArrayList<>(points);
        Collections.shuffle(shuffled, new Random());

        // Progressively add points to circle or recompute circle
        Circle c = null;
        for (int i = 0; i < shuffled.size(); i++) {
            Point p = shuffled.get(i);
            if (c == null || !c.contains(p))
                c = makeCircleOnePoint(shuffled.subList(0, i + 1), p);
        }
        return c;
    }

    private static Circle makeCircleOnePoint(List<Point> points, Point p) {
        Circle c = new Circle(p, 0);
        for (int i = 0; i < points.size(); i++) {
            Point q = points.get(i);
            if (!c.contains(q)) {
                if (c.r == 0)
                    c = makeDiameter(p, q);
                else
                    c = makeCircleTwoPoints(points.subList(0, i + 1), p, q);
            }
        }
        return c;
    }

    private static Circle makeCircleTwoPoints(List<Point> points, Point p, Point q) {
        Circle circ = makeDiameter(p, q);
        Circle left = null;
        Circle right = null;

        // For each point not in the two-point circle
        Point pq = q.subtract(p);
        for (Point r : points) {
            if (circ.contains(r))
                continue;

            // Form a circumcircle and classify it on left or right side
            double cross = pq.cross(r.subtract(p));
            Circle c = makeCircumcircle(p, q, r);
            if (c == null)
                continue;
            else if (cross > 0 && (left == null || pq.cross(c.c.subtract(p)) > pq.cross(left.c.subtract(p))))
                left = c;
            else if (cross < 0 && (right == null || pq.cross(c.c.subtract(p)) < pq.cross(right.c.subtract(p))))
                right = c;
        }

        // Select which circle to return
        if (left == null && right == null)
            return circ;
        else if (left == null)
            return right;
        else if (right == null)
            return left;
        else
            return left.r <= right.r ? left : right;
    }

    static Circle makeDiameter(Point a, Point b) {
        Point c = new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
        return new Circle(c, Math.max(c.distance(a), c.distance(b)));
    }

    static Circle makeCircumcircle(Point a, Point b, Point c) {
        // Mathematical algorithm from Wikipedia: Circumscribed circle
        double ox = (Math.min(Math.min(a.x, b.x), c.x) + Math.max(Math.max(a.x, b.x), c.x)) / 2;
        double oy = (Math.min(Math.min(a.y, b.y), c.y) + Math.max(Math.max(a.y, b.y), c.y)) / 2;
        double ax = a.x - ox, ay = a.y - oy;
        double bx = b.x - ox, by = b.y - oy;
        double cx = c.x - ox, cy = c.y - oy;
        double d = (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) * 2;
        if (d == 0)
            return null;
        double x = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d;
        double y = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d;
        Point p = new Point((float) (ox + x), (float) (oy + y));
        double r = Math.max(Math.max(p.distance(a), p.distance(b)), p.distance(c));
        return new Circle(p, r);
    }

    public void learnNormal(TimeSeries ts) {
        col1 = new ArrayList<>();
        Collections.addAll(col1, SimpleAnomalyDetector.CreatPointsArr(ts.getDataTable().get(0).valuesList, ts.getDataTable().get(1).valuesList));
        cir1 = new Circle(this.makeCircle(col1));
        this.feat1=ts.getDataTable().get(0).featureName;
        this.feat2=ts.getDataTable().get(1).featureName;
    }

    public List<AnomalyReport> detect(TimeSeries ts) {
        List<AnomalyReport> l = new ArrayList<>();
        points = new Point[ts.getDataTable().get(0).valuesList.size()];
        points = SimpleAnomalyDetector.CreatPointsArr(ts.getDataTable().get(0).valuesList, ts.getDataTable().get(1).valuesList);
        long time = 1;
        for (Point p : points) {
            if (!cir1.contains(p)) {
                AnomalyReport a = new AnomalyReport(ts.getDataTable().get(0).featureName + "-" + ts.getDataTable().get(1).featureName, time);
                l.add(a);
            }
            time++;
        }
        return l;
    }


    @Override
    public String getname() {
        return null;
    }

    public String getFeat1() {
        return feat1;
    }

    public void setFeat1(String feat1) {
        this.feat1 = feat1;
    }

    public String getFeat2() {
        return feat2;
    }

    public void setFeat2(String feat2) {
        this.feat2 = feat2;
    }



    @Override
    public boolean Paintlearn(TimeSeries ts, int index, ScatterChart scatterChart) {
        XYChart.Series series=new XYChart.Series();
        XYChart.Series seriescircle=new XYChart.Series();
        scatterChart.getData().clear();
        series.setName("Learn");
        seriescircle.setName("Circle");
        Platform.runLater(() -> {
            for(int i=0;i<col1.size();i++){
                series.getData().add(new XYChart.Data(col1.get(i).x, col1.get(i).y));
            }

            float cx=this.cir1.c.x,cy=this.cir1.c.y,r= (float) this.cir1.r;
            for(int i=0;i<360;i++) {
                float x= (float) (cx+(r*Math.cos(i)));
               float y= (float) (cy+(r*Math.sin(i)));
                seriescircle.getData().add(new XYChart.Data(x, y));
            }

            //series.setData(serline.getData());
            scatterChart.getData().addAll(series,seriescircle);
        });

        return true;

    }


    @Override
    public boolean Paintdetect(XYChart.Series series,int att,int time) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<Number, Number>(points[time].x, points[time].y));
        });
        return (!this.cir1.contains(points[time]));
    }
}
