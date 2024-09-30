package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AuthorityJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        String authority) {
}
