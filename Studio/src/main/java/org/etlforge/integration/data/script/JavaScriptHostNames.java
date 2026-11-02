package org.etlforge.integration.data.script;
public final class JavaScriptHostNames {
 private JavaScriptHostNames(){}
 public static final String[] HOSTS={"ETLForge.ReadFile","ETLForge.WriteFile","ETLForge.DbExecute","ETLForge.DbLookup","ETLForge.GetVar","ETLForge.SetVar","WriteToOperationLog","SetScriptResult","SetScriptOutput"};
 public static final String[] MAPPING={"ETLForge.GetSource"};
 public static String signature(String name){
  if(name.equals("ETLForge.ReadFile"))return "ETLForge.ReadFile(source[, fileName])";
  if(name.equals("ETLForge.WriteFile"))return "ETLForge.WriteFile(target, content[, fileName])";
  if(name.equals("ETLForge.DbExecute"))return "ETLForge.DbExecute(database, sql, ...)";
  if(name.equals("ETLForge.DbLookup"))return "ETLForge.DbLookup(database, sql)";
  if(name.equals("ETLForge.GetVar"))return "ETLForge.GetVar(name)";
  if(name.equals("ETLForge.SetVar"))return "ETLForge.SetVar(name, value)";
  if(name.equals("WriteToOperationLog"))return "WriteToOperationLog(message)";
  if(name.equals("SetScriptResult"))return "SetScriptResult(value)";
  if(name.equals("SetScriptOutput"))return "SetScriptOutput(value)";
  if(name.equals("ETLForge.GetSource"))return "ETLForge.GetSource(literalSourcePath)";
  return name+"(...)";
 }
}
