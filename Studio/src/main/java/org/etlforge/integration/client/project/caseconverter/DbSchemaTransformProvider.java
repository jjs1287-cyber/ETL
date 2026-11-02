package org.etlforge.integration.client.project.caseconverter;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.io.Folder;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

abstract class DbSchemaTransformProvider {
   public abstract DbSchemaTransform getTransform(Element var1, SourceTarget var2) throws Exception;

   public static DbSchemaTransformProvider newProvider(Conversion var0, Folder var1) {
      switch (var0) {
         case SQL_SERVER_TO_ORACLE:
         case ORACLE_TO_ORACLE:
            return new DbSchemaTransformProvider.ToOracle(var0, var1);
         case ORACLE_TO_SQL_SERVER:
            return new DbSchemaTransformProvider.ToSqlServer();
         default:
            throw new AssertionError();
      }
   }

   private static class ToOracle extends DbSchemaTransformProvider {
      private final Conversion conversion;
      private final Folder projectRoot;
      private SourceTarget sourceOrTarget;
      private final Map<String, DbSchemaTransform> knownTransforms;

      public ToOracle(Conversion var1, Folder var2) {
         assert var1 == Conversion.SQL_SERVER_TO_ORACLE || var1 == Conversion.ORACLE_TO_ORACLE;
         this.conversion = var1;
         this.projectRoot = var2;
         this.knownTransforms = Maps.newHashMap();
      }

      @Override
      public DbSchemaTransform getTransform(Element var1, SourceTarget var2) throws Exception {
         this.sourceOrTarget = var2;
         String var3 = this.getSourceOrTargetId(var1);
         DbSchemaTransform var4 = this.knownTransforms.get(var3);
         if (var4 == null) {
            var4 = this.createNewTransform(var3);
            this.knownTransforms.put(var3, var4);
         }

         return var4;
      }

      private String getSourceOrTargetId(Element var1) {
         switch (this.sourceOrTarget) {
            case Source:
               return var1.getAttribute("sourceId");
            case Target:
               return var1.getAttribute("targetId");
            default:
               throw new AssertionError();
         }
      }

      private DbSchemaTransform createNewTransform(String var1) throws Exception {
         String var2 = this.fetchSchemaNameFromLoginName(var1);
         switch (this.conversion) {
            case SQL_SERVER_TO_ORACLE:
               return DbSchemaTransform.sqlServerToOracle(var2);
            case ORACLE_TO_ORACLE:
               return DbSchemaTransform.oracleSchemaChange(var2);
            default:
               throw new AssertionError();
         }
      }

      private String fetchSchemaNameFromLoginName(String var1) throws Exception, XPathExpressionException {
         EntityType var2 = this.sourceOrTarget == SourceTarget.Source ? EntityType.Source : EntityType.Target;
         File var3 = this.projectRoot.subFolder("Data").subFolder(var2.name()).getFile(var1 + ".xml");
         Document var4 = DomParser.parseFile(var3);
         XPath var5 = XPathFactory.newInstance().newXPath();
         NodeList var6 = (NodeList)var5.evaluate("/Entity/Location/Data/Properties/Item[@key='login']", var4, XPathConstants.NODESET);
         if (var6.getLength() == 0) {
            throw new Exception("The user name has not been defined for the source or target with ID " + var1);
         }

         Element var7 = (Element)var6.item(0);
         return var7.getAttribute("value").toUpperCase();
      }
   }

   private static class ToSqlServer extends DbSchemaTransformProvider {
      private final DbSchemaTransform instance = DbSchemaTransform.oracleToSqlServer();

      private ToSqlServer() {
      }

      @Override
      public DbSchemaTransform getTransform(Element var1, SourceTarget var2) throws Exception {
         return this.instance;
      }
   }
}
