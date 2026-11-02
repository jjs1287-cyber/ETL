package org.etlforge.integration.database.driver;

public interface DriverDescriptorVisitor {
   void visit(OdbcDriverDescriptor var1);

   void visit(JdbcDriverDescriptor var1);
}
