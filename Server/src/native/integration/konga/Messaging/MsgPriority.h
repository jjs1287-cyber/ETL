/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef MSG_PRIORITY_H
#define MSG_PRIORITY_H

namespace konga {

	enum MSG_PRIORITY { MSG_PRIORITY_UNDEFINED = -1,
						MSG_PRIORITY_IDLE = 0,
						MSG_PRIORITY_LOWEST = 2,
						MSG_PRIORITY_BELOW_NORMAL = 4,
						MSG_PRIORITY_NORMAL = 5,
						MSG_PRIORITY_ABOVE_NORMAL = 6,
						MSG_PRIORITY_HIGHEST = 9
	};

}                   

#endif                  

