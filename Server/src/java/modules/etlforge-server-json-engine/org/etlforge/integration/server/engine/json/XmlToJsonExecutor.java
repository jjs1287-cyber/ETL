package org.etlforge.integration.server.engine.json;

import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class XmlToJsonExecutor extends SessionExecutionWrapper<JsonSession, Void> {
   private final ConversionParams params;
   private final XsdLocator xsdLocator;

   public XmlToJsonExecutor(JsonSession var1, ConversionParams var2, XsdLocator var3) {
      super(var1);
      this.params = var2;
      this.xsdLocator = var3;
   }

   protected Void doWork(JsonSession var1) throws EngineSessionException {
      try {
         var1.xmlToJson(this.params, this.xsdLocator);
         return null;
      } catch (Exception var3) {
         throw new EngineSessionException(var3);
      }
   }
}
