/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "CancelHandlers.h"

using namespace konga;
using std::map;

void CancelHandlers::addCancelHandler(CancelHandler* cancelHandler) {
	if ( cancelHandler == 0 ) return;

	boost::mutex::scoped_lock lock(m_cancel_mutex);

	chT::const_iterator cit = m_cancelHandlers.find(cancelHandler);
	if ( cit != m_cancelHandlers.end() ) return;

	m_cancelHandlers.insert( chT::value_type(cancelHandler, 0) );
}

void CancelHandlers::removeCancelHandler(CancelHandler* cancelHandler) {
	if ( cancelHandler == 0 ) return;

	boost::mutex::scoped_lock lock(m_cancel_mutex);

	chT::const_iterator cit = m_cancelHandlers.find(cancelHandler);
	if ( cit == m_cancelHandlers.end() ) return;

	m_cancelHandlers.erase( cancelHandler );
}

void CancelHandlers::cancelAll() {
	boost::mutex::scoped_lock lock(m_cancel_mutex);

	chT::iterator it = m_cancelHandlers.begin();
	for( ; it != m_cancelHandlers.end(); ++it ) {
		CancelHandler* handler = it->first;
		if ( handler->cancel() ) {
			m_cancelHandlers.erase(it++);
		}
		else {
			it->second++;
		}
	}
}

AutoRemoveCancelHandler::AutoRemoveCancelHandler(CancelHandlerContainer* cancelHandlers, CancelHandler* cancelHandler) : m_handlers(0), m_handler(0) {
	if ( cancelHandlers == 0 || cancelHandler == 0 ) return;

	m_handlers = cancelHandlers;
	m_handler = cancelHandler;
	m_handlers->addCancelHandler(m_handler);
}

AutoRemoveCancelHandler::~AutoRemoveCancelHandler(void) {
	if ( m_handlers != 0 && m_handler != 0 ) {
		m_handlers->removeCancelHandler(m_handler);
	}
}

