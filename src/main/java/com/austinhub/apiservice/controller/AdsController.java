package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.MyAdsDTO;
import com.austinhub.apiservice.model.dto.SaveAdsRequest;
import com.austinhub.apiservice.model.dto.UpdateAdsRequest;
import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.service.AdsService;
import com.austinhub.apiservice.utils.GsonUtils;
import io.micrometer.core.instrument.Counter;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CacheConfig(cacheNames = {"Ads"})
@RestController
@Validated
@RequestMapping("/compaigns")
public class AdsController {

    private final AdsService adsService;
    private final Counter visitorCounter;

    public AdsController(AdsService adsService, Counter visitorCounter) {
        this.adsService = adsService;
        this.visitorCounter = visitorCounter;
        visitorCounter.increment();
    }

    @GetMapping
    public ResponseEntity<List<Ads>> findAllAds() {
        visitorCounter.increment();
        return ResponseEntity.ok()
                             .body(adsService.findAllAds());
    }

    @GetMapping("/owned")
    public ResponseEntity<List<MyAdsDTO>> findOwnedAds(
            @Valid @NotNull @RequestParam String accountName,
            @Valid @NotNull @RequestParam Boolean isArchived) {
        return ResponseEntity.ok()
                             .body(adsService.findOwnsAds(accountName, isArchived));
    }

    @PutMapping
    public ResponseEntity<String> updateAds(@Valid @RequestBody UpdateAdsRequest updates) {
        adsService.updateAds(updates);
        return ResponseEntity.ok(GsonUtils.getGson()
                                          .toJson("updated"));
    }

    @PostMapping
    public ResponseEntity<String> saveAds(@Valid @RequestBody SaveAdsRequest saveAdsRequest) {
        adsService.saveAds(saveAdsRequest.getAds());
        return ResponseEntity.ok(GsonUtils.getGson()
                                          .toJson("saved"));
    }
}
