#!/bin/bash

curl -H "Content-Type: application/vnd.collection+json" -X POST -d '{"collection":{"version":"1.0","template":{"data":[{"name":"name","prompt":"Name","value":"TestSensor"},{"name":"location","prompt":"Location","value":"TestLocation"}]}}}' http://localhost:8080/sensors

