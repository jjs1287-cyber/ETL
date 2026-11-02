package org.etlforge.integration.http;

public interface HttpConnector {
   String REQUEST_HEADERS = "http_request_headers";
   String SSL_VERSION = "http_ssl_version";

   String getUrl();

   void setUrl(String var1);

   HttpProperties getHttpProperties();

   void setHttpProperties(HttpProperties var1);

   HttpVerb getHttpVerb();

   void setHttpVerb(HttpVerb var1);

   SslVersion getSslVersion();

   void setSslVersion(SslVersion var1);

   String getRequestHeaders();

   void setRequestHeaders(String var1);
}
