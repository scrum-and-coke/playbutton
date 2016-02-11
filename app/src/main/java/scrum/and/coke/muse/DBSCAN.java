package scrum.and.coke.muse;

/**
 * Created by Ben on 1/19/2016.
 */
public class DBSCAN {
    double eps;
    int min_points;
    double[][] X;


    public DBSCAN(double eps, int min_points, double[][] X) { this.eps = eps; this.min_points = min_points; this.X = X; }

    public void cluster() {
        for (double[] x : X) {

        }
    }

    private boolean neighbourhood_query(double[] point) {
        //
    }
}
