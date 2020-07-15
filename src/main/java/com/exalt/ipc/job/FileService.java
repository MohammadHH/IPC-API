package com.exalt.ipc.job;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.ipc.IPCRepository;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.exalt.ipc.job.States.UPLOADED;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IPCRepository ipcRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private LocaleService localeService;

    public static final String mimes[] = {
            "application/pdf",
            "image/jpeg",
            "image/png"
    };

    @Transactional
    private File uploadFile(User user, MultipartFile file) throws Exception {
        fileStorageService.storeFile(file);
        return fileRepository.save(new File(UPLOADED, user, file));
    }

    public int getResidualFiles(User user) {
        return ipcRepository.findById(user.getIpc().getId()).get().getQueueLimit() - fileRepository.findByStateAndUserId(UPLOADED, user.getId()).size();
    }

    @Transactional
    public List<File> storeFiles(User user, MultipartFile[] files, HttpServletRequest request) {
        List<File> savedFiles = new ArrayList<>();
        if (getResidualFiles(user) >= files.length)
            Arrays.stream(files).forEach(file -> {
                try {
                    savedFiles.add(uploadFile(user, file));
                } catch (Exception e) {
                }
            });
        else throw new CustomException("7010", request, HttpStatus.BAD_REQUEST, localeService, 7011);
        return savedFiles;
    }

    public boolean validateFile(MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty())
            throw new CustomException("7016", request, HttpStatus.BAD_REQUEST, localeService);
        if (!Arrays.stream(mimes).anyMatch(mime -> file.getContentType().equals(mime)))
            throw new CustomException("7014", request, HttpStatus.BAD_REQUEST, localeService, 7015);
        return true;
    }

    public boolean validateFiles(MultipartFile[] files, HttpServletRequest request) {
        Arrays.stream(files).forEach((file) -> validateFile(file, request));
        return true;
    }

    public List<File> getUploaded(User user) {
        return fileRepository.findByStateAndUserId(UPLOADED, user.getId());
    }

    public List<File> getAllJobs(User user) {
        return fileRepository.findByUserId(user.getId());
    }

    public List<File> getJobs(String state, User user) {
        return fileRepository.findByStateAndUserId(state, user.getId());
    }

    public File getJob(int jobId, HttpServletRequest request) {
        CustomException ex = new CustomException("7030", request, HttpStatus.NOT_FOUND, localeService, 7032);
        File job = fileRepository.findById(jobId).orElseThrow(() -> ex);
        return job;
    }
}
