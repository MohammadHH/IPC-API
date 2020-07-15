package com.exalt.ipc.press;

import com.exalt.ipc.helpers.HelperSerivce;
import com.exalt.ipc.job.File;
import com.exalt.ipc.job.FileService;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.user.User;
import com.exalt.ipc.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.exalt.ipc.configuration.Constants.HEADER_STRING;

@RestController
public class PressController {
    @Autowired
    PressService pressService;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private HelperSerivce helperSerivce;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "Add a press container")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Press controller is successfully created", response = Press.class),
            @ApiResponse(code = 400, message = "Non well-formed json")
    })
    @PostMapping(value = "/v1/users/loggedIn/press", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPress(
            @ApiParam(value = "Jwt Bearer", required = true) @RequestHeader(HEADER_STRING) String jwt,
            @ApiParam(value = "Press simple description", required = false) @RequestBody Map<String, String> description,
            HttpServletRequest request
    ) {
        User user = userService.getUser(jwt, request);
        Press savedPress = pressService.storePress(user, description.get("description"));
        return ResponseEntity.created(URI.create("http:localhost:8080/" + savedPress.getId())).body(savedPress);
    }


    @ApiOperation(value = "Add a press container")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Press controller is successfully created", response = Press.class),
            @ApiResponse(code = 400, message = "Non well-formed json")
    })
    @PostMapping("/v1/presses/{press}/map")
    public ResponseEntity mapPress(
            @ApiParam(value = "Jwt Bearer", required = true) @RequestHeader(HEADER_STRING) String jwt,
            @ApiParam(value = "pressId", required = true) @PathVariable int press,
            HttpServletRequest request
    ) {
        User user = userService.getUser(jwt, request);
        Press p = pressService.getPress(press, request);
        helperSerivce.isOwner(helperSerivce.getEmail(jwt), p, request);
        return ResponseEntity.ok(pressService.map(p, user, request));
    }

    @PostMapping("/v1/presses/{press}/unmap")
    public ResponseEntity unmapPress(@RequestHeader(HEADER_STRING) String jwt, @PathVariable int press, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press p = pressService.getPress(press, request);
        helperSerivce.isOwner(helperSerivce.getEmail(jwt), p, request);
        return ResponseEntity.ok(pressService.unmap(p, request));
    }

    @PostMapping("/v1/presses/{press}/addJob")
    public File addJobToPress(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, @RequestParam("job") int jobId, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        File job = fileService.getJob(jobId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        helperSerivce.isOwner(user.getEmail(), job, request);
        return pressService.addJob(press, job, request);
    }

    @PostMapping("/v1/presses/{press}/addJobs")
    public List<File> addJobsToPress(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, @RequestBody Map<String, Integer[]> fileIDs, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        Integer[] IDs = fileIDs.get("IDs");
        List<File> jobs = new LinkedList<>();
        Arrays.stream(IDs).forEach((id) -> {
            File job = fileService.getJob(id, request);
            helperSerivce.isOwner(user.getEmail(), job, request);
            jobs.add(job);
        });
        return pressService.addJobs(press, jobs, request);
    }

    @PostMapping("/v1/presses/{press}/print")
    public File printJob(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        return pressService.printJob(press, request);
    }

    @PostMapping("/v1/presses/{press}/printAll")
    public List<File> printAllJob(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        return pressService.printAllJobs(press, request);
    }

    @GetMapping("/v1/presses/{press}/info")
    public Map<String, Object> printPressInfo(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        return pressService.getPressInfo(press, request);
    }

    @GetMapping("/v1/presses/{press}")
    public Press getPress(@RequestHeader(HEADER_STRING) String jwt, @PathVariable("press") int pressId, HttpServletRequest request) {
        User user = userService.getUser(jwt, request);
        Press press = pressService.getPress(pressId, request);
        helperSerivce.isOwner(user.getEmail(), press, request);
        return pressService.getPress(pressId, request);
    }

}
