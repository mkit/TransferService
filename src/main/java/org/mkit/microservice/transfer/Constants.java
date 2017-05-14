package org.mkit.microservice.transfer;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class Constants {

  public static final String PARAM_TRANSFER_ID = "transferId";

  public static final String PATH_CREATE = "/transfer";
  public static final String PATH_GET = "/transfer";
  public static final String PATH_GET_ONE = "/transfer/:" + PARAM_TRANSFER_ID;

  public static final String HOST_KEY = "listen.host";
  public static final String DEFAULT_HOST = "0.0.0.0";

  public static final String PORT_KEY = "listen.port";
  public static final int DEFAULT_PORT = 8060;

  public static final String ACCOUNTS_ADDRESS_KEY = "eb.accounts.address";
  public static final String DEFAULT_ACCOUNTS_ADDRESS = "accounts";

  public static final String HEADER_KEY_X_REQUESTED_WITH = "x-requested-with";
  public static final String HEADER_KEY_ORIGIN = "origin";
  public static final String HEADER_KEY_ACCEPT = "accept";
  public static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_KEY_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

  public static final String HEADER_APPLICATION_JSON_ENCODING = "application/json; charset=utf-8";

}
