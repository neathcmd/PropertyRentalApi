package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.UploadService;
import com.rental.PropertyRentalApi.Utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadsImagesController {

    private final UploadService uploadService;
    private final AuthUtil authUtil;

    @PostMapping("/uploads/{propertyId}")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long propertyId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        Users user = authUtil.getAuthenticatedUser();
        List<String> imageUrls = uploadService.uploadPropertyImages(user, propertyId, files);
        return ResponseEntity.ok(imageUrls);
    }

    @DeleteMapping("/uploads/property/{propertyId}/image/{imageId}")
    public ResponseEntity<?> deletePropertyImage(
            @PathVariable Long propertyId,
            @PathVariable Long imageId
    ) {
        Users user = authUtil.getAuthenticatedUser();
        uploadService.deletePropertyImage(user, imageId, propertyId);
        return ResponseEntity.ok("Image deleted successfully.");
    }

    @PostMapping("/users/profile")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("file") MultipartFile file
    ) {
        Users user = authUtil.getAuthenticatedUser();
        String imageUrl = uploadService.uploadUserProfile(user.getId(), file);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/users/profile/{profileId}")
    public ResponseEntity<?> deleteProfileImage(
            @PathVariable Long profileId
    ) {
        Users user = authUtil.getAuthenticatedUser();
        uploadService.deleteProfileImage(profileId, user.getId());
        return ResponseEntity.ok("Profile image deleted successfully.");
    }
}
