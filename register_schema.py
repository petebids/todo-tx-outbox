import requests
import json

schema = {
    "type": "record",
    "name": "TodoEvent",
    "namespace": "xyz.petebids.todotxoutbox",
    "fields": [
        {
            "name": "id",
            "doc": "System-assigned numeric user ID. Cannot be changed by the user.",
            "type": "string"
        },
        {
            "name": "details",
            "doc": "textual detail of the todo",
            "type": "string"
        },
        {
            "name": "complete",
            "doc": "self explanatory",
            "type": "boolean"
        },
        {
            "name": "event_type",
            "type": "string"
        }

    ]
}

res = requests.post(
    url='http://localhost:8081/subjects/TODO-value/versions',
    data=json.dumps({
        'schema': json.dumps(schema)
    }),
    headers={'Content-Type': 'application/vnd.schemaregistry.v1+json'}).json()

