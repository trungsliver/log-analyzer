package thread;


import concurrency.ReadCounter;
import model.LogResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class FileReaderTask implements Callable<LogResult> {
    private Path filePath;
    private ReadCounter counter;

    public FileReaderTask(Path filePath, ReadCounter counter) {
        this.filePath = filePath;
        this.counter = counter;
    }

    @Override
    public LogResult call() throws Exception {
        try {
            String content = Files.readString(filePath);
            int wordCount = content.split("\\s+").length;
            int keywordCount = content.split("error", -1).length - 1; // ví dụ đếm từ "error"

            counter.increment(); // cập nhật số lần đọc
            return new LogResult(filePath.getFileName().toString(), wordCount, keywordCount, LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}