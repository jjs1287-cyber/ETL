package org.etlforge.integration.server.implementation.webservice.impl.common.property_keys;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.etlforge.string.WildcardStringMatcher;

public class DeployKeyMappings {
   private final Map<String, ServerPropertyKey> keyMappings;
   private final List<ServerPropertyKey> wildCards;
   private final WildcardStringMatcher matcher = new WildcardStringMatcher(false);

   DeployKeyMappings(Map<String, ServerPropertyKey> var1) {
      this.keyMappings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      this.wildCards = Lists.newArrayList();
      this.matcher.setLeadingMatchSufficient(false);

      for (Entry var3 : var1.entrySet()) {
         String var4 = (String)var3.getKey();
         ServerPropertyKey var5 = (ServerPropertyKey)var3.getValue();
         if (var5.getType() == ServerPropertyKeyType.EXTENDED_WILDCARD) {
            this.wildCards.add(var5);
         } else {
            this.keyMappings.put(var4, var5);
         }
      }
   }

   public ServerPropertyKey get(String var1) {
      ServerPropertyKey var2 = this.keyMappings.get(var1);
      if (var2 != null) {
         return var2;
      }

      for (ServerPropertyKey var4 : this.wildCards) {
         String var5 = var4.getClientKeyName();
         if (this.matcher.matches(var5, var1)) {
            return new ServerPropertyKey(ServerPropertyKeyType.EXTENDED, var1, var1);
         }
      }

      return null;
   }
}
