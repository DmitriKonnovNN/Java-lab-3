package INTERFACES;

import java.util.ArrayList;

public interface IStudent {
    void join (IGroup group);
    void leave ();
    void update (ArrayList<Integer>marks, boolean isHead, IGroup group);
    String getName ();
    public ArrayList<Integer> getMarksList();
    int getId();
    void resetHead();
    boolean isHead();
    float getAverageMark();

}
