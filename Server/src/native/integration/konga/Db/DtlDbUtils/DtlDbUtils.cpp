/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include "DtlDbUtils.h"

using namespace std;

void konga_dtl::ThrowDbException(const dtl::DBException& ex, const string& stErr           , long lErr           )
{
    string stSqlState, stSqlErrMsg;
    long lNativeErr = 0L;

    string stErr2(stErr);

	vector<dtl::DBException::ODBCErrorInfo> vInfo = ex.GetAllODBCErrors();
                                                                                  
    if( vInfo.empty() )
    {
        stSqlState = "*NOT DEFINED*";
        stSqlErrMsg = "*NOT DEFINED*";
    }
    else
    {
                                                               
        stSqlState = vInfo.begin()->szSQLState;
        stSqlErrMsg = vInfo.begin()->errMsg;
        lNativeErr = vInfo.begin()->nativeErr;

		stErr2 += stSqlErrMsg + " \n\nDetails: \n";
    }

    stErr2 += ex.what();
    throw konga::DbException(stErr2, stSqlState, stSqlErrMsg, lNativeErr, lErr);
}

void konga_dtl::ThrowDbException(const dtl::RootException& ex, const string& stErr           , long lErr           )
{
    string stErr2(stErr);
    stErr2 += ex.what();
    throw konga::DbException(stErr2, lErr);
}

void konga_dtl::ThrowDbException(const std::exception& ex, const std::string& stErr, long lErr) {
    string stErr2(stErr);
    stErr2 += ex.what();
    throw konga::DbException(stErr2, lErr);
}
