package com.exalt.ipc;

import com.exalt.ipc.file.FileRepository;
import com.exalt.ipc.ipc.IPCRepository;
import com.exalt.ipc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IpcApplication {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IPCRepository ipcRepository;
    @Autowired
    private FileRepository fileRepository;

    public static void main(String[] args) {
        SpringApplication.run(IpcApplication.class, args);
    }
}
