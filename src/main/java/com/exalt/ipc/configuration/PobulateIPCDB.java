package com.exalt.ipc.configuration;

import com.exalt.ipc.file.File;
import com.exalt.ipc.file.FileRepository;
import com.exalt.ipc.ipc.IPC;
import com.exalt.ipc.ipc.IPCRepository;
import com.exalt.ipc.job.Job;
import com.exalt.ipc.press.Press;
import com.exalt.ipc.press.PressRepository;
import com.exalt.ipc.press.queues.*;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
public class PobulateIPCDB {
    private static Logger logger = LoggerFactory.getLogger(PobulateIPCDB.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    IPCRepository ipcRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    PressRepository pressRepository;
    @Autowired
    HeldQueueRepository heldQueueRepository;
    @Autowired
    PrintingQueueRepository printingQueueRepository;
    @Autowired
    ReturnedQueueRepository returnedQueueRepository;

    @Bean
    public CommandLineRunner populateDB() {
        return new CommandLineRunner() {
            @Override
            @Transactional
            //@Transactional is very important here as it keeps the session opened until the method is executed
            //so if any lazy data is loaded within this method, it can be used, also it solves the problem of detached objects
            public void run(String... args) throws Exception {
                User user1 = new User("mohammad@gmail.com", "Mohammad", "Hasan", "123456", "ROLE_USER");
                IPC ipc1 = new IPC(LocalDateTime.now(), 20);
                ipc1.setUser(user1);
                Press press1 = new Press(LocalDateTime.now(), "Handle ReactJs pdfs printing");
                press1.setUser(user1);
                press1.setIpc(ipc1);
                PressQueue pressQueue1 = new PressQueue();
                pressQueue1.setPress(press1);
                HeldQueue held1 = new HeldQueue(pressQueue1, 100_000_000);
                heldQueueRepository.save(held1);
                Job job1 = new Job("Printing docs 1", LocalDateTime.now(), "Staging");
                File file1 = new File(job1);
                file1.setFileInfo("1.pdf", "application/pdf", 1_000_000, new byte[]{1, 2, 3});
//                fileRepository.save(file1);
                file1.setUser(user1);
                file1.setIpc(ipc1);
                fileRepository.save(file1);
                log(userRepository);
                log(ipcRepository);
                log(fileRepository);
                log(pressRepository);
                log(heldQueueRepository);
                logObject(pressRepository.findById(1).get().getUser());
            }
        };
    }


    public void log(CrudRepository repository) {
        repository.findAll().forEach((row) -> logger.info(row.toString()));
    }

    public void logObject(Object object) {
        logger.info(object.toString());
    }
}