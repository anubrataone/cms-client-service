package com.elasticsearch.cms.rest;


import com.elasticsearch.cms.rest.response.ResponseData;
import com.elasticsearch.cms.rest.response.ResponseHeader;
import com.elasticsearch.cms.service.model.SearchEngineResponseMsg;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
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
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("get")
@Api(value = "get")
public class GetServiceImpl {

    @Autowired
    private RestHighLevelClient transportClient;

    public GetServiceImpl() {
        // needed for autowiring
    }


    private boolean validateInputParams(MultivaluedMap<String, String> requestInputs) {
        if (requestInputs == null || requestInputs.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameter(s):");
        }

        //To Do - validate
        return true;
    }

    @RequestMapping(value = "/{idx}/getResourceByUrn/{urn}", method = RequestMethod.GET)
    public ResponseData<SearchHit[]> getResourceByUrn(@PathVariable("idx")  String idx,
                                                      @PathVariable("urn")  String urn) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;

        try {

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            QueryBuilder queryBuilder = QueryBuilders
                    .boolQuery()
                    .must(QueryBuilders.termQuery("urn", urn.toLowerCase()));

            sourceBuilder.query(queryBuilder);
            sourceBuilder.from(0);
            sourceBuilder.size(5);

            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(idx);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = transportClient
                    .search(searchRequest, RequestOptions.DEFAULT);

            ResponseData<SearchHit[]> responseData = createResponse("Success", searchResponse);
            return responseData;
        } catch (Exception e) {

            ResponseData<SearchHit[]> responseData = createResponse("Failed: "+ ExceptionUtils.getStackTrace(e), null);

            return responseData;
        }
    }

    @RequestMapping(value = "/{idx}/getResourceById/{id}", method = RequestMethod.GET)
    public ResponseData<SearchHit[]> getResourceById(@PathVariable("idx")  String idx,
                                                   @PathVariable("id")  String id) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;


        try {

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

//            QueryBuilder queryBuilder = QueryBuilders
//                    .boolQuery()
//                    .must(QueryBuilders.termQuery("id", id));
            QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(id);
            sourceBuilder.query(queryBuilder);
            sourceBuilder.from(0);
            sourceBuilder.size(5);

            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(idx);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = transportClient
                    .search(searchRequest, RequestOptions.DEFAULT);

            ResponseData<SearchHit[]> responseData = createResponse("Success", searchResponse);
            return responseData;

        } catch (Exception e) {

            ResponseData<SearchHit[]> responseData = createResponse("Failed: "+ ExceptionUtils.getStackTrace(e),
                    null);

            return responseData;
        }
    }

    private static ResponseData createResponse(String message, SearchResponse searchResponse){
        ResponseData<SearchHit[]> responseData = new ResponseData();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setSource("cms-client-service");
        responseHeader.setSystemTime(System.currentTimeMillis());

        responseHeader.setMessage(message);

        responseHeader.setStart(0);
        if(searchResponse != null) {
            responseHeader.setCode("0");
            responseHeader.setRows(searchResponse.getHits().totalHits);

            responseData.setHeader(responseHeader);
            responseData.setData(searchResponse.getHits().getHits());
            responseHeader.setCount(searchResponse.getHits().getHits().length);
        } else {
            responseHeader.setCode("-1");
        }

        return responseData;
    }


    @RequestMapping(value = "/{idx}/getResourceByType/{entityType}", method = RequestMethod.GET)
    public ResponseData<SearchHit[]> getResourceByType(@PathVariable("idx")  String idx,
                                                       @PathVariable("entityType")  String entityType) {

        //Step 1: Validate input request  via required parameters

        //Step 2: Validate input header
        //Step 3: Get priority
        List<SearchHit> records = null;

        try {

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            QueryBuilder queryBuilder = QueryBuilders
                    .boolQuery()
                    .must(QueryBuilders.termQuery("entityType", entityType.toLowerCase()));

            sourceBuilder.query(queryBuilder);
            sourceBuilder.from(0);
            sourceBuilder.size(5);

            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(idx);
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = transportClient
                    .search(searchRequest, RequestOptions.DEFAULT);

            ResponseData<SearchHit[]> responseData = createResponse("Success", searchResponse);
            return responseData;
        } catch (Exception e) {

            ResponseData<SearchHit[]> responseData = createResponse("Failed: "+ ExceptionUtils.getStackTrace(e), null);

            return responseData;
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<SearchEngineResponseMsg> handleUserNotFoundException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new SearchEngineResponseMsg(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}