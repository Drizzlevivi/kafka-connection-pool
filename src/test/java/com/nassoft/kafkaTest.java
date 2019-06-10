package com.nassoft;

import com.drizzle.kafka.connection.KafkaConnectionPool;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class kafkaTest {

    private static String configPath="F:\\workspace\\kafka-importer\\src\\main\\resources\\";
    public static void main(String[] args) throws IOException {
        Properties properties=new Properties();
        properties.load(new FileInputStream(new File(configPath+"config.properties")));
       /* KafkaProducer<String, String> producer=new KafkaProducer<String, String>(properties);
        System.out.println("连接成功");
        producer.close();*/
        KafkaConnectionPool<String,String> pool = new KafkaConnectionPool(properties);
        KafkaProducer producer = pool.getConnection();
        System.out.println("连接成功");
        pool.invalidateConnection(producer);
    }
}
