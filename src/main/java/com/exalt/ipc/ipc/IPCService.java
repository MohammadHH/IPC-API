package com.exalt.ipc.ipc;

import com.exalt.ipc.helpers.HelperSerivce;
import com.exalt.ipc.job.File;
import com.exalt.ipc.job.FileRepository;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.exalt.ipc.job.States.UPLOADED;

@Service
public class IPCService {
    @Autowired
    LocaleService localeService;
    @Autowired
    IPCRepository ipcRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    UserService userService;
    @Autowired
    HelperSerivce helperSerivce;

    @Transactional
    public IPC getIPC(int userId) {
        return ipcRepository.findByUserId(userId).get();
    }

    @Transactional
    public Map<String, Object> getIPCMap(String jwt, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Map<String, Object> map = user.getIpc().getIPCMap();
        List<File> files = fileRepository.findByStateAndUserId(UPLOADED, user.getId());
        map.put("residual", user.getIpc().getQueueLimit() - files.size());
        return map;
    }


    @Transactional
    public IPC createIPC(User user) {
        IPC ipc = new IPC();
        ipc.setUser(user);
        return ipcRepository.save(ipc);
    }
}
