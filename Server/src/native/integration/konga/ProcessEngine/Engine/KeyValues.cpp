/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *

#include <string>

#ifdef WIN32
//	#include <windows.h>
#endif // WIN32

#include <konga/Db/DbUtils/DbUtils.h>		// GetDbConnection()
#include <konga/Db/DbUtils/SmartDbPtr.h>	// template class SmartDbPtr<>
#include <konga/Db/TranDb/Key_Values.h>

#include <konga/Utils/DBConnectionInfo.h>	// class DBConnectionInfo

extern DBConnectionInfo g_trandb_connect;

//
//	For getting values of the Key_Values table in TranDB.
//	Note: This function may throw DbException so the caller must be
//	prepared to handle that.
//
std::string GetKeyValueSetting(const std::string& stKey)
{
	konga::SmartDbPtr<konga::DbConnection> pTranDB(konga::GetDbConnection(g_trandb_connect.connectionString()));
	konga::SmartDbPtr<TranDb::Key_Values> tab( TranDb::Key_Values::MakeInstance(pTranDB) );

	tab->Select("WHERE Key_Name='" + stKey + "'");

	if( tab->Next() ) {
		if( !tab->IsKey_ValueNull() )
			return tab->GetKey_Value();
	}

	return "";
}

 */
