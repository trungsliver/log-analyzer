package service;

import concurrency.ReadCounter;
import db.DatabaseManager;
import model.LogResult;
import thread.FileReaderTask;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LogAnalyzerService {
    private DatabaseManager dbManager = new DatabaseManager();
    private ReadCounter counter = new ReadCounter();

    public void analyzeLogs(String folderPath) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<LogResult>> futures = new ArrayList<>();

        try {
            Files.list(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> futures.add(executor.submit(new FileReaderTask(file, counter))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LogResult> results = new ArrayList<>();
        for (Future<LogResult> f : futures) {
            try {
                LogResult r = f.get();
                if (r != null) results.add(r);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        // Lưu batch
        dbManager.saveBatch(results);
        System.out.println("✅ Tổng số file đã đọc: " + counter.getCount());
    }

    public void showResults() {
        dbManager.showAll();
    }
}

