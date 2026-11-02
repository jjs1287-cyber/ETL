package com.etlforge.integration.client.sfdc.data;

public final class MappingConstants {
   public static final String ROOT_GENERATOR_EXPR = "[root$]";
   public static final String TRANSACTION_ROOT_GENERATOR_EXPR = "[root$transaction.]";
   public static final String LOGIN_REQUEST_USER_FIELD = "root$transaction.body$login$username$";
   public static final String LOGIN_REQUEST_PASSWORD_FIELD = "root$transaction.body$login$password$";
   public static final String LOGIN_RESPONSE_SESSION_ID_FIELD = "root$transaction.response$body$loginResponse$result$sessionId$";
   public static final String LOGIN_RESPONSE_SERVER_URL_FIELD = "root$transaction.response$body$loginResponse$result$serverUrl$";
   public static final String LOGOUT_REQUEST_SESSION_ID_FIELD = "root$transaction.header$SessionHeader$sessionId$";
   public static final String REQUEST_PRECONDITION = "root$transaction./PRECONDITION/";
   public static final String REQUEST_SESSION_ID_FIELD = "root$transaction.header$SessionHeader$sessionId$";
   public static final String REQUEST_MRUHEADER_FIELD = "root$transaction.header$MruHeader$updateMru$";

   private MappingConstants() {
   }
}
