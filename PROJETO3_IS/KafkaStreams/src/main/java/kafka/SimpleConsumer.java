package kafka;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SimpleConsumer {
    public static void main(String[] args) throws Exception{

        //Assign topicName to string variable
        String topicName = args[0].toString();
        JSONParser parser = new JSONParser();

        // create instance for properties to access producer configs
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String, Double> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
        while (true) {
            ConsumerRecords<String, Double> records = consumer.poll(5000);
            for (ConsumerRecord<String, Double> record : records){
                System.out.println(record.value());
               // JSONObject json = (JSONObject) parser.parse(String.valueOf(record.value()));
              //  System.out.println(record.value());
                /*
                JSONObject a = (JSONObject) json.get("payload");
                if(a.get("payment") != null){
                    System.out.println("Valor do Pagamento Atual do Client " + a.get("id").toString() + " : " + a.get("payment").toString());
                }
                else{
                    System.out.println("Valor do Crédito Atual do Client " + a.get("id").toString() + " : " + a.get("credit").toString());

                }

                 */
            }
        }
    //consumer.close();
    }
}