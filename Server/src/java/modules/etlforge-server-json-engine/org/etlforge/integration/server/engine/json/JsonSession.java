package org.etlforge.integration.server.engine.json;

import java.io.File;
import java.util.logging.Logger;
import org.etlforge.integration.json.xml.JsonToXmlGenerator;
import org.etlforge.integration.json.xml.XmlToJsonGenerator;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.engine.EngineSession;

public final class JsonSession extends EngineSession<JsonSession> {
   private final Logger logger;

   JsonSession(ExternalApiSupport var1, Logger var2) {
      super(new JsonSessionId(), var1);
      this.logger = var2;
   }

   public JsonSessionId getId() {
      return (JsonSessionId)super.getId();
   }

   @Override
   protected void preCloseConnectionCleanup() {
   }

   public void jsonToXml(ConversionParams var1, XsdLocator var2) throws Exception {
      this.logger
         .info(
            String.format(
               "Received request for JSON-to-XML conversion. Transformation ID: %d. JSON file:\"%s\". XML file: \"%s\"",
               var1.getTransformationId(),
               var1.getJsonFile().getAbsolutePath(),
               var1.getXmlFile().getAbsolutePath()
            )
         );
      var1.verifyJsonFileExists();
      JsonToXmlGenerator var3 = new JsonToXmlGenerator();
      File var4 = var2.getSourceXsd(var1.getTransformationId());
      this.logger.fine("XSD file: " + var4.getAbsolutePath());
      var3.setXsdFile(var4);
      var3.generateXml(var1.getJsonFile(), var1.getXmlFile());
      this.logger.info("The JSON file was converted successfully.");
   }

   public void xmlToJson(ConversionParams var1, XsdLocator var2) throws Exception {
      this.logger
         .info(
            String.format(
               "Received request for XML-to-JSON conversion. Transformation ID: %d. XML file:\"%s\". JSON file: \"%s\"",
               var1.getTransformationId(),
               var1.getXmlFile().getAbsolutePath(),
               var1.getJsonFile().getAbsolutePath()
            )
         );
      var1.verifyXmlFileExists();
      XmlToJsonGenerator var3 = new XmlToJsonGenerator();
      File var4 = var2.getTargetXsd(var1.getTransformationId());
      this.logger.fine("XSD file: " + var4.getAbsolutePath());
      var3.setOutputFile(var1.getJsonFile());
      var3.convertToJson(var1.getXmlFile(), var4);
      this.logger.info("The XML file was converted successfully.");
   }
}
