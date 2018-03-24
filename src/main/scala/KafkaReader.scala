import java.util.Properties
import java.util
import scala.collection.JavaConversions._

import org.apache.kafka.clients.consumer.KafkaConsumer

object KafkaReader extends App {

  val props = new Properties()

  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "customers")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("session.timeout.ms", "30000")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Arrays.asList("customers", "test"))

  while (true) {
    val records = consumer.poll(100)

    for (record <- records) {
      println("offset = %d, key = %s, value = %s", record.offset, record.key, record.value)
    }
  }
}