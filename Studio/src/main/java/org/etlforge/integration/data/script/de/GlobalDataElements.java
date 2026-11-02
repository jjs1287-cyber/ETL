package org.etlforge.integration.data.script.de;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ExpressionSource;

public interface GlobalDataElements {
   GlobalDataElements EMPTY = new GlobalDataElements() {
      @Override
      public void updateReferences(ExpressionSource var1, Set<DataElementReference> var2) {
      }

      @Override
      public void scanProject(IntegrationProject var1) {
      }

      @Override
      public void removeListener(ExpressionSource var1, GlobalDataElementsListener var2) {
      }

      @Override
      public void removeListener(GlobalDataElementsListener var1) {
      }

      @Override
      public void remove(ExpressionSource var1) {
      }

      @Override
      public void parseExpression(ExpressionSource var1, String var2) {
      }

      @Override
      public Set<DataElementReference> getReferences(String var1) {
         return Collections.emptySet();
      }

      @Override
      public SortedSet<String> getAllDataElementsWithPrefix() {
         return Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      }

      @Override
      public SortedSet<String> getAllDataElements() {
         return Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      }

      @Override
      public void addListener(ExpressionSource var1, GlobalDataElementsListener var2) {
      }

      @Override
      public void addListener(GlobalDataElementsListener var1) {
      }

      @Override
      public void setProcessFilter(ProcessFilter var1) {
      }

      @Override
      public ProcessFilter getProcessFilter() {
         return null;
      }
   };

   void scanProject(IntegrationProject var1);

   void parseExpression(ExpressionSource var1, String var2);

   void updateReferences(ExpressionSource var1, Set<DataElementReference> var2);

   void remove(ExpressionSource var1);

   SortedSet<String> getAllDataElements();

   SortedSet<String> getAllDataElementsWithPrefix();

   Set<DataElementReference> getReferences(String var1);

   void addListener(GlobalDataElementsListener var1);

   void removeListener(GlobalDataElementsListener var1);

   void addListener(ExpressionSource var1, GlobalDataElementsListener var2);

   void removeListener(ExpressionSource var1, GlobalDataElementsListener var2);

   void setProcessFilter(ProcessFilter var1);

   ProcessFilter getProcessFilter();
}
