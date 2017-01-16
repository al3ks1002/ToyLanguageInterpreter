package utils.generators;

public class HeapAddressGenerator {
    private static int counter = 0;

    public static int getAddress() {
        return counter++;
    }
}
