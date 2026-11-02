package com.etlforge.integration.server.engine.bulkload.rest;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

final class ByteArrayResponseHandler implements ResponseHandler<byte[]> {
   public byte[] handleResponse(HttpResponse var1) throws ClientProtocolException, IOException {
      HttpEntity var2 = var1.getEntity();
      return var2 != null ? EntityUtils.toByteArray(var2) : null;
   }
}
