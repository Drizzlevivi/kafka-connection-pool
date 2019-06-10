package com.drizzle.kafka.connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * 连接实例创建
 */
public class KafkaConnectionFactory<K,V> extends BasePooledObjectFactory<KafkaProducer<K, V>> {

    private final Properties properties;

    public KafkaConnectionFactory(Properties properties) {
        this.properties = properties;
    }

   @Override
    public KafkaProducer<K, V> create() throws Exception {
        KafkaProducer<K, V> producer = new KafkaProducer<K, V>(properties);
        return producer;
    }

     @Override
    public PooledObject<KafkaProducer<K, V>> wrap(KafkaProducer<K, V> kafkaProducer) {
        return new DefaultPooledObject(kafkaProducer);
    }

   @Override
    public void destroyObject(PooledObject<KafkaProducer<K, V>> p) throws Exception {
        KafkaProducer<K, V> producer = p.getObject();
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<KafkaProducer<K, V>> p) {
        KafkaProducer<K, V> producer = p.getObject();
        return null != producer;
    }

}
