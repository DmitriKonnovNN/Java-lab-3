package CLASS;

public class Main {
    public static void main(String[] args){

        Deanery deanery = new Deanery();


        try {deanery.createGroup("Математика");
            deanery.createGroup("Физика");
            deanery.createGroup("Химия");
            deanery.createGroup("Физкультура");}
        catch (GroupAlreadyExistsException e) {e.printStackTrace();}

        deanery.registerStudentsFromFileRandomly(null);
        System.out.println(deanery.getGroupList());

        deanery.chooseHeadRandomlyForEachGroup();
        System.out.println(deanery.getMapOfHeadsForEachGroup());

        deanery.giveRandomMarkForEachStudentInEachGroup(6);
        System.out.println(deanery.getMapOfHeadsForEachGroup());


        System.out.println(deanery.expelBadStudents().toString());

        System.out.println(deanery.getListOfGroupsStudentsFieldsAsJSON());

    }
}
