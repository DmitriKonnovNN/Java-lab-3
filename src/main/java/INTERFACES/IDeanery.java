package INTERFACES;

import CLASS.GroupAlreadyExistsException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface IDeanery {
    void register (IGroup group);

    void createGroup (String groupName) throws GroupAlreadyExistsException;

    void removeGroup (IGroup group);

    void removeStudent (IGroup group,IStudent student);

    void register (String groupName, IStudent student);

    void notifyGroupAboutExclusion (IGroup group);

    void register (String groupName, String studentName);

    void updateStudentsForGroup(IGroup group);

    ArrayList<IGroup> getGroupList();

    LinkedList<String> expelBadStudents();

    void setOneMarkForEachStudentInEachGroup (int mark);

    void setOneMarkForStudent (int mark, String studentName, String groupName);

    void giveRandomMarkForEachStudentInEachGroup ();

    void giveRandomMarkForStudent(String studentName, String groupName);

    void chooseHeadRandomlyForEachGroup ();

    HashMap<String, String> getMapOfHeadsForEachGroup();

    String getHeadForGroup (String groupName);

    public HashMap<IGroup, ArrayList<IStudent>> getGroupStudentTable();

    public void setRegisterFromFileRandomly(IRegisterFromFileRandomly registerFromFileRandomlyClass);

    public void registerStudentsFromFileRandomly (Object obj);

    public JSONObject getListOfGroupsStudentsFieldsAsJSON ();

    public ArrayList<Integer> getMarkListForStudent(String studentName);
}
