package INTERFACES;

import java.util.ArrayList;

public interface IGroup {
    void register (IStudent student);

    void remove (IStudent student);

    void updateDeanery (IDeanery deanery);

    void notifyStudent (IStudent student);

    void notifyDeanery ();

    void notifyAllStudents ();

    void setOneMarkForEach (int mark);

    void setOneMarkForStudent (int mark, IStudent student);

    void giveRandomMarkForEach ();

    void giveRandomMarkForStudent(IStudent student);

    ArrayList<IStudent> getStudentList ();

    public ArrayList<String> getStudentList(String eachStringForStrings);

    String getGroupName();

    void chooseHeadRandomly ();

    String getHead();

    IStudent getHeadAsObject();

    void setHead(String groupName);
}
