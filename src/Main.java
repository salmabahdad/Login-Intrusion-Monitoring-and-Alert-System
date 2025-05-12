import services.LogAnalyzer;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <log-file>");
            return;
        }

        String logFilePath = args[0];
        LogAnalyzer.analyzeLogs(logFilePath);
    }
}
