package org.chanme.be.service;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.GallaryDTO;
import org.chanme.be.domain.gallary.Bin;
import org.chanme.be.domain.gallary.BinRepository;
import org.chanme.be.domain.gallary.Gallary;
import org.chanme.be.domain.gallary.GallaryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GallaryService {

    private final GallaryRepository gallaryRepository;
    private final BinRepository binRepository;

    public GallaryDTO EntityToDTO(Gallary gallary) {
        GallaryDTO gallaryDTO = new GallaryDTO().builder()
                .date(new Date())
                .image(gallary.getImageLink())
                .build();
        return gallaryDTO;
    }

    public List<GallaryDTO> getGallaries() {
        List<Gallary> gallaries = gallaryRepository.findAll();
        List<GallaryDTO> gallariesDTO = new ArrayList<>();
        for (Gallary gallary : gallaries) {
            gallariesDTO.add(EntityToDTO(gallary));
        }
        return gallariesDTO;
    }

    public List<Bin> getBins() {
        return binRepository.findAll();
    }

    public void deletePhoto(Long photoId) {
        gallaryRepository.deleteById(photoId);
    }

    public GallaryDTO recoverPhoto(Long photoId) {
        Bin bin = binRepository.findById(photoId).orElse(null);
        if(bin != null) {
            Gallary recover = new Gallary().builder()
                    .imageLink(bin.getImageLink())
                    .build();
            GallaryDTO recovered = EntityToDTO(gallaryRepository.save(recover));
            return recovered;
        } else {
            throw new NullPointerException("복원하려는 사진이 없습니다");
        }
    }
}
