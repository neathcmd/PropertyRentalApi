package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {

    List<String> uploadPropertyImages(Users user, Long propertyId, List<MultipartFile> files);

    void deletePropertyImage(Users user, Long imageId, Long propertyId);

    String uploadUserProfile(Long userId, MultipartFile file);

    void deleteProfileImage(Long profileId, Long userId);
}