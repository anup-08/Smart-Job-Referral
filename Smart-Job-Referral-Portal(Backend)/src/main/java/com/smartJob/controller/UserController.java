package com.smartJob.controller;

import com.smartJob.dtos.*;
import com.smartJob.enums.Role;
import com.smartJob.service.JobService;
import com.smartJob.service.ReferralService;
import com.smartJob.service.UserService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController { // For Employee
    private final UserService userService;
    private final ReferralService referralService;
    private final JobService jobService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> addDetails(@Valid @RequestBody UserRequestDto requestDto){
        requestDto.setRole(Role.USER);
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
        System.out.println("TokenToken out");
        return  ResponseEntity.ok(Map.of("accessToken" , token , "refreshToken" , rToken));

    }

    @PostMapping("/getToken")
    public ResponseEntity<?> getNewToken(@RequestBody Map<String,String> tokenInfo){
        System.out.println("refresh Token in");
        String rToken = tokenInfo.get("refreshToken");
        String accessToken = userService.generateNewJwtToken(rToken);
        System.out.println("refresh Token out");
        return ResponseEntity.ok(Map.of("accessToken" , accessToken));
    }

    @GetMapping("/allJobList")
    public ResponseEntity<List<JobResponseDto>> getAllJob(){
        return new ResponseEntity<>(jobService.getAllJobList() , HttpStatus.OK);
    }

    @PostMapping(value = "/referral" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ReferralResponseDto> referral(@Valid @RequestParam("candidateName") String candidateName,
                                                        @RequestParam("candidateEmail") String candidateEmail,
                                                        @RequestParam("jobId") Long jobId,
                                                        @RequestParam(value = "resume", required = false) MultipartFile resume, Principal principal) throws IOException {
        String email = principal.getName();
        ReferralRequestDto requestDto = new ReferralRequestDto();
        requestDto.setCandidateName(candidateName);
        requestDto.setCandidateEmail(candidateEmail);
        requestDto.setJobId(jobId);
        return new ResponseEntity<>(referralService.referral(requestDto ,resume , email) , CREATED);
    }

    @GetMapping("/searchJob")
    public ResponseEntity<List<JobResponseDto>> getJobByTitle(@RequestParam String title){
        return new ResponseEntity<>(jobService.searchJob(title) , HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUser(Principal principal){
        String email = principal.getName();
        return ResponseEntity.ok(userService.getUser(email));
    }

    @GetMapping("/getReferralDetails")
    public ResponseEntity<List<ReferralResponseDto>> getReferralDetails(Principal principal){
        return ResponseEntity.ok(referralService.getReferralData(principal.getName()));
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

    @PutMapping(value = "/updateDoc/{referralId}" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateFile(@PathVariable Long referralId , @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(referralService.updateDoc(referralId,file));
    }
}
