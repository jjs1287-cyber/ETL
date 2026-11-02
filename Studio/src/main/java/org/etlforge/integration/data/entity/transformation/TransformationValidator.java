package org.etlforge.integration.data.entity.transformation;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;

public final class TransformationValidator {
   private final Transformation tf;
   private final ValidationMessageCollector collector;

   public TransformationValidator(Transformation var1, ValidationMessageCollector var2) {
      this.tf = var1;
      this.collector = var2;
   }

   public void validate() {
      this.validateMappings();
      this.validateStructures();
   }

   private void validateMappings() {
      if (this.tf.hasInvalidMappings()) {
         this.collector.addMessage("The transformation contains invalid mappings", ValidationStatus.WARNING);
      }
   }

   private void validateStructures() {
      DataStructure var1 = this.tf.getInputStructure();
      if (var1 instanceof DatabaseStructure) {
         this.validateDatabaseInputStructure((DatabaseStructure)var1);
      }
   }

   private void validateDatabaseInputStructure(DatabaseStructure var1) {
      if (var1.hasManualSqlStatement()) {
         DriverDescriptor var2 = var1.getDbLocation().getDriver();
         if (this.isNotJdbc(var2)) {
            this.collector.addMessage("Manual SQL statements can only be used with a JDBC driver", ValidationStatus.INVALID);
         }
      }
   }

   private boolean isNotJdbc(DriverDescriptor var1) {
      return var1 != null && var1.getType() != DriverType.JDBC;
   }
}
