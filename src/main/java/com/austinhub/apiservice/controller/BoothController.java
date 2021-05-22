package com.austinhub.apiservice.controller;

import com.austinhub.apiservice.model.dto.BoothRequest;
import com.austinhub.apiservice.model.po.Booth;
import com.austinhub.apiservice.service.BoothService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping("/booths")
public class BoothController {

  private BoothService boothService;

  public BoothController(BoothService boothService) {
    this.boothService = boothService;
  }

  @GetMapping
  public ResponseEntity<List<Booth>> findBoothsByCategory(
      @Valid @NotNull @RequestParam Integer categoryId) {
    return ResponseEntity.ok().body(boothService.findByCategory(categoryId));
  }

  @PostMapping
  public ResponseEntity<Booth> saveBooth(@Valid @RequestBody BoothRequest boothRequest) {
    return ResponseEntity.ok(boothService.saveBooth(boothRequest));
  }
}
