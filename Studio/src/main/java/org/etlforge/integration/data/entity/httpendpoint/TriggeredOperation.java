package org.etlforge.integration.data.entity.httpendpoint;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.http.HttpVerb;

public final class TriggeredOperation {
   private final OperationId opId;
   private final OperationRunMode runMode;
   private final boolean responseGenerator;
   private final HttpVerb httpVerb;
   private final boolean useRequestAsSource;

   public TriggeredOperation(OperationId var1, HttpVerb var2, OperationRunMode var3, boolean var4) {
      this(var1, var2, var3, var4, true);
   }

   public TriggeredOperation(OperationId var1, HttpVerb var2, OperationRunMode var3, boolean var4, boolean var5) {
      Preconditions.checkNotNull(var1, "opId");
      Preconditions.checkNotNull(var3, "runMode");
      Preconditions.checkArgument(!var4 || var3 != OperationRunMode.ASYNCHRONOUS, "The response generating operation must run synchronously");
      this.opId = var1;
      this.httpVerb = var2;
      this.runMode = var3;
      this.responseGenerator = var4;
      this.useRequestAsSource = var5;
   }

   public TriggeredOperation replaceOperationId(OperationId var1) {
      return new TriggeredOperation(var1, this.httpVerb, this.runMode, this.responseGenerator, this.useRequestAsSource);
   }

   public OperationId getOperationId() {
      return this.opId;
   }

   public OperationRunMode getRunMode() {
      return this.runMode;
   }

   public boolean isResponseGenerator() {
      return this.responseGenerator;
   }

   public boolean isUsingRequestAsSource() {
      return this.useRequestAsSource;
   }

   public HttpVerb getHttpVerb() {
      return this.httpVerb;
   }

   public boolean isForAllVerbs() {
      return this.httpVerb == null;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof TriggeredOperation)) {
         return false;
      }

      TriggeredOperation var2 = (TriggeredOperation)var1;
      return this.opId.equals(var2.opId)
         && this.httpVerb == var2.httpVerb
         && this.runMode == var2.runMode
         && this.responseGenerator == var2.responseGenerator
         && this.useRequestAsSource == var2.useRequestAsSource;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.opId.hashCode();
      var1 = 31 * var1 + (this.httpVerb != null ? this.httpVerb.hashCode() : 0);
      var1 = 31 * var1 + this.runMode.hashCode();
      var1 = 31 * var1 + (this.responseGenerator ? 1231 : 1237);
      return 31 * var1 + (this.useRequestAsSource ? 1231 : 1237);
   }

   @Override
   public String toString() {
      return "Verb: "
         + (this.httpVerb == null ? "all" : this.httpVerb)
         + " - Mode: "
         + this.runMode
         + " - Uses request as source: "
         + (this.useRequestAsSource ? "Yes" : "No")
         + " - Generates response: "
         + (this.responseGenerator ? "Yes" : "No");
   }
}
