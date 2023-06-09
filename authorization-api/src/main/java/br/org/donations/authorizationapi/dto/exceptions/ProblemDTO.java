package br.org.donations.authorizationapi.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDTO {
    private int status;
    private OffsetDateTime dateAndTime;
    private String title;
    private String path;
}
