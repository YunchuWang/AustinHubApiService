package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.AdsRequest;
import com.austinhub.apiservice.model.po.Ads;
import com.austinhub.apiservice.service.AdsService;
import com.austinhub.apiservice.utils.GsonUtils;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/ads")
public class AdsController {
    private AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    public ResponseEntity<List<Ads>> findAllAds() {
        List<Ads> ads = adsService.findAllAds();
        return ResponseEntity.ok().body(adsService.findAllAds());
    }

    @PostMapping
    public ResponseEntity<String> saveAds(@Valid @RequestBody AdsRequest adsRequest) {
        adsService.saveAds(adsRequest.getAds());
        return ResponseEntity.ok(GsonUtils.getGson().toJson("saved"));
    }
}
