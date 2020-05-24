package CLASS;

import org.junit.jupiter.api.Test;

import javax.swing.text.DefaultStyledDocument;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
     static Deanery deanery1 = new Deanery ();
    static final String FIO1 = "Тестовый Студент 1";
    static final String FIO2 = "Тестовый Студент 2";
    static final String FIO3 = "Тестовый Студент 3";
    static final String FIO4 = "Тестовый Студент 4";

    static final String GROUPNAME1 = "Тестовая группа 1";
    static final String GROUPNAME2 = "Тестовая группа 2";
    static final String GROUPNAME3 = "Тестовая группа 3";

    static Group group1 = new Group(GROUPNAME1, deanery1);
    static Group group2 = new Group(GROUPNAME2, deanery1);
    static Group group3 = new Group(GROUPNAME3, deanery1);

    static Student testStudent1 = new Student(FIO1);
    static Student testStudent2 = new Student(FIO2);
    static Student testStudent3 = new Student(FIO3);



    @Test
    void generate() {
        assertEquals(FIO4, Student.generate(FIO4).getName());
    }

    @Test
    void join() {
        testStudent1.join(group1);
        testStudent2.join(group2);
        testStudent3.join(group3);
        assertEquals(group1,testStudent1.group );
        assertEquals(group2,testStudent2.group );
        assertEquals(group3,testStudent3.group );
        testStudent1.join(group2);
        assertNotEquals(group1,testStudent1.group );
        assertEquals(group2,testStudent1.group );

    }

    @Test
    void leave() {
        testStudent1.join(group1);
        testStudent2.join(group2);
        testStudent3.join(group3);
        testStudent1.leave();
        testStudent2.leave();
        testStudent3.leave();
        assertEquals(testStudent1.group, null);
        assertEquals(testStudent2.group, null);
        assertEquals(testStudent3.group, null);
    }

    @Test
    void getMarksList() {
        testStudent1.join(group1);
        ArrayList<Integer>ml = new ArrayList<>();
        for (int i = 1; i < 11; i++ ){
            ml.add(i);
        }
        for (Integer integer : ml) {
          group1.setOneMarkForStudent(integer, testStudent1);
        }

        assertEquals(ml, testStudent1.getMarksList());

    }

    @Test
    void getName() {
        assertEquals(FIO1, testStudent1.getName());

    }

    @Test
    void getId() {
        assertEquals(1, testStudent1.getId());
        assertEquals(2, testStudent2.getId());
        assertEquals(3, testStudent3.getId());

    }

    @Test
    void resetHead() {
        testStudent1.isHead = true;
        testStudent1.resetHead();
        assertEquals(false, testStudent1.isHead);

    }

    @Test
    void setHead() {
        testStudent1.isHead = true;
        assertEquals(true, testStudent1.isHead);
    }

    @Test
    void isHead() {
        testStudent1.isHead = true;
        assertEquals(true, testStudent1.isHead());
        testStudent1.resetHead();
        assertEquals(false, testStudent1.isHead());

    }

    @Test
    void testEquals() {
        assertEquals(true,testStudent1.equals(testStudent1));
        assertEquals(false,testStudent1.equals(testStudent2));
    }

    @Test
    void testHashCode() {
        int initHC = testStudent1.hashCode();
        testStudent1.join(group3);
        assertEquals(initHC, testStudent1.hashCode());
        assertNotEquals(testStudent1.hashCode(), testStudent2.hashCode());
    }

    @Test
    void getAverageMark() {

        testStudent1.join(group1);
        int sum = 0;
        int counter = 0;
        ArrayList<Integer>ml = new ArrayList<>();
        for (int i = 1; i < 11; i++ ){
            ml.add(i);
            sum += i;
            counter++;
        }
        float avrgM = (float)sum / counter;

        ml.forEach(integer -> group1.setOneMarkForStudent(integer, testStudent1));

        assertEquals(avrgM, testStudent1.getAverageMark());


    }
}