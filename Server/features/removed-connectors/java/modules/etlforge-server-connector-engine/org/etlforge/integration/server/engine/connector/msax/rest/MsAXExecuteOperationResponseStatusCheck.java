package org.etlforge.integration.server.engine.connector.msax.rest;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorFailedResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorIllegalResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.SAXException;

public class MsAXExecuteOperationResponseStatusCheck {
   public static void checkExecuteOperationResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      MsAXExecuteOperationResponseStatusCheck.MsAXExecuteOperationResponseHandler var2 = new MsAXExecuteOperationResponseStatusCheck.MsAXExecuteOperationResponseHandler(
         var0
      );
      var1.registerHandler(var2);

      try {
         var1.parseFile(new File(var0));
      } catch (SAXException var4) {
         throw new ConnectorResponseProcessRuntimeException(var4);
      } catch (IOException var5) {
         throw new ConnectorResponseProcessRuntimeException(var5);
      }
   }

   @RelativePathRoot("executeoperationResponses/executeoperationResponse")
   private static class MsAXExecuteOperationResponseHandler {
      private boolean isSanityResponse = false;
      private boolean success = false;
      private String message = null;
      private final String xmlFile;

      MsAXExecuteOperationResponseHandler(String var1) {
         this.xmlFile = var1;
      }

      @EndElementHandler("success")
      public void success(Boolean var1) {
         this.isSanityResponse = true;
         this.success = var1;
         if (this.success) {
            this.stop();
         }
      }

      @EndElementHandler("message")
      public void message(String var1) {
         this.message = var1;
         this.stop();
      }

      @StartElementHandler
      public void value() {
         this.stop();
      }

      private void stop() {
         RelativePathRoot var1 = this.getClass().getAnnotation(RelativePathRoot.class);
         if (this.isSanityResponse) {
            if (!this.success) {
               String var4 = Strings.isNullOrEmpty(this.message) ? "No Message" : this.message;
               var4 = var4 + "\n the response is in the file " + this.xmlFile;
               throw new ConnectorFailedResponseRuntimeException("The error message: " + var4 + "\n");
            }
         } else {
            String var2 = "The response is unexpected : missing success element under " + var1.value();
            var2 = var2 + "\n the response is in the file " + this.xmlFile;
            throw new ConnectorIllegalResponseRuntimeException("The error message: " + var2 + "\n");
         }
      }
   }
}
