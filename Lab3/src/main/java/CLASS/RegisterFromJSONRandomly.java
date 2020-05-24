package CLASS;
import INTERFACES.*;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterFromJSONRandomly implements IRegisterFromFileRandomly {
    IDeanery deanery;
    public RegisterFromJSONRandomly (IDeanery deanery) {
        this.deanery = deanery;

    }
    @Override
    public void registerFromFileRandomly(Object object) {
        if (object instanceof JSONObject ) {
            var jsonobj = (JSONObject)object;
            int studentsNumber = jsonobj.getInt("количество студентов");
            ArrayList<IGroup> tempGroupList = deanery.getGroupList();
            int groupNumber = deanery.getGroupStudentTable().size();

            for (int i = 1; i <= studentsNumber; i++) { // == IntStream.rangeClosed(1,studentsNumber).forEach((i)->{
                JSONObject student = jsonobj.getJSONObject("студент " + i);
                String name = student.getString("фамилия") + " " + student.getString("имя") + " " + student.getString("отчество");
                deanery.register((tempGroupList.get((int) (Math.random() * groupNumber))).getGroupName(), name);
            }
        }

    }


}
