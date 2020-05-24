package CLASS;

import INTERFACES.IDeanery;
import INTERFACES.IGroup;
import INTERFACES.IRegisterFromFileRandomly;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class RegisterFromTXTRandomly implements IRegisterFromFileRandomly {
    IDeanery deanery;

    public RegisterFromTXTRandomly(IDeanery deanery){
        this.deanery = deanery;
    }

    @Override
    public void registerFromFileRandomly(Object object) {
        if (object instanceof File ) {
            var file = (File)object;

        int groupNumber = deanery.getGroupStudentTable().size();
        try {
            if (groupNumber == 0) {
                throw new Exception("No groups to register students in!");
            }
            boolean prettyprint = true;
            JSONObject studentlist = new JSONObject();
            try (BufferedReader bufRr = new BufferedReader(new FileReader(file))) {
                String line = null;
                int studentCounter = 0;

                while ((line = bufRr.readLine()) != null) {
                    studentCounter++;
                    String[] FIOarray = line.split("\\s+");
                    JSONObject student = new JSONObject();
                    switch (FIOarray.length) {
                        case 3 : {
                            student.put("фамилия", FIOarray[0]);
                            student.put("имя", FIOarray[1]);
                            student.put("отчество", FIOarray[2]);
                            break;
                        }
                        case 2 : {
                            student.put("фамилия", FIOarray[0]);
                            student.put("имя", FIOarray[1]);
                            break;
                        }
                        default : student.put("имя", FIOarray);
                    }
                    studentlist.put("студент " + studentCounter, student);

                }
                studentlist.put("количество студентов", studentCounter);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int studentsNumber = studentlist.getInt("количество студентов");
            ArrayList<IGroup> tempGroupList = deanery.getGroupList();

            for (int i = 1; i <= studentsNumber; i++) { // == IntStream.rangeClosed(1,studentsNumber).forEach((i)->{
                JSONObject student = studentlist.getJSONObject("студент " + i);
                String name = student.getString("фамилия") + " " + student.getString("имя") + " " + student.getString("отчество");
                deanery.register((tempGroupList.get((int) (Math.random() * groupNumber))).getGroupName(), name);
            }
        } catch (Exception e) {e.printStackTrace();}

        }
    }

}
