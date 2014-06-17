#!/bin/bash
curl -X POST http://127.0.0.1:8080/control -d '{"type":"start"}'
curl -X POST http://127.0.0.1:8080/control -d '{"type":"config","values":[{"param":"pop.subpop.0.size","value":"2"},{"param":"generations","value":"3"}]}'
for i in {1..10}
do
  for j in {1..3}
  do
    curl -X POST http://127.0.0.1:8080/control -d '{"type":"sensors","values":[{"value":"0.1"},{"value":"0.2"}]}'
    sleep 1
  done
  curl -X POST http://127.0.0.1:8080/control -d '{"type":"fitness","values":[{"value":"0.5"}]}'
  sleep 2
done