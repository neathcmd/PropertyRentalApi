package com.rental.PropertyRentalApi.Service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.UploadsImages;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Entity.UsersProfile;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UploadsImagesRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Repository.UsersProfileRepository;
import com.rental.PropertyRentalApi.Service.CloudinaryService;
import com.rental.PropertyRentalApi.Service.UploadService;

import lombok.RequiredArgsConstructor;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UploadsImagesServiceImpl implements UploadService {

    private final UploadsImagesRepository uploadsImagesRepository;
    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final UsersProfileRepository usersProfileRepository;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Override
    public List<String> uploadPropertyImages(Users user, Long propertyId, List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw badRequest("No files provided.");
        }

        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found."));

        // Only the agent who created the property can upload images
//        if (!property.getCreatedBy().getId().equals(userId)) {
//            throw forbidden("You are not allowed to modify this property.");
//        }
        CheckAgentRole(user);

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.getSize() > MAX_FILE_SIZE) {
                throw badRequest("One of the files exceeds 5MB limit.");
            }
            try {
                String contentType = file.getContentType();
                if (contentType == null ||
                        (!contentType.equals("image/jpeg") &&
                                !contentType.equals("image/png") &&
                                !contentType.equals("image/jpg"))) {
                    throw badRequest("Only jpg, jpeg, png images are allowed.");
                }

                Map<?, ?> uploadResult = cloudinaryService.upload(file, "folder");
                String imageUrl = uploadResult.get("secure_url").toString();
                String publicId = uploadResult.get("public_id").toString();

                UploadsImages image = new UploadsImages();
                image.setUrls(imageUrl);
                image.setPublicId(publicId);
                image.setProperty(property);

                uploadsImagesRepository.save(image);
                imageUrls.add(imageUrl);

            } catch (IOException e) {
                throw internal("Failed to upload image", e);
            }
        }

        return imageUrls;
    }

    @Override
    public void deletePropertyImage(Users user, Long imageId, Long propertyId) {

        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found."));

        // Only the agent who created the property can delete its images
//        if (!property.getCreatedBy().equals(userId)) {
//            throw forbidden("You are not allowed to modify this property.");
//        }
        CheckAgentRole(user);

        UploadsImages image = uploadsImagesRepository.findById(imageId)
                .orElseThrow(() -> notFound("Image not found."));

        try {
            cloudinaryService.delete(image.getPublicId());
            uploadsImagesRepository.delete(image);
        } catch (IOException e) {
            throw internal("Failed to delete image", e);
        }
    }

    @Override
    public String uploadUserProfile(Long userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw badRequest("No files provided.");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User not found."));

        try {

            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/jpeg") &&
                            !contentType.equals("image/png") &&
                            !contentType.equals("image/jpg"))) {
                throw badRequest("Only jpg, jpeg, png images are allowed.");
            }

            Map<?, ?> uploadResult = cloudinaryService.upload(file, "folder");

            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            if (user.getProfile() != null && user.getProfile().getPublicId() != null) {
                cloudinaryService.delete(user.getProfile().getPublicId());
            }

            UsersProfile userProfile = new UsersProfile();
            userProfile.setUrls(imageUrl);
            userProfile.setPublicId(publicId);
            userProfile.setUser(user);

            usersProfileRepository.save(userProfile);

            return imageUrl;

        } catch (IOException e) {
            throw internal("Failed to upload profile image!", e);
        }
    }

    @Override
    public void deleteProfileImage(Long profileId, Long userId) {

        // Verify the user exists
        userRepository.findById(userId)
                .orElseThrow(() -> notFound("User not found."));

        UsersProfile profile = usersProfileRepository.findById(profileId)
                .orElseThrow(() -> notFound("No profile found."));

        // Only the owner of the profile can delete it
        if (!profile.getUser().getId().equals(userId)) {
            throw forbidden("You are not allowed to delete this profile image.");
        }

        try {
            cloudinaryService.delete(profile.getPublicId());
            usersProfileRepository.delete(profile);
        } catch (IOException e) {
            throw internal("Failed to delete image", e);
        }
    }

    private void CheckAgentRole(Users user) {
        boolean isAgent = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("agent"));

        if (!isAgent) {
            throw forbidden("You are not allowed to do this action.");
        }
    }
}