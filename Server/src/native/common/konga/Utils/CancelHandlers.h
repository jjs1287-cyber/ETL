/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef UTILS_CANCEL_HANDLERS_H
#define UTILS_CANCEL_HANDLERS_H

#include <boost/thread/mutex.hpp>

#include <map>

#include "CancelHandler.h"

namespace konga {

	class CancelHandlerContainer {
	public:
		CancelHandlerContainer(void) {}
		virtual ~CancelHandlerContainer(void) {}

		virtual void addCancelHandler(CancelHandler* cancelHandler) = 0;

		virtual void removeCancelHandler(CancelHandler* cancelHandler) = 0;
	};

	class CancelHandlers : public CancelHandlerContainer {
	public:
		CancelHandlers(void) {}
		~CancelHandlers(void) {}

	public:
		void addCancelHandler(CancelHandler* cancelHandler);

		void removeCancelHandler(CancelHandler* cancelHandler);

		void cancelAll();

	private:
		boost::mutex m_cancel_mutex;

		typedef std::map<CancelHandler*, int> chT;
		chT m_cancelHandlers;
	};

	class AutoRemoveCancelHandler {
	public:
		AutoRemoveCancelHandler(CancelHandlerContainer* cancelHandlers, CancelHandler* cancelHandler);
		virtual ~AutoRemoveCancelHandler(void);

	private:
		CancelHandlerContainer* m_handlers;
		CancelHandler* m_handler;
	};
}

#endif                           

