package CLASS;

public class idGenerator {
    private static Integer lastId = 0;

    public static Integer getNextId() {
        return ++lastId;
    }
}

