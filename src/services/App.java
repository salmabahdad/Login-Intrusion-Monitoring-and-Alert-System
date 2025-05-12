package services;

import java.io.*;


public class App {

public  void tailFile(File file) throws IOException, InterruptedException {
    long lastKnownPosition = 0;

    while (true) {
        long fileLength = file.length();

        if (fileLength > lastKnownPosition) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.seek(lastKnownPosition);
                String line;
                while ((line = raf.readLine()) != null) {
                    LogAnalyzer.analyzeLogLine(line);
                }
                lastKnownPosition = raf.getFilePointer();
            }
        }

        Thread.sleep(1000);
    }
}
}