package CLASS;

import INTERFACES.*;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.json.*;

import javax.xml.crypto.NoSuchMechanismException;


public class Deanery implements IDeanery {
    private HashMap <IGroup, ArrayList<IStudent>> GroupStudentTable;
    IRegisterFromFileRandomly registerFromFileRandomly;

    public Deanery () {
        GroupStudentTable = new HashMap<>();
        registerFromFileRandomly = new RegisterFromSourceTXTRandomly(this);

    }
    public Deanery(String inputFileFormat)  {
        GroupStudentTable = new HashMap<>();
        switch (inputFileFormat) {
            case "txt", ".txt", "TXT" -> registerFromFileRandomly = new RegisterFromTXTRandomly(this);
            case "JSON", ".json", "json" -> registerFromFileRandomly = new RegisterFromJSONRandomly(this);
            default -> {registerFromFileRandomly = null;
                try {
                    throw new NoSuchMechanismException("Format " +inputFileFormat+ " hasn't been implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    
    @Override
    public void setRegisterFromFileRandomly(IRegisterFromFileRandomly registerFromFileRandomlyClass) {
        this.registerFromFileRandomly = registerFromFileRandomlyClass;
    }

    @Override
    public void registerStudentsFromFileRandomly (Object obj){
        registerFromFileRandomly.registerFromFileRandomly(obj);
    }


    @Override
    public void createGroup(String groupName) throws GroupAlreadyExistsException {

        boolean [] equalNameFound = new boolean[]{false};
        getGroupList().forEach(group -> {if(groupName.equals(group.getGroupName()))equalNameFound[0] = true;});

        if (equalNameFound[0]) {
            try {
                throw new GroupAlreadyExistsException("Group with this name already exists!");
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        else new Group(groupName, this);
    }


    @Override
    public void register(IGroup group) {
        GroupStudentTable.put(group, new ArrayList<>());

    }

    @Override
    public void register(String groupName, IStudent student) {

        GroupStudentTable.forEach((group, list)->{
            if (group.getGroupName().equals(groupName)) group.register(student);

        });
    }
    @Override
    public void register (String groupName, String studentName) {
        int id = idGenerator.getNextId();
        IGroup [] tempGroup = new IGroup[1];
        boolean [] equalNameFound = new boolean[]{false};
        GroupStudentTable.forEach((group,list)->{
            if (group.getGroupName().equals(groupName)) {
                tempGroup[0] = group;
                list.forEach((student)->{
                    if(student.getName() == studentName){
                        equalNameFound[0] = true;
                    }
                });
            }

        });
        try {
            if (equalNameFound[0]) {
                throw new StudentAlreadyExistsException("There's already a student with name " + studentName + "!");
            }
            if (tempGroup[0] == null) {
                throw new NoSuchElementException("There's no groups with name " + groupName + "!");
            }
        }catch (Exception e){e.printStackTrace();}

        if (!equalNameFound[0] && tempGroup[0]!= null) {
            register(groupName,new Student(studentName, id, tempGroup[0])); }

    }

    @Override
    public ArrayList<IGroup> getGroupList() {
        ArrayList <IGroup> tempGroups = new ArrayList<>();
        GroupStudentTable.keySet().forEach((group)->{tempGroups.add(group);});
        return tempGroups;

    }



    @Override
    public void removeGroup(IGroup group) {
        notifyGroupAboutExclusion(group);
        GroupStudentTable.remove(group);

    }
    public void removeGroup (String groupName){
        boolean [] equalNameFound = new boolean[]{false};
        getGroupList().forEach(group -> {if(groupName.equals(group.getGroupName())){equalNameFound[0] = true;
            removeGroup(group);}});
        if (!equalNameFound[0]) {
            try {
                throw new NoSuchElementException("No such group found!");
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

    }

    @Override
    public void notifyGroupAboutExclusion(IGroup group) {
        group.updateDeanery(null);
    }

    @Override
    public void removeStudent(IGroup group, IStudent student) {
       group.remove(student);
    }

    public void removeStudent (String studentName, String groupName){

        boolean [] equalGroupFound = new boolean[]{false};
        boolean [] equalNameFound = new boolean[]{false};
        GroupStudentTable.forEach((group,list)->{
            if (group.getGroupName().equals(groupName)) {
               equalGroupFound[0] = true;
                list.forEach((student)->{
                    if(student.getName() == studentName){
                        equalNameFound[0] = true;
                        removeStudent(group,student);
                    }
                });
            }

        });
        try {
            if (!equalNameFound[0]) {
                throw new NoSuchElementException("No students with name " + studentName + "found!");
            }
            if (!equalGroupFound[0]) {
                throw new NoSuchElementException("There's no groups with name " + groupName + "found!");
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public LinkedList<String> expelBadStudents() {
        LinkedList<String> ListOfExpelledStudents = new LinkedList<>();
        GroupStudentTable.forEach((group,list)->{list.forEach(student->{float averageMark = student.getAverageMark();
        if (averageMark < 3.5) {ListOfExpelledStudents.add(student.getName());
            group.remove(student); }});});
        ListOfExpelledStudents.addFirst( ListOfExpelledStudents.size() + " students expelled: ");
        return ListOfExpelledStudents;
    }

    @Override
    public void giveRandomMarkForEachStudentInEachGroup() {
         GroupStudentTable.forEach((group, students) -> group.giveRandomMarkForEach());
    }
    public void giveRandomMarkForEachStudentInEachGroup(int howManyMarks){
        if (howManyMarks > 0) for (int i = 1; i <= howManyMarks; i++){
            giveRandomMarkForEachStudentInEachGroup();
        }
    }


    @Override
    public void setOneMarkForEachStudentInEachGroup(int mark) {
        GroupStudentTable.forEach((group, student) -> group.setOneMarkForEach(mark));
    }

    @Override
    public void setOneMarkForStudent(int mark, String studentName, String groupName) {

        boolean [] equalGroupFound = new boolean[]{false};
        boolean [] equalNameFound = new boolean[]{false};
        GroupStudentTable.forEach((group,list)->{
            if (group.getGroupName().equals(groupName)) {
                equalGroupFound[0] = true;
                list.forEach((student)->{
                    if(student.getName() == studentName){
                        equalNameFound[0] = true;
                        group.setOneMarkForStudent(mark, student);
                    }
                });
            }

        });
        try {
            if (!equalNameFound[0]) {
                throw new NoSuchElementException("No students with name " + studentName + "found!");
            }
            if (!equalGroupFound[0]) {
                throw new NoSuchElementException("There's no groups with name " + groupName + "found!");
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void giveRandomMarkForStudent(String studentName, String groupName) {
        boolean [] equalGroupFound = new boolean[]{false};
        boolean [] equalNameFound = new boolean[]{false};
        GroupStudentTable.forEach((group,list)->{
            if (group.getGroupName().equals(groupName)) {
                equalGroupFound[0] = true;
                list.forEach((student)->{
                    if(student.getName() == studentName){
                        equalNameFound[0] = true;
                        group.giveRandomMarkForStudent(student);
                    }
                });
            }

        });
        try {
            if (!equalNameFound[0]) {
                throw new NoSuchElementException("Ain't no students with name " + studentName + "found!");
            }
            if (!equalGroupFound[0]) {
                throw new NoSuchElementException("Ain't no groups with name " + groupName + "found!");
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void chooseHeadRandomlyForEachGroup() {
        GroupStudentTable.forEach((group, list)-> group.chooseHeadRandomly());
    }

    @Override
    public HashMap <String,String> getMapOfHeadsForEachGroup() {
        HashMap <String, String>  GHMap = new HashMap<>();
        GroupStudentTable.forEach((group,list)-> GHMap.put(group.getGroupName(), group.getHead()));
        return GHMap;
    }

    @Override
    public String getHeadForGroup(String groupName) {
        boolean [] equalGroupFound = new boolean[]{false};
        String [] HeadName = new String [1];
        GroupStudentTable.forEach((group,list)->{
            if (group.getGroupName().equals(groupName)) {
                equalGroupFound[0] = true;
                HeadName[0] = group.getHead();
            }

        });
        try {
            if (!equalGroupFound[0]) {
                throw new NoSuchElementException("There's no groups with name " + groupName + "found!");
            }
        }catch (Exception e){e.printStackTrace();}
        return HeadName[0];
    }

    @Override
    public void updateStudentsForGroup(IGroup group) {

        GroupStudentTable.put(group, group.getStudentList());
    }

    @Override
    public JSONObject getListOfGroupsStudentsFieldsAsJSON (){
        int [] counter = {0};
        JSONObject listOb = new JSONObject();
        GroupStudentTable.forEach((gr, stList)->{
            JSONObject groupOb = new JSONObject();
            counter[0]++;
            groupOb.put(gr.getGroupName(),getStudentListAsJSON(gr));
            listOb.put("группа " + counter[0], groupOb);
        });
       return listOb;
    }
    private JSONObject getStudentListAsJSON (IGroup group) {
        int [] counter = {0};
        final JSONObject groupOb = new JSONObject();
        group.getStudentList().forEach((s)->{ JSONObject studentOb = new JSONObject();
            counter[0]++;
            studentOb.put("имя", s.getName());
            studentOb.put("id", s.getId());
            studentOb.put("оценки", s.getMarksList());
            studentOb.put("средняя оценка", s.getAverageMark());
            groupOb.put("студент " + counter[0],studentOb);
        });
        return groupOb;
    }


    @Override
    public HashMap<IGroup, ArrayList<IStudent>> getGroupStudentTable() {
        return GroupStudentTable;
    }

    @Override
    public ArrayList<Integer> getMarkListForStudent(String studentName) {
        ArrayList<Integer> Marklist = new ArrayList<>();
        boolean [] equalNameFound = new boolean[]{false};
        GroupStudentTable.forEach((group,list)->{
                list.forEach((student)->{
                    if(student.getName() == studentName){
                        equalNameFound[0] = true;
                        Marklist.addAll(student.getMarksList());
                    }
                });


        });
        try {
            if (!equalNameFound[0]) {
                throw new NoSuchElementException("No students with name " + studentName + "found!");
            }
        }catch (Exception e){e.printStackTrace();}
        return Marklist;
    }
}
