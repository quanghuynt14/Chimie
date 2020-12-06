public class APoint {

    public double x, y;

    public APoint(){
        x = 0;
        y = 0;
    }

    public APoint(double ax, double ay){
        x = ax;
        y = ay;
    }

    public double distance( APoint otherPoint ) {
        double dx = x - otherPoint.x-5;
        double dy = y - otherPoint.y-5;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString() {
        return  x + " " + y;
    }

}

