package CLASS;


import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.NoSuchMechanismException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

class DeaneryTest {

    static Deanery deanery1 = new Deanery ();
    static Deanery deanery2 = new Deanery("JSON");
    static Deanery deanery3 = new Deanery(".txt");




    static final String FIO1 = "Тестовый Студент 1";
    static final String FIO2 = "Тестовый Студент 2";
    static final String FIO3 = "Тестовый Студент 3";

    static final String GROUPNAME1 = "Тестовая группа 1";
    static final String GROUPNAME2 = "Тестовая группа 2";
    static final String GROUPNAME3 = "Тестовая группа 3";
    static final String GROUPNAME4 = "Тестовая группа 4";
    static final String GROUPNAME5 = "Тестовая группа 5";
    static final String GROUPNAME6 = "Тестовая группа 6";

    @Test
    void constructorTest(){
        assertThrows(NoSuchMechanismException.class, ()-> new Deanery("XML"));
            }


    @Test
    void setRegisterFromFileRandomly() {
        RegisterFromJSONRandomly regJSON = new RegisterFromJSONRandomly(deanery1);
        deanery1.setRegisterFromFileRandomly(regJSON);
        assertEquals(deanery1.registerFromFileRandomly, regJSON);

       RegisterFromTXTRandomly regTXT = new RegisterFromTXTRandomly(deanery1);
       deanery1.setRegisterFromFileRandomly(regTXT);
       assertEquals(deanery1.registerFromFileRandomly, regTXT);

       RegisterFromSourceTXTRandomly sourceTXT = new RegisterFromSourceTXTRandomly(deanery1);
       deanery1.setRegisterFromFileRandomly(sourceTXT);
       assertEquals(deanery1.registerFromFileRandomly, sourceTXT);



    }
   @BeforeAll
   static void createGroup() {
        try {deanery1.createGroup(GROUPNAME1);
            deanery1.createGroup(GROUPNAME2);
            deanery1.createGroup(GROUPNAME3);
            deanery1.createGroup(GROUPNAME5);
        } catch (GroupAlreadyExistsException e){e.printStackTrace();}

      ArrayList<String> grNames = new ArrayList<>();
      deanery1.getGroupList().forEach(group -> grNames.add(group.getGroupName()));
      assertNotEquals(true, grNames.contains(GROUPNAME4));


   }

   @Test
   void createExistingGroup(){
        assertThrows(GroupAlreadyExistsException.class, ()-> deanery1.createGroup(GROUPNAME1));
   }

    @BeforeEach
    void registerStudentsFromFileRandomly() {

       deanery1.registerStudentsFromFileRandomly(null);
       assertNotNull(deanery1.getGroupList());
       deanery1.getGroupList().forEach( group -> assertTrue(group.getGroupName().startsWith("Тестовая группа")));

       deanery1.getGroupList().forEach(group -> group.getStudentList().forEach(student -> {
          assertNotNull(student.getName());
       assertNotEquals(0, student.getId());} ));
    }




    @Test
    void removeGroup() { int numberOfRemainGroups = 3;
        deanery1.removeGroup(GROUPNAME5);
        assertEquals(numberOfRemainGroups, deanery1.getGroupList().size());

    }

    @Test
    void removeNotExistingGroup(){
        assertThrows(NoSuchElementException.class, ()-> deanery1.removeGroup(GROUPNAME6));

    }



    @Test
    void removeStudent() {

        deanery1.register(GROUPNAME1, FIO1);
        deanery1.removeStudent(FIO1, GROUPNAME1);
        ArrayList <String> names = new ArrayList<>();
        String[] tempGroup = new String[1];
        deanery1.getGroupList().forEach(group -> {if (group.getGroupName().equals(GROUPNAME1)){
            tempGroup[0]=group.getGroupName();
            group.getStudentList().forEach( st -> names.add(st.getName()));}});
        assertTrue(tempGroup[0].equals(GROUPNAME1) && !names.contains(FIO1));

    }


    @AfterAll
    static void expelBadStudents() {
        int badMark = 2;
        int [] counter = {0};
        for (int i = 0; i < 20; i++){deanery1.setOneMarkForEachStudentInEachGroup(2);}
        deanery1.getGroupStudentTable().forEach((group, studentArrayList) -> counter[0] += studentArrayList.size());
        assertEquals(counter[0], deanery1.expelBadStudents().size()-1);

    }


