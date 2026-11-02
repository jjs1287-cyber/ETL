package org.etlforge.integration.server.engine.json;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.db.trandb.TransformationsTab;
import org.etlforge.integration.server.db.trandb.TransformationsTabRow;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class DbXsdLocator implements XsdLocator {
   private final Connection connection;

   public DbXsdLocator(Connection var1) {
      this.connection = var1;
   }

   @Override
   public File getSourceXsd(long var1) throws Exception {
      TransformationsTabRow var3 = this.queryTransformation(var1);
      String var4 = this.getRelativePathForSourceXsd(var1, var3);
      return this.createAbsolutePath(var4);
   }

   private TransformationsTabRow queryTransformation(long var1) throws SQLException {
      TransformationsTab var3 = new TransformationsTab(this.connection);
      TransformationsTabRow var4 = var3.query(var1);
      if (var4 == null) {
         throw new IllegalArgumentException("Unknown Transformation ID: " + var1);
      } else {
         return var4;
      }
   }

   private String getRelativePathForSourceXsd(long var1, TransformationsTabRow var3) {
      String var4 = var3.get_source_xml();
      return checkRelativePath(var3, var4, "source");
   }

   private static String checkRelativePath(TransformationsTabRow var0, String var1, String var2) {
      if (StringUtils.isEmpty(var1)) {
         throw new IllegalArgumentException(
            String.format("The transformation \"%s\" (ID = %d) does not have a %s XSD.", var0.get_transformation_name(), var0.get_transformation_id(), var2)
         );
      } else {
         return var1;
      }
   }

   private File createAbsolutePath(String var1) throws ServerConfigurationException {
      String var2 = ServerConfig.getServerHome();
      return new File(var2, var1);
   }

   @Override
   public synchronized File getTargetXsd(long var1) throws Exception {
      TransformationsTabRow var3 = this.queryTransformation(var1);
      String var4 = this.getRelativePathForTargetXsd(var1, var3);
      return this.createAbsolutePath(var4);
   }

   private String getRelativePathForTargetXsd(long var1, TransformationsTabRow var3) {
      String var4 = var3.get_target_xml();
      return checkRelativePath(var3, var4, "target");
   }
}
