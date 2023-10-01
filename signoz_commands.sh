 ## run on k8s
helm install -n platform  --create-namespace signoz  signoz/signoz


# expose clickhouse
kubectl --namespace platform port-forward chi-signoz-clickhouse-cluster-0-0-0 8123

# generate load

# from /Users/pete/todo-k6/k6-test

k6 run  -vu 30 -d  30s script.js


# expose ui
export POD_NAME=$(kubectl get pods --namespace platform -l "app.kubernetes.io/name=signoz,app.kubernetes.io/instance=signoz,app.kubernetes.io/component=frontend" -o jsonpath="{.items[0].metadata.name}")

# expose otel collector
export POD_NAME=$(kubectl get pods --namespace platform -l "app.kubernetes.io/name=signoz,app.kubernetes.io/instance=signoz,app.kubernetes.io/component=otel-collector" -o jsonpath="{.items[0].metadata.name}")





## run backedn app with otel instrumentation

java -javaagent:otel/opentelemetry-javaagent.jar \                                                                                                                             ─╯
 -Dotel.exporter.otlp.endpoint=http://localhost:4317 \
 -Dotel.resource.attributes=service.name=todo \
 -jar target/todo-tx-outbox-0.0.1-SNAPSHOT.jar
