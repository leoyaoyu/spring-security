package com.ibm.esw.leo.spring.oauth2.uaa.domain.entity;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 **/
@Data
public class Permission {

    private String id;
    private String code;
    private String description;
    private String url;
}
