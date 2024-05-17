package com.e_commerce.service;

import static com.e_commerce.exception.ErrorCode.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import com.e_commerce.dto.ItemSearchDto;
import com.e_commerce.entity.Item;
import com.e_commerce.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemSearchService {

	private final RestHighLevelClient restHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<Item> searchItems(ItemSearchDto itemSearchDto) {
		SearchRequest searchRequest = new SearchRequest("item");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		// 판매 상태 필터
		if (itemSearchDto.getItemSellStatus() != null) {
			queryBuilder.must(QueryBuilders.matchQuery("sellStatus", itemSearchDto.getItemSellStatus().name()));
		}

		// 검색어 및 검색 유형 필터
		if (itemSearchDto.getSearchQuery() != null && !itemSearchDto.getSearchQuery().isEmpty()) {
			queryBuilder.must(QueryBuilders.matchQuery(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()));
		}

		// 날짜 범위 필터
		if (itemSearchDto.getSearchDateType() != null && !itemSearchDto.getSearchDateType().isEmpty()) {
			queryBuilder.filter(getDateRangeQuery("registeredDate", itemSearchDto.getSearchDateType()));
		}

		searchSourceBuilder.query(queryBuilder);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = null;
		try {
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new CustomException(SEARCH_PHRASE_ANALYSIS_FAILED);
		}

		return getSearchResult(searchResponse);
	}

	private BoolQueryBuilder getDateRangeQuery(String dateField, String searchDateType) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = switch (searchDateType) {
			case "1d" -> now.minusDays(1);// 최근 하루 전
			case "1w" -> now.minusWeeks(1); // 최근 일주일 전
			case "1m" -> now.minusMonths(1); // 최근 한달 전
			case "6m" -> now.minusMonths(6);  // 최근 6개월 전
			default -> now.minusYears(100); // all 선택시 과거부터 현재까지
		};

		return QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery(dateField)
			.gte(startDate.format(DateTimeFormatter.ISO_DATE))
			.lte(now.format(DateTimeFormatter.ISO_DATE)));
	}

	private List<Item> getSearchResult(SearchResponse response) {
		List<Item> results = new ArrayList<>();
		for (SearchHit hit : response.getHits()) {
			try {
				Item item = objectMapper.readValue(hit.getSourceAsString(), Item.class);
				results.add(item);
			} catch (IOException e) {
				throw new CustomException(SEARCH_PHRASE_ANALYSIS_FAILED);
			}
		}
		return results;
	}

}
