package com.smartJob.controller;

import com.smartJob.dtos.*;
import com.smartJob.entity.Referral;
import com.smartJob.enums.Role;
import com.smartJob.service.JobService;
import com.smartJob.service.ReferralService;
import com.smartJob.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor

public class AdminController {

    private final JobService jobService;
    private final UserService userService;
    private final ReferralService referralService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDto requestDto){

        requestDto.setRole(Role.ADMIN);
        UserResponseDto dto = userService.saveUser(requestDto) ;
        String token = userService.generateToken(dto.getEmail());
        String rToken = userService.generateRefreshToken(dto.getEmail());
        return  ResponseEntity.ok(Map.of("accessToken" , token , "refreshToken" , rToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody Map<String , String> loginInfo){

        if (loginInfo.get("userName") == null || loginInfo.get("password") == null) {
            return ResponseEntity.badRequest().body("UserName and password are required");
        }

        String username = loginInfo.get("userName");
        String password = loginInfo.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        String token = userService.generateToken(username);
        String rToken = userService.generateRefreshToken(username);
        return  ResponseEntity.ok(Map.of("accessToken" , token , "refreshToken" , rToken));

    }

    @PostMapping("/getToken")
    public ResponseEntity<?> getNewToken(@RequestBody Map<String,String> tokenInfo){
        String rToken = tokenInfo.get("refreshToken");
        String accessToken = userService.generateNewJwtToken(rToken);
        return ResponseEntity.ok(Map.of("accessToken" , accessToken));
    }


    @PostMapping("/addJob")
    public ResponseEntity<JobResponseDto> addJob(@Valid @RequestBody JobRequestDto requestDto , Principal principal){
        return new ResponseEntity<>(jobService.addJob(requestDto , principal.getName()) , HttpStatus.OK);
    }

    @GetMapping("/seeReferral/{jobId}")
    public ResponseEntity<List<ReferralResponseDto>> getReferralDetails(@PathVariable Long jobId){
        return new ResponseEntity<>(referralService.getReferralDetail(jobId) , HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
        return new ResponseEntity<>(true , HttpStatus.OK);
    }

    @GetMapping("/allJobList")
    public ResponseEntity<List<JobResponseDto>> getAllJob(){
        return new ResponseEntity<>(jobService.getAllJobList() , HttpStatus.OK);
    }

    @GetMapping("/searchJob")
    public ResponseEntity<List<JobResponseDto>> getJobByTitle(@RequestParam String title){
        return new ResponseEntity<>(jobService.searchJob(title) , HttpStatus.OK);
    }

    @PostMapping("/setStatus/{id}")
    public ResponseEntity<ReferralResponseDto> setReferralStatus(@PathVariable Long id , @RequestBody UpdateReferralStatusRequest status){
        Referral upgrade = referralService.setReferralStatus(id,status.getNewStatus());
        return new ResponseEntity<>(referralService.convertReferral(upgrade) , HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUser(Principal principal){
        String email = principal.getName();
        return ResponseEntity.ok(userService.getUser(email));
    }

    @GetMapping("/getPostedJobs")
    public ResponseEntity<List<JobResponseDto>> getPostedJobs(Principal principal){
        return new ResponseEntity<>(jobService.getPostedJobs(principal.getName()) , HttpStatus.OK);
    }

    @GetMapping("/getPostedJobReferral")
    public ResponseEntity<List<ReferralResponseDto>> getPostedReferral(Principal principal){
        return ResponseEntity.ok(referralService.getPostedJobReferral(principal.getName()));
    }

    @GetMapping("/referralData/{referralId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long referralId) throws IOException {
        // 1. Get the Resource from the service
        Resource resource = referralService.getDocumentByReferralId(referralId);

        // 2. Set content type. Forcing PDF since that's all you allow.
        String contentType = "application/pdf";

        // 3. Build and return the ResponseEntity using methods available on the Resource interface
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
