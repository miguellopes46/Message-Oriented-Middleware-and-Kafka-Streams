package kafka;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stream {
    public static void main(String[] args) throws InterruptedException, IOException {
        String outtopicname = "cliente";
        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "exercises-application-a");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,10000);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> creditsStream = builder.stream("Credits");
        KStream<String, String> paymentsStream = builder.stream("Payments");

        //##############################################################################################################
        //##########################################< CREDITS >#########################################################
        //##############################################################################################################

        KTable<String, Double> creditTable = creditsStream
                .mapValues(v -> getValue(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce((oldval, newval) -> oldval + newval);

        creditTable.toStream()
                   .mapValues((k, v) -> save(k, String.valueOf(v), "credit"))
                   .to(outtopicname, Produced.with(Serdes.String(), Serdes.String()));

        //##############################################################################################################
        //##########################################< PAYMENTS >########################################################
        //##############################################################################################################

        KTable<String, Double> paymentTable = paymentsStream
                .mapValues(v -> getValue(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce((oldval, newval) -> oldval + newval) ;

        paymentTable.toStream()
                    .mapValues((k, v) -> save(k, String.valueOf(v), "payment"))
                    .to(outtopicname, Produced.with(Serdes.String(), Serdes.String()));

        //##############################################################################################################
        //#########################################< TOTAL PAYMENTS >###################################################
        //##############################################################################################################

        KTable<String, Double> totalPayments = paymentsStream
                .mapValues(v -> getValue(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce((oldval, newval) -> oldval + newval, Materialized.as("TotalPayments"));


        //##############################################################################################################
        //###########################################< TOTAL CREDITS >##################################################
        //##############################################################################################################

        KTable<String, Double> totalCredits = creditsStream
                .mapValues(v -> getValue(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce((oldval, newval) -> oldval + newval, Materialized.as("TotalCredits"));

        //##############################################################################################################
        //#########################################< BALANCE >##########################################################
        //##############################################################################################################

        KTable<String, Double> balance = creditTable.join(paymentTable, (credits, payments) -> payments - credits);
        balance.toStream()
                .mapValues((k, v) -> save(k, String.valueOf(v), "balance"))
                .to(outtopicname, Produced.with(Serdes.String(), Serdes.String()));


        //-----------------------------------------------< TUMBLING WINDOW - 1month Bill >---------------------------------------------

        KTable<Windowed<String>, Double> billT = creditsStream
                .mapValues(v -> getValue(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(2)))
                .reduce((oldval, newval) -> oldval + newval, Materialized.as("MonthBill"));

        billT.toStream((wk, v) -> wk.key()).map((k, v) -> new KeyValue<>(k, "Client ID: " + k + " Credito no ultimo mes: " + v))
                .to("month", Produced.with(Serdes.String(), Serdes.String()));


        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        System.out.println("Reading stream from topic Payments and Credits !");
        TimeUnit.SECONDS.sleep(20);
        saveTotal("TotalPayments", "TotalCredits", streams);

    }

    public static Double getValue(String value) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Double result = Double.parseDouble(json.get("value").toString()) * Double.parseDouble(json.get("currency").toString());
        System.out.println("Valor Recebido: " + json.get("value").toString());
        System.out.println("Taxa de Cambio: " + json.get("currency").toString());
        System.out.println("Resultado: " + result);
        System.out.println("-------------------------------------------------");
        return result;
    }

    public static void saveTotal(String TotalTablePayments, String TotalTableCredits, KafkaStreams streams) throws InterruptedException {

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

        String id="";
        double totalPayment,totalCredit;
        long MAXVALUE = Long.MAX_VALUE;
        while (true) {
            totalPayment = 0.0;
            totalCredit = 0.0;

            ReadOnlyKeyValueStore<String, Double> keyValueStore = streams.store(TotalTablePayments, QueryableStoreTypes.keyValueStore());
            KeyValueIterator<String, Double> range = keyValueStore.range("1", String.valueOf(MAXVALUE));

            ReadOnlyKeyValueStore<String, Double> keyValueStoreC = streams.store(TotalTableCredits, QueryableStoreTypes.keyValueStore());
            KeyValueIterator<String, Double> rangeCredit = keyValueStoreC.range("1", String.valueOf(Long.MAX_VALUE));

            while (range.hasNext()) {
                KeyValue<String, Double> next = range.next();
                System.out.println("count for " + next.key + ": " + next.value);
                totalPayment+= next.value;
            }
            while (rangeCredit.hasNext()) {
                KeyValue<String, Double> next = rangeCredit.next();
                System.out.println("count for " + next.key + ": " + next.value);
                totalCredit += next.value;
            }

            double totalBalance = totalPayment - totalCredit;
            range.close();
            rangeCredit.close();
            System.out.println("Total of-> Payments: "+ totalPayment + ", Credits: " + totalCredit +"," +"Balance: " + totalBalance);
            producer.send(new ProducerRecord<String, String>("resultados", "1", saveResults(totalBalance, totalCredit, totalPayment)));
            Thread.sleep(30000);
        }
    }

    public static String save(String id, String value, String collum){
        DecimalFormat df = new DecimalFormat("0.00");
        return "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":false,\"field\":\""+ collum + "\"}],\"optional\":false},\"payload\":{\"id\":" + id + ",\"" + collum + "\":" + df.format(Double.parseDouble(value)) + "}}";
    }

    public static String saveResults(double totalbalance, double totalcredits, double totalpayments){
        String messagetoSend = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":false,\"field\":\"id\"},{\"type\":\"double\",\"optional\":false,\"field\":\"totalbalance\"},{\"type\":\"double\",\"optional\":false,\"field\":\"totalcredits\"},{\"type\":\"double\",\"optional\":false,\"field\":\"totalpayments\"}],\"optional\":false},\"payload\":{\"id\":1,\"totalbalance\":" + totalbalance + ",\"totalcredits\":" + totalcredits + ",\"totalpayments\":" + totalpayments + "}}";

        return messagetoSend;

    }
}

