package com.drizzle.kafka.connection;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.log4j.Logger;

import java.util.Properties;


public class KafkaConnectionPool<K,V> implements ConnectionPool<KafkaProducer<K, V>>{

    private static Logger log = Logger.getLogger(KafkaConnectionPool.class);

    private GenericObjectPool<KafkaProducer<K, V>> internalPool;

    public KafkaConnectionPool(Properties properties){
        this(new KafkaPoolConfig(),properties);
    }

    public KafkaConnectionPool(KafkaPoolConfig config,Properties properties){
        this(config,new KafkaConnectionFactory(properties));
    }

    public KafkaConnectionPool(final GenericObjectPoolConfig poolConfig,
                               PooledObjectFactory<KafkaProducer<K, V>> factory){
        if (this.internalPool != null)
            this.internalPool.close();
        this.internalPool = new GenericObjectPool<KafkaProducer<K, V>>(factory, poolConfig);
    }

    @Override
    public KafkaProducer<K, V> getConnection() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            log.error("获取连接失败："+e.getMessage());
        }
        return null;
    }


    @Override
    public void returnConnection(KafkaProducer<K, V> conn) {
        if(null!=conn){
            try{
                internalPool.returnObject(conn);
            }catch (Exception e){
                log.error("归还连接失败："+e.getMessage());
            }
        }

    }

    @Override
    public void invalidateConnection(KafkaProducer<K, V> conn) {
        if(null!=conn){
            try{
                internalPool.invalidateObject(conn);
            }catch (Exception e){
                log.error("废弃池对象失败："+e.getMessage());
            }
        }
    }
}
