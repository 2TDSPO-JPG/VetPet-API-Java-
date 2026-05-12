package com.challenger.sprint.javaspg.dto.function;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    private String email;

    private String senha;

}
