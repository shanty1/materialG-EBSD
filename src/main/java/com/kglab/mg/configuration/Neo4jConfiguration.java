package com.kglab.mg.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableNeo4jRepositories(basePackages = "com.kglab.mg.repositor")
//@EnableTransactionManagement
//@EntityScan(basePackages = "com.kglab.mg.entity.po")
public class Neo4jConfiguration {

//    @Value("${spring.data.neo4j.uri}")
//    private String url;
//
//    @Value("${spring.data.neo4j.username}")
//    private String username;
//
//    @Value("${spring.data.neo4j.password}")
//    private String password;
//
//    @Bean(name = "session")
//    public Session neo4jSession() {
//        Driver driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
//        return driver.session();
//    }

}