package org.etlforge.application.update;

public class JsonResponseParser implements HttpVersionInfoSupplier.ResponseParser {
   @Override
   public VersionInfo parse(String var1) throws Exception {
      return new JsonVersionInfo(var1);
   }
}
