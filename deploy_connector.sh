#! /bin/bash

name=$(cat $1 | jq -rc ".name")


curl -X PUT \
  -H "content-type: application/json" \
  -d "@$1" \
  "localhost:8083/connectors/$name/config" \
  | jq

