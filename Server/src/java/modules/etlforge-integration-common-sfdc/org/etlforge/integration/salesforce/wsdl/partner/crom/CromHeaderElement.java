package org.etlforge.integration.salesforce.wsdl.partner.crom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CromType;

public abstract class CromHeaderElement {
   private final ImmutableList<CromHeaderElement.Folder> path;
   private final boolean required;
   private final ImmutableList<CromHeaderValue> values;

   protected CromHeaderElement(String var1, CromHeaderValue... var2) {
      this(singleFolder(var1), var2);
   }

   private static final ImmutableList<CromHeaderElement.Folder> singleFolder(String var0) {
      CromHeaderElement.Folder var1 = new CromHeaderElement.Folder(var0, CromHeaderElement.Cardinality.SINGLE);
      return ImmutableList.of(var1);
   }

   protected CromHeaderElement(ImmutableList<CromHeaderElement.Folder> var1, CromHeaderValue... var2) {
      this(var1, false, var2);
   }

   protected CromHeaderElement(String var1, boolean var2, CromHeaderValue... var3) {
      this(singleFolder(var1), var2, var3);
   }

   protected CromHeaderElement(ImmutableList<CromHeaderElement.Folder> var1, boolean var2, CromHeaderValue... var3) {
      this.path = var1;
      this.required = var2;
      this.values = ImmutableList.copyOf(var3);
   }

   public final CROM insertUnder(CROM var1) {
      CROM var2 = this.createHeaderPath(var1, null);
      this.addValueNodes(var2);
      return var2;
   }

   private void addValueNodes(CROM var1) {
      for (CromHeaderValue var3 : this.values) {
         var3.createCrom(var1);
      }
   }

   public final CROM insertAfter(CROM var1) {
      CROM var2 = var1.getParent();
      CROM var3 = this.createHeaderPath(var2, var1);
      this.addValueNodes(var3);
      return var3;
   }

   private CROM createHeaderPath(CROM var1, CROM var2) {
      CromHeaderElement.Folder var3 = this.path.get(0);
      CROM var4 = this.createHeaderRoot(var3, var1, var2);
      CROM var5 = var4;

      for (CromHeaderElement.Folder var7 : Iterables.skip(this.path, 1)) {
         CROM var8 = var5;
         var5 = new CROM(CromType.CROM_t_Xml, var8);
         var5.setName(var7.name);
         this.configureHeaderNode(var5, var7.cardinality);
      }

      return var5;
   }

   private CROM createHeaderRoot(CromHeaderElement.Folder var1, CROM var2, CROM var3) {
      CROM var4;
      if (var3 == null) {
         var4 = new CROM(CromType.CROM_t_Xml, var2);
         var4.setName(var1.name);
      } else {
         int var5 = var2.indexOfChild(var3);
         var4 = new CROM(var2, true, var1.name, var5 + 1, CromType.CROM_t_Xml);
      }

      this.configureHeaderNode(var4, var1.cardinality);
      return var4;
   }

   private void configureHeaderNode(CROM var1, CromHeaderElement.Cardinality var2) {
      var1.setType(1L);
      switch (var2) {
         case SINGLE:
            if (!this.required) {
               CromUtils.makeOptional(var1);
            }
            break;
         case LOOP:
            if (this.required) {
               CromUtils.makeRequiredLoop(var1);
            } else {
               CromUtils.makeOptionalLoop(var1);
            }
            break;
         default:
            throw new AssertionError(var2);
      }

      CromUtils.insertXmlExtensionForHeader(var1);
   }

   public enum Cardinality {
      SINGLE,
      LOOP;
   }

   public static final class Folder {
      private final String name;
      private final CromHeaderElement.Cardinality cardinality;

      public Folder(String var1, CromHeaderElement.Cardinality var2) {
         this.name = var1;
         this.cardinality = var2;
      }
   }
}
