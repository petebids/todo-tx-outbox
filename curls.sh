export TOKEN=$(curl --request POST 'http://localhost:4080/auth/realms/mydomain/protocol/openid-connect/token' --header 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'client_id=demoapp' --data-urlencode 'username=testuser' --data-urlencode 'password=password123' --data-urlencode 'grant_type=password' --data-urlencode 'client_secret=kJFzzLYpzQg2frk57o5qyzIHSE3eliFC' | jq -r .access_token)

curl -H "Authorization: Bearer $TOKEN " http://localhost:9090/todos -X POST -d '{"details":"get bread"}' -H "Content-type: application/json" -i


curl -s \
  -X POST \
  "http://localhost:8081/subjects/todos/versions" \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{"schema": "{\"type\":\"record\",\"name\":\"sensor_sample\",\"fields\":[{\"name\":\"timestamp\",\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},{\"name\":\"identifier\",\"type\":\"string\",\"logicalType\":\"uuid\"},{\"name\":\"value\",\"type\":\"long\"}]}"}' \
  | jq