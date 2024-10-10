package com.example.demojwt.constant;

public class PermissionConstant {
    private static final String PRE_FIX = " http://localhost:8080/api/v1";public static final String MANAGE_ROLES = PRE_FIX + "/roles";
    public static final String CREATE_STUDENT = PRE_FIX + "/students/create-student";
    public static final String UPDATE_STUDENT = PRE_FIX + "/students/update-student";
    public static final String DELETE_STUDENT = PRE_FIX + "/students/delete-student";
    public static final String SEARCH_STUDENT = PRE_FIX + "/students/search-student";

}
