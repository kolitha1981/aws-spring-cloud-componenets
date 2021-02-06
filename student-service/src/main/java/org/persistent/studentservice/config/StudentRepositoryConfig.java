package org.persistent.studentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "org.persistent.studentservice.repository")
public class StudentRepositoryConfig extends AbstractMongoClientConfiguration {

	@Value("${org.persistent.studentservice.database.name}")
	private String databaseName;
	@Value("${org.persistent.studentservice.database.server}")
	private String server;
	@Value("${org.persistent.studentservice.database.server.port}")
	private String serverPort;

	@Override
	protected String getDatabaseName() {
		return this.databaseName;
	}

	@Override
	public MongoClient mongoClient() {
		final StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("mongodb://");
		urlBuilder.append(server).append(":");
		urlBuilder.append(serverPort).append("/").append(databaseName);
		return MongoClients.create(MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(urlBuilder.toString())).build());
	}
}
