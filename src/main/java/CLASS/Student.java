package CLASS;

import INTERFACES.IGroup;
import INTERFACES.IStudent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class Student implements IStudent {
    final private String name;
    IGroup group;
    final private int id;
    private ArrayList<Integer> MarksList = new ArrayList<>();
    public boolean isHead = false;
    private Integer INIT_HASH_CODE;

    public Student (String name) {
        this.name = name;
        this.id = idGenerator.getNextId();


    }
    public Student (String name, IGroup group) {
        this.name = name;
        this.id = idGenerator.getNextId();
        this.group = group;
        group.register(this);

    }
    public Student (String name, int id ,IGroup group) {
        this.name = name;
        this.id = id;
        this.group = group;
        group.register(this);

    }

   public static IStudent generate (String name){
        return new Student(name);
    }


    @Override
    public void join(IGroup group) {
        resetHead();
        this.group = group;
        group.register(this);

    }

    @Override
    public void leave() {
        if (group != null) {resetHead();
            group.remove(this);}
    }

    @Override
    public void update(ArrayList<Integer> marks, boolean isHead, IGroup group) {
        MarksList = marks;
        this.isHead = isHead;
        this.group = group;
    }

    @Override
    public ArrayList<Integer> getMarksList() {
        return MarksList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void resetHead() {
        isHead = false;
    }

    @Override
    public boolean isHead() {

        return isHead;
    }

    @Override
    public boolean equals (Object o) {
       if (this == o) return true; // if both invoking object and passed object point to the same memory location.
       if (o == null) return false;
       if (!(o instanceof Student student)) return false; // new syntax' alive!
       return id == student.id &&
               name.equals(student.name) &&
               Objects.equals(group, student.group);

   }

    @Override
    public int hashCode() {
       if (INIT_HASH_CODE == null) {INIT_HASH_CODE = Objects.hash(invokeAllGettersAndBooleans());}
        return INIT_HASH_CODE;
    }

    private ArrayList invokeAllGettersAndBooleans (){
       Method [] methodsList = this.getClass().getMethods();
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

    @Override
    public float getAverageMark() {
        float avrMark;
        if (MarksList.isEmpty()) return 0;
        return (float)MarksList.stream().mapToInt(i -> i).average().getAsDouble();
    }


}
