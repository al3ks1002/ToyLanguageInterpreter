package utils.generators;

public class ProgramStateIdGenerator {
    private static int counter = 0;

    public static int getProgramStateId() {
        return counter++;
    }
}
