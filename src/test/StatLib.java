package test;


public class StatLib {



	// simple average
	public static float avg(float[] x){
		float sum=0;
		for(int i=0;i<x.length;i++)
		{
			sum+=x[i];
		}
		return (sum/x.length);
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float u=avg(x);
		float sum=0;
		for(int i=0;i<x.length;i++)
		{
			sum+= (x[i]*x[i]);
		}
		return ((sum/x.length)-(u*u));
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){

		float sum=0;
		for(int i=0;i<x.length;i++)
		{
			sum+= (x[i]-avg(x))*(y[i]-avg(y));
		}
		return (sum/x.length);
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		double square_x, square_y;
		square_x= Math.sqrt((double)(var(x)));
		square_y= Math.sqrt((double)(var(y)));
		return (cov(x,y)/(float)(square_x*square_y));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		int size = points.length;
		float []valX =new float[size];
		float []valY =new float[size];
		for (int i=0; i<size; i++)
		{
			valX[i]=points[i].x;
			valY[i]=points[i].y;
		}

		float a = cov(valX, valY)/var(valX);
		float b= avg(valY)- a*avg(valX);
		Line newL = new Line(a,b);
		return newL;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		float a =  linear_reg(points).a;;
		float b=  linear_reg(points).b;
		Line newL= new Line(a,b);
		return (dev(p, newL));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		return (Math.abs(p.y-l.f(p.x)));
	}

}
