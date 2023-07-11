
- [官方文档](https://kafka.apache.org/documentation/#quickstart)

# 集群

## 消费者组

## 消费分区

## 消费者

## 生产者

## 偏移

```sh
# 创建topic
bin/kafka-topics.sh --create --topic test-events --bootstrap-server localhost:9092

#查看topics
bin/kafka-topics.sh --describe --topic haoshuai-events --bootstrap-server localhost:9092

#查看所有topics
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# 消费者组
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group console-consumer-62639 --offsets

bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group console-consumer-9205 --offsets


# 生产者
bin/kafka-console-producer.sh --topic haoshuai-events --bootstrap-server localhost:9092

# 消费者
bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092


docker run --net=host \
	-e KAFKA_CLUSTERS_0_NAME=local \
	-e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=localhost:9092 \
	-d provectuslabs/kafka-ui:latest 

```
