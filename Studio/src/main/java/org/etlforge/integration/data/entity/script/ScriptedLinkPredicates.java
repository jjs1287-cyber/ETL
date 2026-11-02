package org.etlforge.integration.data.entity.script;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Script;

public enum ScriptedLinkPredicates implements Predicate<ScriptedLink> {
   OPERATION {
      public boolean apply(ScriptedLink var1) {
         return var1.getMainObject() instanceof Operation;
      }
   },
   SCRIPT {
      public boolean apply(ScriptedLink var1) {
         return var1.getMainObject() instanceof Script;
      }
   };

   ScriptedLinkPredicates() {
   }
}
