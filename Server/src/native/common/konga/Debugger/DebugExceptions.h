/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef KONGA_DEBUGEXCEPTIONS_H
#define KONGA_DEBUGEXCEPTIONS_H

namespace konga {

	class DebugException {
	};

	class DebugSessionExpired : public DebugException {
	};

	class CancelDebugSession : public DebugException {
	};
}

#endif                           

