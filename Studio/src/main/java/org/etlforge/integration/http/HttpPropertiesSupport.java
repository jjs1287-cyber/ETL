package org.etlforge.integration.http;

import org.etlforge.collections.StringKeyValuePairs;

public final class HttpPropertiesSupport {
   private final StringKeyValuePairs data;

   public HttpPropertiesSupport(StringKeyValuePairs var1) {
      this.data = var1;
   }

   public HttpProperties createProperties() {
      return new HttpProperties(
         this.data.getBoolean("enable_content_encoding", false),
         this.data.getBoolean("enable_chunked_transfer_encoding", false),
         this.data.getBoolean("send_expect_100_continue", false)
      );
   }

   public void applyProperties(HttpProperties var1) {
      this.data.putBoolean("enable_content_encoding", var1.isContentEncodingEnabled());
      this.data.putBoolean("enable_chunked_transfer_encoding", var1.isChunkedTransferEncodingEnabled());
      this.data.putBoolean("send_expect_100_continue", var1.isSendingExpect100Continue());
   }
}
