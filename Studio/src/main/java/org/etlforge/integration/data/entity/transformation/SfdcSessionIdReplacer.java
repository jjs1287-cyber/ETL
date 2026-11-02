package org.etlforge.integration.data.entity.transformation;

import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

final class SfdcSessionIdReplacer {
   private static final String REGEX = buildRegex();
   private final List<Mapping> mappings;
   private final Transformation tx;
   private final ReferenceReplacementService refReplacementService;

   private static String buildRegex() {
      String var0 = Pattern.quote("__Salesforce.SessionID");
      String var1 = buildGuidRegex();
      return String.format(".+\\\"%s\\.(%s)\\\".+", var0, var1);
   }

   private static String buildGuidRegex() {
      String var0 = "[0-9a-fA-F]";
      return String.format("%s{8}-%s{4}-%s{4}-%s{4}-%s{12}", var0, var0, var0, var0, var0);
   }

   public SfdcSessionIdReplacer(Transformation var1, ReferenceReplacementService var2) {
      this.tx = var1;
      this.refReplacementService = var2;
      this.mappings = var1.getMappings();
   }

   public void run() {
      Mapping var1 = this.findMapping();
      if (var1 != null) {
         Mapping var2 = this.replaceExpression(var1);
         if (var2 != var1) {
            this.updateMapList(var1, var2);
         }
      }
   }

   private Mapping findMapping() {
      NodePath var1 = NodePath.of("root$transaction.header$SessionHeader$sessionId$");

      for (Mapping var3 : this.mappings) {
         if (var3.getTargetPath().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private Mapping replaceExpression(Mapping var1) {
      String var2 = var1.getExpression();
      SalesforceConnectorId var3 = extractOrgIdFromSessionIdMapping(var2);
      if (var3 == null) {
         return var1;
      }

      SalesforceConnector var4 = this.refReplacementService.getReplacement(var3, SalesforceConnector.class);
      if (var4 == null) {
         return var1;
      }

      String var5 = var2.replace(var3.toString(), var4.getID().toString());
      return MappingFactory.newMapping(var1.getTargetPath(), var5);
   }

   private void updateMapList(Mapping var1, Mapping var2) {
      this.mappings.remove(var1);
      this.mappings.add(var2);
      this.tx.setMappings(this.mappings);
   }

   @VisibleForTesting
   static SalesforceConnectorId extractOrgIdFromSessionIdMapping(String var0) {
      Pattern var1 = Pattern.compile(REGEX, 2);
      Matcher var2 = var1.matcher(var0);
      if (var2.find()) {
         String var3 = var2.group(1);
         if (var3 != null) {
            return new SalesforceConnectorId(var3);
         }
      }

      return null;
   }
}
