/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.quartz;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.jdbc.init.PlatformPlaceholderDatabaseDriverResolver;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;

/**
 * {@link DataSourceScriptDatabaseInitializer} for the Quartz Scheduler database. May be
 * registered as a bean to override auto-configuration.
 *
 * @author Vedran Pavic
 * @author Andy Wilkinson
 * @author Phillip Webb
 * @since 2.6.0
 */
public class QuartzDataSourceScriptDatabaseInitializer extends DataSourceScriptDatabaseInitializer {

	/**
	 * Create a new {@link QuartzDataSourceScriptDatabaseInitializer} instance.
	 * @param dataSource the Quartz Scheduler data source
	 * @param properties the Quartz properties
	 * @see #getSettings
	 */
	public QuartzDataSourceScriptDatabaseInitializer(DataSource dataSource, QuartzProperties properties) {
		this(dataSource, getSettings(dataSource, properties));
	}

	/**
	 * Create a new {@link QuartzDataSourceScriptDatabaseInitializer} instance.
	 * @param dataSource the Quartz Scheduler data source
	 * @param settings the database initialization settings
	 * @see #getSettings
	 */
	public QuartzDataSourceScriptDatabaseInitializer(DataSource dataSource, DatabaseInitializationSettings settings) {
		super(dataSource, settings);
	}

	/**
	 * Adapts {@link QuartzProperties Quartz properties} to
	 * {@link DatabaseInitializationSettings} replacing any {@literal @@platform@@}
	 * placeholders.
	 * @param dataSource the Quartz Scheduler data source
	 * @param properties the Quartz properties
	 * @return a new {@link DatabaseInitializationSettings} instance
	 * @see #QuartzDataSourceScriptDatabaseInitializer(DataSource,
	 * DatabaseInitializationSettings)
	 */
	public static DatabaseInitializationSettings getSettings(DataSource dataSource, QuartzProperties properties) {
		DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
		PlatformPlaceholderDatabaseDriverResolver platformResolver = new PlatformPlaceholderDatabaseDriverResolver();
		platformResolver = platformResolver.withDriverPlatform(DatabaseDriver.DB2, "db2_v95");
		platformResolver = platformResolver.withDriverPlatform(DatabaseDriver.MYSQL, "mysql_innodb");
		platformResolver = platformResolver.withDriverPlatform(DatabaseDriver.MYSQL, "mysql_innodb");
		platformResolver = platformResolver.withDriverPlatform(DatabaseDriver.POSTGRESQL, "postgres");
		platformResolver = platformResolver.withDriverPlatform(DatabaseDriver.SQLSERVER, "sqlServer");
		settings.setSchemaLocations(platformResolver.resolveAll(dataSource, properties.getJdbc().getSchema()));
		settings.setMode(properties.getJdbc().getInitializeSchema());
		settings.setContinueOnError(true);
		return settings;
	}

}
