package kafka;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;


public class SimpleProducer {
    public static void main(String[] args) throws Exception{

        JSONParser parser = new JSONParser();
        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;

        Properties propsConsumer = new Properties();
        propsConsumer.put("bootstrap.servers", "localhost:9092");
        propsConsumer.put("acks", "all");
        propsConsumer.put("retries", 0);
        propsConsumer.put("batch.size", 16384);
        propsConsumer.put("linger.ms", 1);
        propsConsumer.put("buffer.memory", 33554432);
        propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "kafkaExampleConsumer");
        propsConsumer.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumer.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(propsConsumer);


        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        Random rand = new Random();
        ArrayList<String> coins = new ArrayList<String>();
        ArrayList<String> clientIDS = new ArrayList<String>();

        ConsumerRecords<String, String> clients;
        ConsumerRecords<String, String> currencies;

        while (true) {
            consumer.subscribe(Collections.singletonList("DBInfoTopic"));
            clients = consumer.poll(5000);

            consumer.subscribe(Collections.singletonList("DBInfoTopicCurrency"));
            currencies = consumer.poll(5000);

            for(ConsumerRecord<String, String> record : currencies){
                JSONObject json = (JSONObject) parser.parse(record.value());
                JSONObject a = (JSONObject) json.get("payload");
                if(!coins.contains(a.get("exchange_rate").toString())) {
                    coins.add(a.get("exchange_rate").toString());
                }
            }

            for (ConsumerRecord<String, String> record : clients) {
                JSONObject json = (JSONObject) parser.parse(record.value());
                JSONObject a = (JSONObject) json.get("payload");
                if(!clientIDS.contains(a.get("id").toString())) {
                    clientIDS.add(a.get("id").toString());
                }
            }
            //Escolhe Valor random de 0 a 100
            double value = rand.nextDouble() * (100);

            //Random se vai fazer pagamento ou crédito
            int option = rand.nextInt(1 + 1);

            //Escolhe uma moeda random
            int index = (int)(Math.random() * coins.size());
            if(coins.size() == 0){
                continue;
            }
            String currency = coins.get(index);

            //Escolhe um cliente random
            int indexClient = (int)(Math.random() * clientIDS.size());
            if(clientIDS.size() == 0){
                continue;
            }
            String id = clientIDS.get(indexClient);

            //Mensagem que vai enviar para o Stream
            String messagetoSend = String.format("{\"value\": %.2f, \"currency\": \"%s\"}", value, currency);

            //Manda mensagem
            if (option == 0) { //Credit
                producer.send(new ProducerRecord<String, String>("Credits", id, messagetoSend));
                System.out.println("Credit of " + df.format(value) + " from Client " + id + " !");
            }
            else { //Payment
                producer.send(new ProducerRecord<String, String>("Payments", id, messagetoSend));
                System.out.println("Payment of " + df.format(value) + " from Client " + id + " !");
            }
        }
    }
}
