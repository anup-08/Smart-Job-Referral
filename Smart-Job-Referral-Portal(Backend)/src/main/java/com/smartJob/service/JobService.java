package com.smartJob.service;

import com.smartJob.dtos.JobRequestDto;
import com.smartJob.dtos.JobResponseDto;
import com.smartJob.entity.Job;
import com.smartJob.entity.User;
import com.smartJob.exception.NotFound;
import com.smartJob.repo.JobRepository;
import com.smartJob.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class JobService {
    private final JobRepository jobRepo;
    private final UserRepository userRepo;

    @PreAuthorize("hasRole('ADMIN')")
    public JobResponseDto addJob(JobRequestDto requestDto ,String email ){
        User user = userRepo.findByEmail(email).get();
        Job job = new Job(
                requestDto.getTitle(), requestDto.getDescription() , requestDto.getDepartment(), requestDto.getCompanyName() , requestDto.getLocation(),
                requestDto.getExperienceRequired(), new Date() , user
        );

        jobRepo.save(job);
        return new JobResponseDto(job.getId(), job.getTitle(), job.getCompanyName(), job.getDescription(), job.getDepartment(),
                job.getLocation(), job.getExperienceRequired() , job.getPostDate() , user.getId());

    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteJob(long id){
        Job job = jobRepo.findById(id).orElseThrow(()->new NotFound("Job Doesn't Exist.."));
        jobRepo.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<JobResponseDto> getPostedJobs(String email){
        User user = userRepo.findByEmail(email).get();
        List<Job> jobs = jobRepo.findByUserId(user);
        List<JobResponseDto> newList = new ArrayList<>();
        for(Job job : jobs){
            newList.add(new JobResponseDto(job.getId(), job.getTitle(), job.getCompanyName(), job.getDescription(), job.getDepartment(),
                    job.getLocation() , job.getExperienceRequired(), job.getPostDate() , user.getId()));
        }
        return newList;
    }

    public List<JobResponseDto> searchJob(String title){
        List<Job> jobs =  jobRepo.findByTitle(title).get();

        if(jobs.isEmpty()){
            throw new NotFound("No Job found..!");
        }

        List<JobResponseDto> list = new ArrayList<>();

        for(Job job : jobs){
            list.add(new JobResponseDto(job.getId(), job.getTitle(), job.getCompanyName(), job.getDescription(), job.getDepartment(),
                    job.getLocation(), job.getExperienceRequired() , job.getPostDate() , job.getId()));
        }
        return list;
    }

    public List<JobResponseDto> getAllJobList(){
        List<JobResponseDto> list = new ArrayList<>();

        List<Job> jobList = jobRepo.findAll();

        if(jobList.isEmpty()) {
            throw new NotFound("No Job is scheduled");
        }

        for(Job job : jobList){
            list.add(new JobResponseDto(job.getId(), job.getTitle(), job.getCompanyName(), job.getDescription(), job.getDepartment(), job.getLocation(),
                    job.getExperienceRequired() , job.getPostDate() , job.getId()));
        }
        return list;
    }

}
