package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.lang.exception.ExceptionHandler;

public interface MappingErrorHandler extends ExceptionHandler<TreeMapperException> {
   MappingErrorHandler STACK_TRACE_WRITER = new MappingErrorHandler() {
      public void caught(TreeMapperException var1) {
         var1.printStackTrace();
      }
   };
   MappingErrorHandler LOGGER = new MappingErrorHandler() {
      public void caught(TreeMapperException var1) {
         ErrorLog.log(TreeMapper.class, "There is a problem with the current mappings: " + var1.getMessage(), var1);
      }
   };
   MappingErrorHandler IGNORE = new MappingErrorHandler() {
      public void caught(TreeMapperException var1) {
      }
   };
}
