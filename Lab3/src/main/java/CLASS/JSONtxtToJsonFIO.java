package CLASS;

import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class JSONtxtToJsonFIO {

    public static JSONObject createJSONfromTXT(boolean prettyprint){
        JSONObject studentlist = new JSONObject();
        URL resource = JSONtxtToJsonFIO.class.getResource("/names.txt");
        try (BufferedReader bufRr = new BufferedReader(new FileReader(new File(resource.toURI())))){
            String line = null;
            int studentCounter = 0;

            while( (line = bufRr.readLine())!= null) {
                studentCounter++;
                String [] FIOarray = line.split("\\s+");
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
                studentlist.put("студент " + studentCounter,student);

            }
            studentlist.put("количество студентов", studentCounter);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return studentlist;
    }

}