    @Test
    void GiveRandomMarkForEachStudentInEachGroup() {
        int numberOfMarksToGive = 4;
        deanery1.giveRandomMarkForEachStudentInEachGroup(numberOfMarksToGive);
        deanery1.getGroupList().forEach(
                group -> group.getStudentList().forEach(
                        student ->assertEquals(numberOfMarksToGive,student.getMarksList().size())));

    }


    @Test
    void setOneMarkForStudent() {
        int mark = 4;
        deanery1.register(GROUPNAME1, FIO2);
        deanery1.setOneMarkForStudent(mark,FIO2,GROUPNAME1);
        assertEquals(mark, deanery1.getMarkListForStudent(FIO2).get(0).intValue());
        deanery1.removeStudent(FIO2, GROUPNAME1);
    }

    @Test
    void giveRandomMarkForStudent() {
        deanery1.register(GROUPNAME1, FIO2);
        deanery1.giveRandomMarkForStudent(FIO2,GROUPNAME1);
        assertNotNull(deanery1.getMarkListForStudent(FIO2));
        deanery1.removeStudent(FIO2, GROUPNAME1);
    }

    @Test
    void chooseHeadRandomlyForEachGroup() {
        int numberOfElections = 3;
        for (int i = 0; i < numberOfElections; i++ ){
            deanery1.chooseHeadRandomlyForEachGroup();
        }
        deanery1.getMapOfHeadsForEachGroup().forEach((group, head)-> assertNotNull(head));
    }

    @Test
    void getHeadForGroup() {
        deanery1.chooseHeadRandomlyForEachGroup();
        deanery1.getMapOfHeadsForEachGroup().forEach((group, head)->
                assertEquals(head, deanery1.getHeadForGroup(group)));

    }

    @Test
    void getListOfGroupsStudentsFieldsAsJSON() {
        int [] realNumberOfGroups = {deanery1.getGroupList().size()};
        int [] expectedNumberOfStudents ={0};
        int [] expNumOfGroups = {0};
        ArrayList <String> expGroupNames = new ArrayList<>();

        int numberOfMarksToGive = 4;
        deanery1.giveRandomMarkForEachStudentInEachGroup(numberOfMarksToGive);

        deanery1.getGroupList().forEach(group ->{
            expGroupNames.add(group.getGroupName());
            group.getStudentList().forEach(student -> {
            expectedNumberOfStudents[0]++;
        });});
        expNumOfGroups [0] = expGroupNames.size();

        Object expectedObj = deanery1.getListOfGroupsStudentsFieldsAsJSON();
        assertTrue(expectedObj instanceof JSONObject);
        if (expectedObj instanceof JSONObject outputObj){
            deanery1.getGroupStudentTable().forEach((expgroup, expstudent)->{
                if (expNumOfGroups[0]>0  ){
                    if (expectedNumberOfStudents[0]>0) {
                        for (int i[] = {1}; i[0] <= realNumberOfGroups[0]; i[0]++) {
                            JSONObject jsonob1 = outputObj.getJSONObject("группа " + i[0]);
                            if (jsonob1.has(expgroup.getGroupName())) {
                                for (int j[] = {1}; j[0] <= expgroup.getStudentList().size(); j[0]++ ){
                                    JSONObject jsonobj2 = jsonob1.getJSONObject(expgroup.getGroupName());
                                    if (jsonobj2.has("студент" +" " + j[0])) {
                                        expstudent.forEach((student) -> {

                                            JSONObject jsonobj3 = jsonobj2.getJSONObject("студент" +" " + j[0]);
                                            if (jsonobj3.getString("имя").equals(student.getName()) &&
                                                    Arrays.equals(jsonobj3.getJSONArray("оценки").toList().stream().mapToInt(f-> (int) f).toArray(),
                                                            student.getMarksList().stream().mapToInt(m->m).toArray()) &&
                                                    jsonobj3.getFloat("средняя оценка") == student.getAverageMark())

                                            expectedNumberOfStudents[0]--;
                                            });

                                    }

                                }
                                expNumOfGroups[0]--;
                            }

                        }
                    }

                }

            });

        }

        assertEquals(0, expectedNumberOfStudents[0]);
        assertEquals(0, expNumOfGroups[0]);


    }

}