package org.etlforge.integration.client.mapping.text;

import com.google.common.collect.Maps;
import java.util.HashMap;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_ext;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.callback.CallbackResult;

public final class ComplexTextStructureMappingTreeConverter {
   public static MappingTreeStructure convert(ComplexTextStructure var0, CallbackResult var1) {
      HashMap var2 = Maps.newHashMap();
      CROM var3 = null;
      boolean var4 = var0.isFixField();
      boolean var5 = false;

      for (Segment var7 : var0.getSegmentList()) {
         Segment var8 = var7.findParent(var0);
         CROM var9 = (CROM)var2.get(var8);
         CROM var10 = new CROM(var9, true, var7.getName(), -1, CromType.CROM_t_Text);
         if (var9 == null) {
            var10.addType(32768L);
         }

         CROM_ext var11 = var10.getExtension();
         String var12 = Utils.getDelimiterString(var0);
         var11.setDelimiter(var12);
         var11.setStrQual(var0.getStringQualifier());
         Field var13 = var7.findIdentField();
         if (var13 != null && var4) {
            var11.setIdentPos(var13.getBeginPosition());
            var11.setIdentLength(var13.getLength());
         }

         if (var3 != null && !var5) {
            var11 = var3.getExtension();
            var11.setDelimiter(var12);
            var11.setStrQual(var0.getStringQualifier());
            if (var13 != null && var4) {
               var11.setIdentPos(var13.getBeginPosition());
               var11.setIdentLength(var13.getLength());
            }

            var5 = true;
         }

         var2.put(var7, var10);
         if (var8 == null) {
            var3 = var10;
         }

         switch (var7.getOccurenceID()) {
            case 1:
               var10.setOneToOneNode();
               break;
            case 2:
               var10.setAtLeastOneNode();
               break;
            case 3:
               var10.setOptionalNode();
               break;
            case 4:
               var10.setOuterJoin();
         }

         for (Field var15 : var7.getFieldList()) {
            CROM var16 = new CROM(var10, false, var15.fieldName, -1, CromType.CROM_t_Text);
            var16.setValueType(var15.dataType);
            if (var15.equals(var13)) {
               var16.addType(4L);
            } else {
               var16.addType(36L);
            }

            if (var15.defaultValue != null && var15.defaultValue.length() > 0) {
               var16.setDefaultValue(var15.defaultValue);
               var16.addType(16777216L);
            }

            var11 = var16.getExtension();
            var11.setFormat(var15.format);
            if (var4) {
               var11.setFieldPos((short)var15.getBeginPosition());
               var11.setFieldLength((short)var15.getLength());
            }
         }
      }

      CromBasedMappingTreeStructure var17 = new CromBasedMappingTreeStructure(var3, new CROMHeader("Text"));
      if (var1 != null) {
         var1.succeeded(var17);
      }

      return var17;
   }

   private ComplexTextStructureMappingTreeConverter() {
   }
}
