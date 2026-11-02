/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef DBEXCEPTION_H
#define DBEXCEPTION_H

#include <cstring>						         

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

namespace konga
{

class DbException : public CrException {
public:

	DbException(const std::string& stErr = "", long lErrNum = -1) : 
        CrException(stErr, lErrNum), m_lNativeErr(0)
    {} 

    DbException(const std::string& stErr, const std::string& stSqlState, const std::string& stSqlErrMsg, long lNativeErr, long lErrNum = -1)
        : CrException(stErr, lErrNum), m_lNativeErr(lNativeErr), m_stSqlState(stSqlState), m_stSqlErrMsg(stSqlErrMsg)
    {}

	virtual ~DbException() {};

	std::string GetStateNativeOrigin() const {
		return "State: " + m_stSqlState + ", Native error: " + toString(m_lNativeErr);
	}

    std::string GetSqlState() const {
        return m_stSqlState;
    }

    long GetNativeError() const {
        return m_lNativeErr;
    }

    std::string GetSqlErrorMessage() const {
        return m_stSqlErrMsg;
    }

	                                                                 
	std::string GetPrettyErrorMessage() const {
		const std::string& stErr0 = GetErrorString();
		std::string stErr(stErr0);
		if ( !m_stSqlState.empty() && strstr(stErr0.c_str(), m_stSqlState.c_str())==0 )
			stErr += "\nSql State: " + m_stSqlState;
		if ( !m_stSqlErrMsg.empty() && strstr(stErr0.c_str(), m_stSqlErrMsg.c_str())==0 )
			stErr += "\nSql Error: " + m_stSqlErrMsg;
		if ( m_lNativeErr != 0 )
			stErr += "\nNative Error Number: " + toString(m_lNativeErr);
		return stErr;
	}

private:
    long        m_lNativeErr;
    std::string m_stSqlState;
    std::string m_stSqlErrMsg;
};

}                       

#endif                 
