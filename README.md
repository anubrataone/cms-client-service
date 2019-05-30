# cms-client-service



## Synopsis

MicroService to serve Content Retrieval from Elastic Search


### Standard Request Header

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| X-Auth-Token    | STRING | This is base64 encoded username:password format. This is used to authenticate the user invoking API (For now: once we have Auth Integrated this should be the OVAT token)	|
| Target-Source    | STRING | This could be either CMS or Discovery	|
| Target-Tenant    | STRING | This could be like FOX / ROGERS / VSM	|


### Standard HTTP Response Codes

| HTTP Code | Description |
| :---:   |   :---:   |
| 200 OK    | Successful response. |
| 401 Unauthorized    | Whenever a API requires a invalid user token/OVAT OR if it is not provided in the request or user is not authorized for requested tenant |
| 404 Not Found    | If the requested ID or Resource not found in backend |




### Standard Response Body & Fields

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| header    | Object | Standard header object.	|
| header -> code    | Integer | Response Code: 0 –> Success,  -1 –> Failure	|
| header -> message    | String | Response Message. Optional for failure response. For success response this should have the success message displayed to the end user.	|
| header -> system_time    | Timestamp | Unix Epoch Time in Milliseconds	|
| Errors    | Object | Standard errors object	|
| errors -> code| String | Error Code	|
| errors -> description| String | Error Description	|
| Data| Object | Container for the response object.	|

### Standard Success Response for single value response - GET CALL

```
{
    "header": {
        "source": "<Source Micro-service Name>",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
    },
    "data": {
        <RESPONSEOBJECT>
    }
}
```
### Standard Success Response for multi-value response - GET CALL

```
{
    "header": {
        "source": "Micro-service Name",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
        "start": 0,
        "rows": 10,
        "count": 100
    },
    "data": [
        <RESPONSEOBJECTLIST>
    ]
}
```


 ### Standard Failure Response (Plain acknowledgement)

 ```
 {
     "header": {
         "source": "<Source Micro-service Name>",
         "code": "-1",
         "message": "Failure Message",
         "system_time": 1558041284123
         "errors": [
             {
                 "code": "Error Code 1",
                 "description": "Error Description 1"
             },
             {
                 "code": "Error Code 2",
                 "description": "Error Description"
             }
         ]
     },
     --  this data part is optional
     "data": [
         <RESPONSEOBJECTLIST>
     ]
 }
 ```

## API Listing



##### GET CALLS

/client/getResourceById/<index>/{id}

```
- Method: GET
- Request line:
    - Path params
      - id: UUID of the resource

Corresponding Elastic Search API:
http://165.227.240.203:9200/cms/_search?q=entityType:MOVIE
http://165.227.240.203:9200/cms/_search?q=entityType:Episode
```

/client/getResourceByType/<index>/{type}

```
- Method: GET
- Request line:
    - Path params
      - type: type of the resource
    - Query params
        - pageSize: Integer field  (optional) -- Default by 10
        - pageNumber: Integer field  (optional)
```

/client/getResourceByIds

```
- Method: GET
- Request line:

    - Query params
        - ids: String (Command separated string of UUIDs)
        - type: String (optional Resource Type)
        - pageSize: Integer field  (optional) -- Default by 10
        - pageNumber: Integer field  (optional)
```

```
Corresponding Elastic Search API:
POST CALL:

{
    "query": {
        "constant_score" : {
           "filter" : {
                "term" : { "urn" : "122434cxvdfgdfghhg"}
            },
            "boost" : 1.2
        }
    }
}

{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "entityType" : "movie" }
      },
      "filter": {
        "term" : { "urn" : "122434cxvdfgdfghhg" }
      }
    }
  }
}


```
/client/search/<index>/

```
- Method: GET
- Request line:

    - Query params
        - match: String (ALL / ANY) - Default value ALL
        - key /value pair of search field.
        - pageSize: Integer field  (optional) -- Default by 10
        - pageNumber: Integer field  (optional)

Example:

/client/search?genres=comedy
/client/search?category=documentary   
/client/search?match=any&genres=comedy&rating=A    

Corresponding ES Query:

{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "genres" : "comedy" }
      }
    }
  }
}


{
  "query": {
    "bool" : {
      "should" : [
        { "term" : { "genres" : "comedy" } },
        { "term" : { "rating " : "A" } }
      ],
      "minimum_should_match" : 1
    }
  }
}


```

/client/searchByKeyword/<index>/?query=<Search Text>

```
- Method: GET
- Request line:

    - Query params
        
        - query: String <search string>
        - pageSize: Integer field  (optional) -- Default by 10
        - pageNumber: Integer field  (optional)

```
