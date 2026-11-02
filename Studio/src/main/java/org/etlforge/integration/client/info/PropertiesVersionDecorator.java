package org.etlforge.integration.client.info;

import com.google.common.base.Strings;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.etlforge.application.VersionCommentDecorator;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.version.Version;
import org.etlforge.util.version.VersionDecorator;

final class PropertiesVersionDecorator implements VersionDecorator {
   private final String edition;

   public static PropertiesVersionDecorator fromResource(URL var0) throws IOException {
      Properties var1 = PropertiesReader.read(Resources.newInputStreamSupplier(var0));
      return new PropertiesVersionDecorator(var1);
   }

   public PropertiesVersionDecorator(Properties var1) {
      this.edition = var1.getProperty("edition");
   }

   public Version apply(Version var1) {
      if (Strings.isNullOrEmpty(this.edition)) {
         return var1;
      }

      VersionCommentDecorator var2 = new VersionCommentDecorator(this.edition);
      return var2.apply(var1);
   }
}
