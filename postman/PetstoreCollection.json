{
  "info": {
    "name": "Petstore Veeva Collection",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    { "key": "url", "value": "https://petstore.swagger.io/v2" }
  ],
  "item": [
    {
      "name": "Assignment 1 - GET findByStatus available",
      "request": {
        "method": "GET",
        "url": "{{url}}/pet/findByStatus",
        "query": [{ "key": "status", "value": "available" }]
      },
      "event": [{
        "listen": "test",
        "script": { "exec": ["pm.test('Status is 200', function(){ pm.response.to.have.status(200); });"] }
      }]
    },
    {
      "name": "Assignment 2 - POST create pet",
      "request": {
        "method": "POST",
        "url": "{{url}}/pet",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "body": { "mode": "raw", "raw": "{\"id\": 99999, \"name\": \"VeevaDog\", \"status\": \"available\"}" }
      },
      "event": [{
        "listen": "test",
        "script": { "exec": [
          "pm.test('Status 200', function(){ pm.response.to.have.status(200); });",
          "pm.test('Name in body', function(){ pm.expect(pm.response.text()).to.include('VeevaDog'); });"
        ]}
      }]
    },
    {
      "name": "Assignment 3a - GET valid pet (200)",
      "request": { "method": "GET", "url": "{{url}}/pet/99999" },
      "event": [{ "listen": "test", "script": { "exec": ["pm.test('200 OK', function(){ pm.response.to.have.status(200); });"] } }]
    },
    {
      "name": "Assignment 3b - GET non-existent pet (404)",
      "request": { "method": "GET", "url": "{{url}}/pet/999999999999" },
      "event": [{ "listen": "test", "script": { "exec": ["pm.test('404 Not Found', function(){ pm.response.to.have.status(404); });"] } }]
    },
    {
      "name": "Assignment 3c - POST empty body (400/405)",
      "request": {
        "method": "POST",
        "url": "{{url}}/pet",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "body": { "mode": "raw", "raw": "" }
      },
      "event": [{ "listen": "test", "script": { "exec": [
        "pm.test('4xx error', function(){ pm.expect(pm.response.code).to.be.oneOf([400, 405]); });"
      ]} }]
    },
    {
      "name": "Assignment 4 - Using environment variable {{url}}",
      "request": { "method": "GET", "url": "{{url}}/pet/123" }
    }
  ]
}
