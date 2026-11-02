package org.etlforge.integration.server.progress;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public final class EnterNewSectionMessage implements OperationProgressMessage {
   private final String section;
   private final String message;

   public EnterNewSectionMessage(String var1) {
      this(var1, "");
   }

   public EnterNewSectionMessage(String var1, String var2) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "section");
      Preconditions.checkNotNull(var2, "message");
      this.section = var1;
      this.message = var2;
   }

   @Override
   public String getSection() {
      return this.section;
   }

   @Override
   public String getMessage() {
      return this.message;
   }

   @Override
   public Date getEstimatedEndTime() {
      return null;
   }

   @Override
   public long getEstimatedTimeLeftInSeconds() {
      return -1L;
   }

   @Override
   public int getPercentageDone() {
      return -1;
   }
}
