package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AuthUserJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("enabled")
        Boolean enabled,
        @JsonProperty("accountNonExpired")
        Boolean accountNonExpired,
        @JsonProperty("accountNonLocked")
        Boolean accountNonLocked,
        @JsonProperty("credentialsNonExpired")
        Boolean credentialsNonExpired,
        @JsonProperty("authorities")
        List<AuthorityJson> authorities) {
}

