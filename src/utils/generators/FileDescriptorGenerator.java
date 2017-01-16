package utils.generators;

public class FileDescriptorGenerator {
    private static int counter = 0;

    public static int getDescriptor() {
        return counter++;
    }
}
