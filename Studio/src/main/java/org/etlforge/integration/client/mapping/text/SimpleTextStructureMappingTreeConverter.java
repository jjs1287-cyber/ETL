package org.etlforge.integration.client.mapping.text;

import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Text;
import org.etlforge.integration.structure.crom.CROM_ext;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.callback.CallbackResult;

public final class SimpleTextStructureMappingTreeConverter {
   public static MappingTreeStructure convert(SimpleTextStructure var0, CallbackResult var1) {
      CROM var2 = new CROM(null, true, "", -1, CromType.CROM_t_Text);
      var2.addType(32768L);
      Segment var3 = var0.getSegment();
      CROM var4 = new CROM(var2, true, var3.getName(), -1, CromType.CROM_t_Text);
      var4.addType(8L);
      if (var0.getDelimiter() != null || var0.getStringQualifier() != null) {
         CROM_ext var5 = var4.getExtension();
         if (var5 == null) {
            var5 = new CROM_Text();
         }

         if (var0.getDelimiter() != null) {
            var5.setDelimiter(Utils.getDelimiterString(var0));
         }

         var5.setStrQual(var0.getStringQualifier());
      }

      for (Field var6 : var0.getSegment().fieldList) {
         CROM var7 = new CROM(var4, false, var6.fieldName, -1, CromType.CROM_t_Text);
         var7.setValueType(var6.dataType);
         var7.addType(36L);
         var7.setDefaultValue(var6.defaultValue);
         var7.getExtension().setFormat(var6.format);
         var7.getExtension().setFieldPos((short)var6.getBeginPosition());
         var7.getExtension().setFieldLength((short)var6.getLength());
      }

      CromBasedMappingTreeStructure var9 = new CromBasedMappingTreeStructure(var2, new CROMHeader("Text"));
      if (var1 != null) {
         var1.succeeded(var9);
      }

      return var9;
   }

   private SimpleTextStructureMappingTreeConverter() {
   }
}
