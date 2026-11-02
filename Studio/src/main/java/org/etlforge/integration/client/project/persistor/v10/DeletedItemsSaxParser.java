package org.etlforge.integration.client.project.persistor.v10;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.system.callback.Receiver;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.StartElementHandler;
import org.etlforge.xml.sax.StartOfElement;

final class DeletedItemsSaxParser {
   private final Receiver<EntityDescriptor> receiver;
   private Logger logger = Logger.getAnonymousLogger();

   public DeletedItemsSaxParser(Receiver<EntityDescriptor> var1) {
      this.receiver = var1;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void loadDeletedItems(File var1) throws XmlLoadingException {
      this.loadDeletedItems(Files.newInputStreamSupplier(var1));
   }

   @VisibleForTesting
   void loadDeletedItems(InputSupplier<? extends InputStream> var1) throws XmlLoadingException {
      try {
         KongaSaxParser var2 = new KongaSaxParser(new DeletedItemsSaxParser.Callback());
         var2.parseInput(var1);
      } catch (Exception var3) {
         throw new XmlLoadingException(var3);
      }
   }

   private class Callback {
      private Callback() {
      }

      @StartElementHandler("DeletedItems/*")
      public void handle(StartOfElement var1) {
         String var2 = var1.attributes().getValue("id_val");
         EntityDescriptor var3 = new EntityDescriptor(var1.localName(), var2);
         DeletedItemsSaxParser.this.receiver.handle(var3);
         DeletedItemsSaxParser.this.logger.log(Level.FINE, "Marked as deleted: " + var3);
      }
   }
}
