package com.example.productservice.controllers;

import com.example.productservice.dtos.SearchRequestDto;
import com.example.productservice.models.Product;
import com.example.productservice.services.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public List<Product> search(@RequestBody SearchRequestDto requestDto) {
        return searchService.search(requestDto.getKeyword(), requestDto.getPageNumber(), requestDto.getSizeOfPage());
    }
}
