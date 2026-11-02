package org.etlforge.integration.data.script;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.data.script.de.DataElementReference;

public final class ExpressionParser {
   private final ExpressionSource source;
   private final Transform transform;
   private final ScriptValidator validator;
   private boolean sourceDataElementAware;

   public ExpressionParser(ExpressionSource var1) {
      this.source = Preconditions.checkNotNull(var1);
      this.transform = new Transform();
      this.validator = new ScriptValidator();
   }

   public ExpressionSource getSource() {
      return this.source;
   }

   public void setSourceDataElementContainer(SourceDataElementContainer var1) {
      this.validator.setSourceDataElementContainer(var1);
      this.transform.setSourceDataElementContainer(var1);
      this.sourceDataElementAware = true;
   }

   public ExpressionParser.Result parse(String var1) {
      List var2 = this.transform.tokenize(var1);
      if (var2 == null) {
         return new ExpressionParser.Result(this.createInvalidStatus(), null, null, null);
      }

      DataElementExtractor var3 = new DataElementExtractor();
      var3.setSourceDataElementAware(this.sourceDataElementAware);
      var3.parse(var2);
      ScriptStatus var4 = this.validate();
      return new ExpressionParser.Result(var4, var3.getGlobalDataElementReferences(this.source), var3.getLocalDataElements(), var3.getSourceDataElements());
   }

   private ScriptStatus createInvalidStatus() {
      return ScriptStatus.invalid(this.transform.m_err != null ? this.transform.m_err.getFormattedErrorMessage() : "Unknown error");
   }

   private ScriptStatus validate() {
      List var1 = this.transform.rpnOrder();
      return var1 != null ? this.validator.validate(var1) : this.createInvalidStatus();
   }

   public ImmutableSet<DataElementReference> extractDataElementReferences(String var1) {
      List var2 = this.transform.tokenize(var1);
      if (var2 != null) {
         DataElementExtractor var3 = new DataElementExtractor();
         var3.parse(var2);
         return var3.getGlobalDataElementReferences(this.source);
      } else {
         return null;
      }
   }

   public static final class Result {
      private final ScriptStatus status;
      private final ImmutableSet<DataElementReference> globalDeRefs;
      private final ImmutableSortedSet<String> localDEs;
      private final ImmutableSortedSet<String> sourceDEs;

      private Result(ScriptStatus var1, ImmutableSet<DataElementReference> var2, ImmutableSortedSet<String> var3, ImmutableSortedSet<String> var4) {
         this.status = var1;
         this.globalDeRefs = var2;
         this.localDEs = var3;
         this.sourceDEs = var4;
      }

      public ScriptStatus getStatus() {
         return this.status;
      }

      @Nullable
      public ImmutableSet<DataElementReference> getGlobalDataElementReferences() {
         return this.globalDeRefs;
      }

      @Nullable
      public ImmutableSet<String> getSourceDataElements() {
         return this.sourceDEs;
      }

      @Nullable
      public ImmutableSet<String> getLocalDataElements() {
         return this.localDEs;
      }
   }
}
