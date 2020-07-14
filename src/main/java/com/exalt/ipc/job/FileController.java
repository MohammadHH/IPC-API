package com.exalt.ipc.job;

import com.exalt.ipc.exception.CustomException;
import com.exalt.ipc.helpers.HelperSerivce;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserRepository;
import com.exalt.ipc.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static com.exalt.ipc.configuration.Constants.HEADER_STRING;
import static com.exalt.ipc.job.States.getStates;


@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private HelperSerivce helperSerivce;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Upload chosen files",
            notes = "All the files should be one of these extensions: png,jpeg,pdf",
            response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded the chosen files"),
            @ApiResponse(code = 400, message = "Failed to upload the chosen files")
    })
    @PostMapping(value = "/v1/ipc/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> uploadFiles(@ApiParam(value = "Authorization Bearer helpers", required = true)
                                  @RequestHeader(HEADER_STRING) String jwt,
                                  @RequestParam("file") MultipartFile[] files,
                                  HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        if (files == null || files.length == 0)
            new CustomException("7017", request, HttpStatus.BAD_REQUEST, localeService);
        fileService.validateFiles(files, request);
        return fileService.storeFiles(user.getId(), user.getIpc().getId(), files, request);
    }


    @ApiOperation(value = "Get all ipc uploaded files", response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the uploaded files"),
            @ApiResponse(code = 400, message = "Failed to retrieve the uploaded files")
    })

    @GetMapping(value = "/v1/ipc/uploaded", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> uploadFiles(@ApiParam(value = "Authorization Bearer helpers", required = true)
                                  @RequestHeader(HEADER_STRING) String jwt,
                                  HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        return fileService.getUploaded(user.getIpc().getId());
    }

    @GetMapping(value = "/v1/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> getAllJobs(@ApiParam(value = "Authorization Bearer helpers", required = true)
                                 @RequestHeader(HEADER_STRING) String jwt,
                                 HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        return fileService.getAllJobs(user.getIpc().getId());
    }

    @GetMapping(value = "/v1/jobs/{state}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> getJobs(@ApiParam(value = "Authorization Bearer helpers", required = true)
                              @RequestHeader(HEADER_STRING) String jwt,
                              @PathVariable String state,
                              HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        if (!Arrays.stream(getStates()).anyMatch((s) -> s.equals(state)))
            throw new CustomException("7090", request, HttpStatus.BAD_REQUEST, localeService, 7091);
        return fileService.getJobs(state, user.getIpc().getId());
    }

}
