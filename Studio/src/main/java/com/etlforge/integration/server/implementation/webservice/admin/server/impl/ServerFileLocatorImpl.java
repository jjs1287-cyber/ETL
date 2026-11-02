package com.etlforge.integration.server.implementation.webservice.admin.server.impl;

import com.google.common.base.Function;
import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.WsServerFileLocator;
import java.util.Date;
import org.etlforge.integration.common.server.ServerConversionUtils;

final class ServerFileLocatorImpl implements ServerFileLocator {
   public static final Function<WsServerFileLocator, ServerFileLocator> FROM_SERVER = new Function<WsServerFileLocator, ServerFileLocator>() {
      public ServerFileLocator apply(WsServerFileLocator var1) {
         return new ServerFileLocatorImpl(var1);
      }
   };
   private final String path;
   private final Date modified;
   private final long size;

   public ServerFileLocatorImpl(WsServerFileLocator var1) {
      this.path = var1.getPath();
      this.modified = ServerConversionUtils.fromServerDateFormat(var1.getLastModified());
      this.size = var1.getSizeInBytes();
   }

   @Override
   public String getPath() {
      return this.path;
   }

   @Override
   public Date getModifiedDate() {
      return this.modified;
   }

   @Override
   public long getSizeInBytes() {
      return this.size;
   }
}
