package com.syberpunk.content;

import java.nio.charset.StandardCharsets;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAccountService implements UserDetailsService, ApplicationRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    private final AdminProperties properties;

    public AdminAccountService(JdbcTemplate jdbc, PasswordEncoder encoder, AdminProperties properties) {
        this.jdbc = jdbc; this.encoder = encoder; this.properties = properties;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return jdbc.query("SELECT * FROM users WHERE username = ?", (row, index) -> User.withUsername(row.getString("username"))
            .password(row.getString("password_hash")).roles(row.getString("role"))
            .disabled(!row.getBoolean("enabled")).build(), username)
            .stream().findFirst().orElseThrow(() -> new UsernameNotFoundException("账号或密码错误"));
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String username = properties.username();
        String password = properties.password();
        if (username.isEmpty() && password.isEmpty()) return;
        if (!username.matches("[a-zA-Z0-9_-]{3,64}") || password.length() < 12 || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalStateException("ADMIN_USERNAME 需为 3–64 位字母、数字、下划线或短横线，ADMIN_PASSWORD 至少 12 个字符且不超过 72 字节");
        }
        if (jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE username = ?", Integer.class, username) == 0) {
            jdbc.update("INSERT INTO users (username,password_hash,role) VALUES (?,?,?)", username, encoder.encode(password), "ADMIN");
        }
    }
}
