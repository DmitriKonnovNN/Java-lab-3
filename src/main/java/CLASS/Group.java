package CLASS;

import INTERFACES.IDeanery;
import INTERFACES.IGroup;
import INTERFACES.IStudent;

import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;



public class Group implements IGroup {
    final private String GroupName;
    private Integer INIT_HASH_CODE;
    public HashMap <IStudent, ArrayList<Integer>> StudentMarksTable;
    IDeanery deanery;
    String HeadName;
    IStudent Head;





    public Group (String GroupName, Deanery deanery)
    {
        this.GroupName = GroupName;
        StudentMarksTable = new HashMap<>();
        this.deanery = deanery;
        deanery.register(this);
    }



    @Override
    public void register(IStudent student) {
        StudentMarksTable.put(student,new ArrayList<Integer>());
        notifyDeanery();
    }

    @Override
    public void remove(IStudent student) {
        notifyStudentAboutExpulsion(student);
        StudentMarksTable.remove(student);
        notifyDeanery();
    }

    @Override
    public void notifyAllStudents() {
        StudentMarksTable.forEach((student, list)-> {student.update(list, student.getName() == HeadName,this);
        });
            

    }
    @Override
    public void notifyStudent(IStudent student) {
        student.update(StudentMarksTable.get(student), student.getName() == HeadName, this);

    }
    private void notifyStudentAboutExpulsion (IStudent student){
        student.update(StudentMarksTable.get(student), false, null);
    }

    @Override
    public void notifyDeanery() {
        deanery.updateStudentsForGroup(this);
    }

    @Override
    public void updateDeanery(IDeanery deanery) {
        this.deanery = deanery;

    }

    @Override
    public void setHead(String name){
        boolean [] equalNameFound = {false};
        StudentMarksTable.forEach((student, list) ->
        {
            if (student.getName().equals(name)) {
                equalNameFound[0] = true;
                if (!student.isHead()){
                    Head = student;
                    HeadName = name;
                    notifyAllStudents();
                }

                }

        });
        try {
            if (!equalNameFound[0]) throw new Exception("No students with name " + name + " found!");
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void chooseHeadRandomly() {
        int numberOfStudents  = StudentMarksTable.size();
        final int randomIndex = 1 + (int)(Math.random()*numberOfStudents);
        int [] counter = {0};
        StudentMarksTable.forEach((student, list ) ->{ counter[0]++; if (counter[0]==randomIndex) setHead(student.getName());});
    }

    @Override
    public void setOneMarkForEach(int mark) {
        StudentMarksTable.forEach((student,list)->list.add(mark));
        notifyAllStudents();
    }

    @Override
    public void setOneMarkForStudent(int mark, IStudent student) {
        StudentMarksTable.get(student).add(mark);
        notifyStudent(student);
    }

    @Override
    public void giveRandomMarkForEach() {
        StudentMarksTable.forEach((student,list)->list.add(2 + (int) (Math.random() * 4)));
        notifyAllStudents();
    }

    @Override
    public void giveRandomMarkForStudent(IStudent student) {
        StudentMarksTable.get(student).add(2 + (int) Math.random() * 4);
        notifyStudent(student);
    }

    @Override
    public ArrayList<IStudent> getStudentList() {
        if (StudentMarksTable.isEmpty()) return null;
        ArrayList <IStudent> tempStudent = new ArrayList<>();
        StudentMarksTable.keySet().forEach((student)->{tempStudent.add(student);});
        return tempStudent;
    }

    @Override
    public ArrayList<String> getStudentList(String eachStringForStrings) {
        if (StudentMarksTable.isEmpty()) return null;
        ArrayList <String> tempStudent = new ArrayList<>();
        StudentMarksTable.keySet().forEach((student)->{tempStudent.add(student.getName());});
        return tempStudent;
    }

    @Override
    public String getGroupName() {
        return GroupName;
    }

    @Override
    public String getHead(){
        String [] headName = new String[1];
        StudentMarksTable.forEach((student, integers) -> { if (student.isHead())headName[0] = student.getName();});
        return headName[0];
    }

    @Override
    public IStudent getHeadAsObject(){
        IStudent [] headName = new IStudent[1];
        StudentMarksTable.forEach((student, integers) -> { if (student.isHead())headName[0] = student;});
        return headName[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Group group)) return false;
        return GroupName.equals(group.GroupName) &&
                deanery.equals(group.deanery);
    }

    @Override
    public int hashCode() {
        if (INIT_HASH_CODE == null) {INIT_HASH_CODE = Objects.hash(invokeAllGettersAndBooleans());}
        return INIT_HASH_CODE;
    }

    private ArrayList invokeAllGettersAndBooleans (){
        Method[] methodsList = this.getClass().getMethods();
        ArrayList <Object> results = new ArrayList<>();
        try {
            for (Method met: methodsList) {
                if (met.getName().startsWith("get") && met.getParameterCount()==0 || met.getName().startsWith("is"))
                {results.add(met.invoke(this, new Object[]{}));} // null leads to warnings, new Object[]{null} leads to exceptions.
            }

        } catch ( InvocationTargetException | IllegalAccessException exception ){
            exception.printStackTrace();}
        return results;
    }
}
