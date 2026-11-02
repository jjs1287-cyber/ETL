package org.etlforge.integration.data.location;
import com.google.common.collect.ImmutableList;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
public final class GlobalVariableLocation extends FileLocation {
   public GlobalVariableLocation() { super(DataLocationType.GlobalVariable); }
   public static String[] getPropertyKeys() { return org.etlforge.lang.Arrays2.append(FileLocation.getPropertyKeys(), new String[]{"locator"}); }
   public String getVariableName() { return getProperty("locator", ""); }
   public void setVariableName(String value) { setStringProperty("locator", value.trim()); }
   public static boolean isValidVariableName(String value) { return value != null && value.matches("\\$?[A-Za-z_][A-Za-z0-9_.]{0,255}") && !value.replace("$", "").startsWith("__"); }
   @Override public void collectValidationMessages(ValidationMessageCollector messages) {
      super.collectValidationMessages(messages);
      if (!isValidVariableName(getVariableName())) messages.addMessage("Enter a global variable name, for example Payload or $Payload.", ValidationStatus.INVALID);
   }
   @Override public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(new ExportableStringProperty(this, "locator", "Global variable", false));
   }
}
