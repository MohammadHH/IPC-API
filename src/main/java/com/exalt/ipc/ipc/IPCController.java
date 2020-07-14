package com.exalt.ipc.ipc;

import com.exalt.ipc.helpers.HelperSerivce;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IPCController {
    @Autowired
    private IPCService ipcService;
    @Autowired
    private HelperSerivce helperSerivce;

    @ApiOperation(value = "Get a user ipc", notes = "Gets a logged user its ipc information and the residual files available for upload",
            response = IPC.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the ipc for the provided user"),
            @ApiResponse(code = 404, message = "Couldn't get the ipc associated with this user"),
    })
    @GetMapping(value = "/v1/users/loggedIn/ipc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserIPC(@ApiParam(value = "Authorization Bearer helpers",
            required = true) @RequestHeader("Authorization") String jwt, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(ipcService.getIPCMap(jwt, request));
    }


}
