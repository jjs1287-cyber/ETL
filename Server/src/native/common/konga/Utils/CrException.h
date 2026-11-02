/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

                                

#ifndef KONGA_UTILS_CREXCEPTION_H
#define KONGA_UTILS_CREXCEPTION_H

#include <string>

#include <konga/Utils/OdbcConnectionStringUtils.h>	                  
#include <konga/Utils/StringUtils.h>				           

#define CR_E_FAIL 0x80004005L

namespace konga {

	class CrException {
	public:
		CrException(long errNum=CR_E_FAIL, const std::string& errStr="") : m_errNum(errNum), m_errStr(errStr) {}
		CrException(const std::string& errStr, long errNum = CR_E_FAIL) : m_errNum(errNum), m_errStr(errStr) {} 
		virtual ~CrException() {}

		virtual std::string GetErrorString() const {
			if ( konga::HasPwd(m_errStr) )
				return konga::ReplacePassword(m_errStr, "<REMOVED>");
			else
				return m_errStr;
		}
		std::string GetErrorNumberAsString() const {
			return konga::toString(m_errNum);
		}

		long GetErrorNumber() const { return m_errNum; }

		void SetErrorString(const std::string& errStr) { m_errStr=errStr; }
		void SetErrorNumber(long errNum) { m_errNum=errNum; }

		void PrependErrorString(const std::string& stMsg) { m_errStr = stMsg + m_errStr; }
		void AppendErrorString(const std::string& stMsg) { m_errStr += stMsg; }

	private:
		long m_errNum;
		std::string m_errStr;
	};

	class CrFatalException : public CrException {
	public:
		CrFatalException(long errNum=CR_E_FAIL, std::string errStr="") : CrException(errNum, errStr) {}
		virtual ~CrFatalException() {}
	};

	class NotImplementedYet : public CrException {
	public:
		NotImplementedYet(const std::string& errStr, long errNum = CR_E_FAIL) : CrException(errNum, errStr) {}
		virtual ~NotImplementedYet() {}
	};

	                                                                        
	                                                                     
	                              
	class InternalErrorException : public CrException {
	public:
		InternalErrorException(const std::string& errStr) : CrException(errStr) {}
		virtual ~InternalErrorException(void) {}

	public:
		                                                                                    
		virtual std::string GetErrorString() const {
			std::string msg(CrException::GetErrorString());
			msg = "Internal Error: " + msg;
			msg += "\nPlease post this problem on the ETLForge forum: http://www.etlforge.invalid/forums";
			msg += "\nor email support@etlforge.invalid.";
			return msg;
		}
	};
}

#endif                             
