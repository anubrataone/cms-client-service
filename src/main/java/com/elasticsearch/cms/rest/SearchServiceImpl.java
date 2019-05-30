package com.elasticsearch.cms.rest;

import com.elasticsearch.cms.service.model.SearchEngineResponseMsg;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("search")
@Api(value = "search")
public class SearchServiceImpl {


    @Autowired
    private RestHighLevelClient transportClient;

    public SearchServiceImpl() {
        // needed for autowiring
    }


    private boolean validateInputParams(MultivaluedMap<String, String> requestInputs) {
        if (requestInputs == null || requestInputs.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameter(s):");
        }

        //To Do - validate
        return true;
    }

    @RequestMapping(value = "/{idx}", method = RequestMethod.GET)
    public SearchEngineResponseMsg search(@PathVariable("idx")  String idx, @RequestParam Map<String, String> requestParams) {

        //Step 1: Validate input request  via required parameters
        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;

        try {

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder queryBuilder = QueryBuilders
                    .boolQuery();

            boolean orOperator = true;
            for (Map.Entry<String, String> entrySet : requestParams.entrySet()) {
                if (entrySet.getKey().contains("match") && entrySet.getValue().equalsIgnoreCase("any")) {
                    orOperator = false;
                }
            }


            for (Map.Entry<String, String> entrySet : requestParams.entrySet()) {
                if (orOperator) {
                    queryBuilder.must(QueryBuilders.termQuery(entrySet.getKey(), entrySet.getValue().toLowerCase()));
                } else {
                    queryBuilder.should(QueryBuilders.termQuery(entrySet.getKey(), entrySet.getValue().toLowerCase()));
                }
            }

            sourceBuilder.query(queryBuilder);
            sourceBuilder.from(0);
            sourceBuilder.size(5);

            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(idx);
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = transportClient
                    .search(searchRequest, RequestOptions.DEFAULT);

            SearchEngineResponseMsg responseMsg = new SearchEngineResponseMsg("Success");
            responseMsg.setData(searchResponse.getHits().getHits());
            return responseMsg;

        } catch (Exception e) {
            SearchEngineResponseMsg responseMsg = new SearchEngineResponseMsg("Failed:" + ExceptionUtils.getStackTrace(e));
            responseMsg.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return responseMsg;
        }

    }


    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<SearchEngineResponseMsg> handleUserNotFoundException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new SearchEngineResponseMsg(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}