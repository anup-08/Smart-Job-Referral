package com.smartJob.service;

import com.smartJob.dtos.ReferralRequestDto;
import com.smartJob.dtos.ReferralResponseDto;
import com.smartJob.entity.Document;
import com.smartJob.entity.Job;
import com.smartJob.entity.Referral;
import com.smartJob.entity.User;
import com.smartJob.enums.ReferralStatus;
import com.smartJob.exception.AlreadyRegisterException;
import com.smartJob.exception.NotFound;
import com.smartJob.repo.DocRepository;
import com.smartJob.repo.JobRepository;
import com.smartJob.repo.ReferralRepository;
import com.smartJob.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReferralService {
    private final ReferralRepository referralRepo ;
    private final JobRepository jobRepository;
    private final UserRepository userRepo ;
    private final DocRepository docRepo;
    private String uploadDir;
    private Path rootLocation;

    public ReferralService(ReferralRepository referralRepo, JobRepository jobRepository,
                           UserRepository userRepo, DocRepository docRepo,
                           @Value("${file.upload-dir}") String uploadDir) {
        this.referralRepo = referralRepo;
        this.jobRepository = jobRepository;
        this.userRepo = userRepo;
        this.docRepo = docRepo;
        this.uploadDir = uploadDir;
    }

    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(uploadDir);
        Files.createDirectories(rootLocation);
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ReferralResponseDto referral(ReferralRequestDto requestDto , MultipartFile file , String referredEmail) throws IOException {
        Job job = jobRepository.findById(requestDto.getJobId()).orElseThrow(()->new NotFound("Job doesn't Exist..!"));

        List<Referral> existReferral = referralRepo.findByCandidateEmail(requestDto.getCandidateEmail());

        if(!existReferral.isEmpty()){
            for(Referral ref : existReferral){
                if(ref.getJob().getId() == requestDto.getJobId()){
                    throw new AlreadyRegisterException("Candidate is Already Referred by "+ref.getReferredBy().getName());
                }
            }
        }

        User user = userRepo.findByEmail(referredEmail).get();

        Referral referral = new Referral();

        referral.setCandidateName(requestDto.getCandidateName());
        referral.setCandidateEmail(requestDto.getCandidateEmail());
        referral.setReferredAt(new Date());
        referral.setReferralStatus(ReferralStatus.REFERRED);
        referral.setJob(job);
        referral.setReferredBy(user);

        if (file != null && !file.isEmpty()) {
            // 1. Generate a unique filename to prevent conflicts
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFileName)).normalize().toAbsolutePath();

            // 2. Save the file to the disk
            Files.copy(file.getInputStream(), destinationFile);

            // 3. Create the Document entity and save the PATH, not the bytes
            Document doc = new Document();
            doc.setFileName(file.getOriginalFilename()); // The original name for display
            doc.setStoredPath(file.getOriginalFilename()); // The actual path on the disk
            doc.setReferral(referral);
            referral.setDocument(doc);
        }

        referralRepo.save(referral);

        return convertReferral(referral);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReferralResponseDto> getReferralDetail(long jobId){
        Job job = jobRepository.findById(jobId).orElseThrow(()->new NotFound("No job Found"));
        List<ReferralResponseDto> candidateList = new ArrayList<>();
        List<Referral> getData = referralRepo.findByJob(job);

        if(getData.isEmpty()) {
            throw new NotFound("No candidate found");
        }

        for(Referral ref : getData){
            candidateList.add(convertReferral(ref));
        }

        return  candidateList;
    }

    @PreAuthorize("hasRole('USER')")
    public List<ReferralResponseDto> getReferralData(String email){
        User user = userRepo.findByEmail(email).get();

        List<Referral> referral = referralRepo.findByReferredBy(user);

        if(referral.isEmpty()){
            throw new NotFound("No Candidate Found..!");
        }

        List<ReferralResponseDto> list = new ArrayList<>();

        for(Referral ref : referral){
            list.add(convertReferral(ref));
        }
        return list;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public Referral setReferralStatus(long id , String newStatus){
        Referral referral = referralRepo.findById(id)
                .orElseThrow(() -> new NotFound("Referral not found"));

        if(newStatus.equalsIgnoreCase("REFERRED")){
            referral.setReferralStatus(ReferralStatus.REFERRED);
        }
        else if(newStatus.equalsIgnoreCase("INTERVIEWED")){
            referral.setReferralStatus(ReferralStatus.INTERVIEWED);
        }
        else if(newStatus.equalsIgnoreCase("HIRED")){
            referral.setReferralStatus(ReferralStatus.HIRED);
        }
        else if(newStatus.equalsIgnoreCase("REJECTED")){
            referral.setReferralStatus(ReferralStatus.REJECTED);
        }
        return referralRepo.save(referral);

    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReferralResponseDto> getPostedJobReferral(String email){
        User user = userRepo.findByEmail(email).get();
        List<Job> jobs = jobRepository.findByUserId(user);
        List<ReferralResponseDto> refDto = new ArrayList<>();
        for(Job job : jobs){
            List<Referral> referral = referralRepo.findByJob(job);
            for(Referral ref : referral){
                refDto.add(convertReferral(ref));
            }
        }
        return refDto;
    }

    @Transactional
    public Resource getDocumentByReferralId(Long referralId) throws MalformedURLException {
        Referral referral = referralRepo.findById(referralId).orElseThrow(()->new NotFound("Referral with ID " + referralId + " not found."));

        Document doc = referral.getDocument();

        if(doc == null){
            throw new NotFound("No document found for referral ID " + referralId);
        }

        Path file = Paths.get(doc.getFileName());
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    @PreAuthorize("hasRole('user')and @referralRepo.findById(#referralId).get().getReferredBy().getEmail() == authentication.name")
    public String updateDoc(Long referralId,MultipartFile file) throws IOException {
        Referral ref = referralRepo.findById(referralId).orElseThrow(()->new NotFound("Referral with ID " + referralId + " not found."));
        if(file.isEmpty()) {
            throw new NotFound("file is Missing");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }
        Document doc = ref.getDocument();
        if (doc != null && doc.getFileName() != null) {
            Files.deleteIfExists(Paths.get(doc.getFileName()));
        } else {
            // If no document exists, create a new one
            doc = new Document();
            doc.setReferral(ref);
            ref.setDocument(doc);
        }

        // Save the new file
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFileName)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        // Update the entity with the new details
        doc.setFileName(file.getOriginalFilename());
        doc.setFileName(destinationFile.toString());
        docRepo.save(doc);

        return "Document updated successfully";
    }

    public ReferralResponseDto convertReferral(Referral referral){

        String fileName = (referral.getDocument() != null) ? referral.getDocument().getFileName() : null;

        return new ReferralResponseDto(referral.getId() , referral.getCandidateName() , referral.getCandidateEmail() , referral.getJob().getTitle()
                ,referral.getReferredBy().getName() , referral.getReferredBy().getEmail() , referral.getReferralStatus() , referral.getReferredAt()
        ,fileName);
    }

}
