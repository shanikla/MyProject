package com.myproject.stream;

import com.myproject.utils.DataCounter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamHandler implements Runnable {

    private final Logger log = LoggerFactory.getLogger(StreamHandler.class);

    @Override
    public void run() {
        log.info("initiating observable");
        Observable<String> observable = Observable.create(emitter -> {
            try {
                // get the current executable by the OS.
                final String executable = getExecutable();
                if (executable != null) {
                    ProcessBuilder pb = new ProcessBuilder(getExecutable());
                    Process process = pb.start();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        emitter.onNext(line);
                        if (Thread.currentThread().isInterrupted()) {
                            log.info("stopping observable");
                            // thread has stopped - dispose
                            emitter.onComplete();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        Disposable disposable =
                observable.subscribe(t -> {
                    log.debug("handling event: " + t);
                    DataCounter.getInstance().handleEvent(t);
                });

        disposable.dispose();
    }

    private String getExecutable() {
        final String os = System.getProperty("os.name").toLowerCase();
        final String executablesPath = this.getClass().getClassLoader().getResource("executables").getPath();
        String executablePath = null;

        if (os.contains("win")) {
            executablePath = executablesPath + File.separator + "generator-windows-amd64.exe";
        } else {
            try {
                String[] command = {"chmod", "-R", "+x", executablesPath};
                Runtime rt = Runtime.getRuntime();
                rt.exec(command).waitFor();

                if (os.contains("nux")) {
                    // linux OS
                    executablePath = executablesPath + File.separator + "generator-linux-amd64.dms";
                } else if (os.contains("mac")) {
                    // MAC OS
                    executablePath = executablesPath + File.separator + "generator-macosx-amd64";
                }
            } catch (IOException | InterruptedException e) {
                log.error("problem finding executables", e);
            }
        }
        return executablePath;
    }
}