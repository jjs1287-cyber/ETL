/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_ENGINE_EXCEPTION
#define KONGA_OPERATION_ENGINE_EXCEPTION

#include <konga/Utils/CrException.h>

namespace konga {

	class OperationEngineException : public CrException {
	public:
		OperationEngineException(const std::string& stError, long lOperationId = 0, const std::string& stWhere = "", long lError = -1) :
			CrException(stError, lError), m_bFatal(true), m_lOperationId(lOperationId), m_stWhere(stWhere) { }
		
		OperationEngineException(const std::string& stError, bool bIsFatal, const std::string& stWhere = "", long lOperationId = 0, long lError = -1) :
			CrException(stError, lError), m_bFatal(bIsFatal), m_lOperationId(lOperationId), m_stWhere(stWhere) { }
		
		OperationEngineException(const std::string& stError, const std::string& stWhere, long lOperationId = 0, long lError = -1) :
			CrException(stError, lError), m_bFatal(true), m_lOperationId(lOperationId), m_stWhere(stWhere) { }
		
		OperationEngineException(const std::string& stError, const char* sWhere, long lOperationId = 0, long lError = -1) :
			CrException(stError, lError), m_bFatal(true), m_lOperationId(lOperationId), m_stWhere(sWhere) { }
		
		~OperationEngineException(void) { }

		long getOperationId() const {
			return m_lOperationId;
		}

		void setOperationId(long opId) {
			m_lOperationId = opId;
		}

		bool isFatal() const {
			return m_bFatal;
		}

		std::string getWhere() const {
			return m_stWhere;
		}

	private:
		bool m_bFatal;
		long m_lOperationId;
		std::string m_stWhere;	                                   
	};
}

#endif                                    

