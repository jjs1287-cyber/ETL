package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import com.google.common.base.Function;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.io.FileUtils;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;
import org.etlforge.util.PropertyDependency;
import org.etlforge.util.StringProperty;
import org.etlforge.util.file.WindowsFileNameValidator;

public final class SoapSchemaModel extends AbstractPropertyBasedModel {
   public static final String BUSY = "busy";
   public static final String NAME = "name";
   public static final String VALID = "valid";
   public static final String CONFLICTS = "conflicts";
   public static final String DONE = "done";
   private final Property<Boolean> busy = this.newProperty("busy", Boolean.FALSE);
   private final StringProperty name = this.newStringProperty("name", "");
   private final Property<Boolean> valid = this.newProperty("valid", Boolean.FALSE);
   private final Property<Boolean> conflicts = this.newProperty("conflicts", Boolean.FALSE);
   private final Property<Boolean> done = this.newProperty("done", Boolean.FALSE);
   private final WebServiceOperationInfo opInfo;

   public SoapSchemaModel(WebServiceOperationInfo var1) {
      this.opInfo = var1;
      this.name.setAutoTrim(true);
      this.name.set(this.getSuggestedName());
      this.name.setAllowsNull(false);
      PropertyDependency.installAndSync(this.name, this.valid, new SoapSchemaModel.NameValidator());
   }

   private String getSuggestedName() {
      String var1 = this.opInfo.getProperty("Service");
      String var2 = this.opInfo.getProperty("Operation");
      String var3 = var1 + "_" + var2;
      String var4 = FileUtils.getSafeFileName(var3, '_');
      return var4.length() > 50 ? var4.substring(0, 50) : var4;
   }

   public WebServiceOperationInfo getOpInfo() {
      return this.opInfo;
   }

   public void setName(String var1) {
      this.name.set(var1);
   }

   public String getRequestXsdName() {
      String var1 = "_Request";
      return this.createXsdName(var1);
   }

   private String createXsdName(String var1) {
      String var2 = this.getName();
      if (var2.toLowerCase().endsWith(".xsd")) {
         var2 = var2.substring(0, var2.length() - 4);
      }

      return var2 + var1 + ".xsd";
   }

   public String getResponseXsdName() {
      String var1 = "_Response";
      return this.createXsdName(var1);
   }

   public String getName() {
      return this.name.get();
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }

   public void setConflictsDetected(boolean var1) {
      this.conflicts.set(var1);
   }

   public boolean isValid() {
      return this.valid.get();
   }

   public void setDone(boolean var1) {
      this.done.set(var1);
   }

   public int getNameMaxLength() {
      return 75;
   }

   private class NameValidator implements Function<String, Boolean> {
      private final WindowsFileNameValidator validator = new WindowsFileNameValidator();

      public NameValidator() {
         this.validator.setMaxLength(SoapSchemaModel.this.getNameMaxLength());
      }

      public Boolean apply(String var1) {
         return this.validator.isValid(var1);
      }
   }
}
