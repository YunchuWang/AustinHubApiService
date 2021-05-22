package com.austinhub.apiservice.service;

import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.repository.AdsRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class AdsService {
    private AdsRepository adsRepository;

    public List<Ads> findAllAds() {
        return adsRepository.findAll(Sort.by(Direction.ASC, "created_timestamp"));
    }

    public void saveAds(List<Ads> ads) {
        adsRepository.saveAll(ads);
    }
}
