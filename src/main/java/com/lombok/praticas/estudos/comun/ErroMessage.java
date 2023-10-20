package com.lombok.praticas.estudos.comun;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErroMessage {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
    private String message;
}
