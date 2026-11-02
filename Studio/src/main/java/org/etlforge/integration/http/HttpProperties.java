package org.etlforge.integration.http;

public final class HttpProperties {
   public static final String ENABLE_CONTENT_ENCODING = "enable_content_encoding";
   public static final String ENABLE_CHUNKED_TRANSFER_ENCODING = "enable_chunked_transfer_encoding";
   public static final String SEND_EXPECT_100_CONTINUE = "send_expect_100_continue";
   private final boolean enableContentEncoding;
   private final boolean enableChunkedTransferEncoding;
   private final boolean sendExpect100Continue;

   public HttpProperties() {
      this(false, false, false);
   }

   public HttpProperties(boolean var1, boolean var2, boolean var3) {
      this.enableContentEncoding = var1;
      this.enableChunkedTransferEncoding = var2;
      this.sendExpect100Continue = var3;
   }

   public boolean isContentEncodingEnabled() {
      return this.enableContentEncoding;
   }

   public boolean isChunkedTransferEncodingEnabled() {
      return this.enableChunkedTransferEncoding;
   }

   public boolean isSendingExpect100Continue() {
      return this.sendExpect100Continue;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof HttpProperties)) {
         return false;
      }

      HttpProperties var2 = (HttpProperties)var1;
      return this.enableContentEncoding == var2.enableContentEncoding
         && this.enableChunkedTransferEncoding == var2.enableChunkedTransferEncoding
         && this.sendExpect100Continue == var2.sendExpect100Continue;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + (this.enableContentEncoding ? 0 : 1);
      var1 = 31 * var1 + (this.enableChunkedTransferEncoding ? 0 : 1);
      return 31 * var1 + (this.sendExpect100Continue ? 0 : 1);
   }

   @Override
   public String toString() {
      return "HttpProperties[enableContentEncoding="
         + this.enableContentEncoding
         + ", enableChunkedTransferEncoding="
         + this.enableChunkedTransferEncoding
         + ", sendExpect100Continue="
         + this.sendExpect100Continue
         + "]";
   }
}
