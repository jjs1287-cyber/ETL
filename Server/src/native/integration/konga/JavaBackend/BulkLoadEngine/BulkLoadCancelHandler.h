/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_BULK_LOAD_ENGINE_CANCEL_HANDLER_H
#define KONGA_BULK_LOAD_ENGINE_CANCEL_HANDLER_H

#include <konga/Utils/CancelHandler.h>
#include <konga/Utils/CrException.h>

#include "BulkLoadEngineWrapper.h"

namespace konga {

	class BulkLoadCancelHandler : public CancelHandler {
	public:
		BulkLoadCancelHandler(BulkLoadEngineWrapper& wrapper) : m_wrapper(wrapper) {}
		~BulkLoadCancelHandler(void) {};

	bool BulkLoadCancelHandler::cancel() {
		try {
			m_wrapper.closeSession();
			return true;
		}
		catch( const CrException& ) {
			return false;
		}
	}


	private:
		BulkLoadEngineWrapper& m_wrapper;
	};

}

#endif                                           


