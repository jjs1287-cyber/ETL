package org.etlforge.db.tableinfo;

public interface TableInfoProvider {
   TableColumns[] getTableInfo(String var1, String var2, String var3, String[] var4) throws TableInfoProviderException;
}
