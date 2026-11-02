package org.etlforge.application.update;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import org.codehaus.jackson.JsonParseException;
import org.etlforge.application.VersionFactory;
import org.etlforge.json.dom.ArrayNode;
import org.etlforge.json.dom.FieldNode;
import org.etlforge.json.dom.JsonDomParser;
import org.etlforge.json.dom.JsonNode;
import org.etlforge.json.dom.ObjectNode;
import org.etlforge.json.dom.RootNode;
import org.etlforge.json.dom.ScalarValueNode;
import org.etlforge.json.dom.ValueNode;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class JsonVersionInfo implements VersionInfo {
   private final BranchListVersionInfo branches = new BranchListVersionInfo();

   public JsonVersionInfo(String var1) throws IOException, JsonParseException {
      this(parseJson(var1));
   }

   private static RootNode parseJson(String var0) throws IOException, JsonParseException {
      JsonDomParser var1 = new JsonDomParser();
      return var1.parseJson(var0);
   }

   public JsonVersionInfo(RootNode var1) {
      ArgCheck.checkInstance(var1, ArrayNode.class);

      for (JsonNode var3 : var1.getChildren()) {
         ObjectNode var4 = (ObjectNode)var3;
         this.parseApp(var4);
      }
   }

   private void parseApp(ObjectNode var1) {
      String var2 = getFieldStringValue(var1, "app-id");
      FieldNode var3 = var1.getField("branches");
      ArrayNode var4 = (ArrayNode)var3.getValue();

      for (JsonNode var6 : var4.getChildren()) {
         try {
            ObjectNode var7 = (ObjectNode)var6;
            Branch var8 = getBranch(var7);
            String var9 = getFieldStringValue(var7, "version");
            Version var10 = VersionFactory.fromString(var9);
            String var11 = getFieldStringValue(var7, "download-url");
            long var12 = getFileSize(var7);
            Date var14 = getReleaseDate(var7);
            Set var15 = getBranchesRecommendedFor(var8, var7);
            LatestVersion var16 = new LatestVersion(var10, var11, var14, var12, var15);
            this.branches.addBranch(new BranchVersion(var2, var8, var16));
         } catch (IllegalVersionException var17) {
            throw new RuntimeException(var17);
         }
      }
   }

   private static Branch getBranch(ObjectNode var0) {
      String var1 = getFieldStringValue(var0, "branch");
      return Branch.parse(var1);
   }

   private static String getFieldStringValue(ObjectNode var0, String var1) {
      FieldNode var2 = var0.getField(var1);
      if (var2 == null) {
         return null;
      }

      ScalarValueNode var3 = (ScalarValueNode)var2.getValue();
      return (String)var3.getValue();
   }

   private static long getFileSize(ObjectNode var0) {
      FieldNode var1 = var0.getField("file-size");
      if (var1 == null) {
         return -1L;
      }

      ScalarValueNode var2 = (ScalarValueNode)var1.getValue();
      Number var3 = (Number)var2.getValue();
      return var3 == null ? -1L : var3.longValue();
   }

   private static Date getReleaseDate(ObjectNode var0) {
      FieldNode var1 = var0.getField("release-date");
      if (var1 == null) {
         return null;
      }

      ScalarValueNode var2 = (ScalarValueNode)var1.getValue();
      String var3 = (String)var2.getValue();
      if (Strings.isNullOrEmpty(var3)) {
         return null;
      }

      try {
         SimpleDateFormat var4 = new SimpleDateFormat("yyyy-MM-dd");
         return var4.parse(var3);
      } catch (ParseException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private static Set<Branch> getBranchesRecommendedFor(Branch var0, ObjectNode var1) {
      FieldNode var2 = var1.getField("recommended-for");
      if (var2 == null) {
         return ImmutableSet.of(var0);
      }

      Builder var3 = ImmutableSet.builder();
      ArrayNode var4 = (ArrayNode)var2.getValue();

      for (ValueNode var6 : var4.getChildren()) {
         String var7 = (String)((ScalarValueNode)var6).getValue();
         var3.add(Branch.parse(var7));
      }

      return var3.build();
   }

   @Override
   public LatestVersions getLatestVersions(BranchId var1) {
      return this.branches.getLatestVersions(var1);
   }

   @Override
   public String toString() {
      return this.branches.toString();
   }
}
