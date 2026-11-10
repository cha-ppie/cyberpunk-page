package com.syberpunk.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

class ContentSeederTest {
    @TestConfiguration(proxyBeanMethods = false)
    @EnableTransactionManagement(proxyTargetClass = true)
    static class Transactions {}

    @Test void failedImportRollsBackAndCanBeRetried() throws Exception {
        var mapper = spy(new ObjectMapper());
        var failOnce = new AtomicBoolean(true);
        doAnswer(invocation -> {
            if (failOnce.getAndSet(false)) throw new IOException("Unable to read seed data");
            return invocation.callRealMethod();
        }).when(mapper).readTree(any(InputStream.class));

        var database = new EmbeddedDatabaseBuilder()
            .generateUniqueName(true).setType(EmbeddedDatabaseType.H2).build();
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(Transactions.class, DatabaseConfiguration.class, ContentSeeder.class);
            context.registerBean(DataSource.class, () -> database);
            context.registerBean(JdbcTemplate.class, () -> new JdbcTemplate(database));
            context.registerBean(JdbcTransactionManager.class, () -> new JdbcTransactionManager(database));
            context.registerBean(ObjectMapper.class, () -> mapper);
            context.refresh();

            var seeder = context.getBean(ContentSeeder.class);
            var jdbc = context.getBean(JdbcTemplate.class);
            var args = new DefaultApplicationArguments();
            assertThatThrownBy(() -> seeder.run(args)).isInstanceOf(IOException.class);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM categories", Integer.class)).isZero();

            seeder.run(args);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM categories", Integer.class)).isEqualTo(2);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM articles", Integer.class)).isPositive();
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM gallery_items", Integer.class)).isPositive();
        } finally {
            database.shutdown();
        }
    }
}
