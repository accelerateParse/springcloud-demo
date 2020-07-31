package com.prey.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prey
 * @description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String username;

    private String token;

    private String refreshToken;

    private boolean skipVerification =false;
}
