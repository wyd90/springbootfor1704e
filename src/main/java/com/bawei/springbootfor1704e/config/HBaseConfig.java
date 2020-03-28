package com.bawei.springbootfor1704e.config;

import org.apache.hadoop.hbase.HConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HBaseConfig {

    @Value("${spring.hbase.zookeeperQuorum}")
    private String zookeeperQuorum;

    @Bean
    public HbaseTemplate hbaseTemplate() {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set(HConstants.ZOOKEEPER_QUORUM,zookeeperQuorum);
        return new HbaseTemplate(conf);
    }
}
