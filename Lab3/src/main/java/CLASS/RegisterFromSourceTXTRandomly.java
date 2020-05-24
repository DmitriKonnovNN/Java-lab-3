package CLASS;
import INTERFACES.*;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterFromSourceTXTRandomly implements IRegisterFromFileRandomly {
    IDeanery deanery;

    public RegisterFromSourceTXTRandomly(IDeanery deanery){
        this.deanery = deanery;
    }

    @Override
    public void registerFromFileRandomly(Object object) {

            int groupNumber = deanery.getGroupStudentTable().size();
            try {
                if (groupNumber == 0) {
                    throw new Exception("No groups to register students in!");
                }
                JSONObject jsonObj = JSONtxtToJsonFIO.createJSONfromTXT(true);
                int studentsNumber = jsonObj.getInt("количество студентов");
                ArrayList<IGroup> tempGroupList = deanery.getGroupList();


                for (int i = 1; i <= studentsNumber; i++) { // == IntStream.rangeClosed(1,studentsNumber).forEach((i)->{
                    JSONObject student = jsonObj.getJSONObject("студент " + i);
                    String name = student.getString("фамилия") + " " + student.getString("имя") + " " + student.getString("отчество");
                    deanery.register((tempGroupList.get((int) (Math.random() * groupNumber))).getGroupName(), name);
                }
            }catch (Exception e) {e.printStackTrace();}


    }


}
