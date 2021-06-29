package test.Algoritms;

public class isAnomaly {
    float val;
    boolean aberrant;

    public isAnomaly(float val, boolean aberrant) {
        this.val = val;
        this.aberrant = aberrant;
    }

    public float getVal() {
        return val;
    }
    //commit
    public void setVal(float val) {
        this.val = val;
    }

    public boolean isAberrant() {
        return aberrant;
    }

    public void setAberrant(boolean aberrant) {
        this.aberrant = aberrant;
    }
}
