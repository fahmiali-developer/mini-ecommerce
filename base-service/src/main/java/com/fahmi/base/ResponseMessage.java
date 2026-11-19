package com.fahmi.base;

public final class ResponseMessage {

    public static final String LOGIN_SUCCESS = "Login berhasil";
    public static final String INVALID_CREDENTIALS = "Email atau password salah";
    public static final String ACCOUNT_INACTIVE = "Status akun tidak aktif";
    public static final String INVALID_REQUEST = "Request tidak valid";
    public static final String INTERNAL_SERVER_ERROR = "Terjadi kesalahan internal pada server";

    private ResponseMessage() {
    }
}
