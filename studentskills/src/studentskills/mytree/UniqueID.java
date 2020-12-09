package studentskills.mytree;

public class UniqueID {
    private static int idCount = 0;
    public static int getUniqueID(){
        return idCount++;
    }
}
