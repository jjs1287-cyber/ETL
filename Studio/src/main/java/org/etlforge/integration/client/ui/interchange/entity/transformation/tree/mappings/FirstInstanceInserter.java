package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.data.script.Transform;

final class FirstInstanceInserter {
   private static final String REGEX = "(?<!#_?\\d{1,10})\\.";
   private static final Pattern PATTERN = Pattern.compile("(?<!#_?\\d{1,10})\\.");
   private List<String> sourceDeList = Lists.newArrayList();
   private Boolean isChangeAllowedByUser;

   public FirstInstanceInserter() {
   }

   public void setSourceDeList(List<String> var1) {
      this.sourceDeList = var1;
   }

   public String processExpression(String var1) {
      if (this.userHasDeclined()) {
         return null;
      }

      for (String var3 : this.sourceDeList) {
         Matcher var4 = PATTERN.matcher(var3);
         if (var4.find() && this.isChangeAllowedByUser()) {
            return this.setFirstInstance(var1);
         }
      }

      return null;
   }

   private boolean userHasDeclined() {
      return this.isChangeAllowedByUser != null && !this.isChangeAllowedByUser;
   }

   private boolean isChangeAllowedByUser() {
      if (this.isChangeAllowedByUser == null) {
         Answer var1 = Ask.yesOrNo(
            "Do you want to change the mapping to use the first instance for each source?",
            "Single instance target cannot be mapped by multiple instance source."
         );
         this.isChangeAllowedByUser = var1.isYes();
      }

      return this.isChangeAllowedByUser;
   }

   private String setFirstInstance(String var1) {
      Transform var2 = new Transform();
      List var3 = var2.tokenize(var1);

      for (Object __etlItem59 : var3) {
         Transform.Token var5 = (Transform.Token) __etlItem59;
         if (var5.m_id == 405) {
            String var6 = var5.m_str;
            Matcher var7 = PATTERN.matcher(var6);
            if (var7.find()) {
               var5.m_str = var6.replaceAll("(#)+\\.", "\\#1\\.").replaceAll("(?<!#_?\\d{1,10})\\.", "\\#1\\.");
            }
         }
      }

      StringBuilder var8 = new StringBuilder();

      for (Object __etlItem71 : var3) {
         Transform.Token var10 = (Transform.Token) __etlItem71;
         var8.append(var10.m_str);
      }

      return var8.toString();
   }
}
