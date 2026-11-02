/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                       
  
                                                                      

#if !defined(AFX_CURRENTTIME_H__C365BDE5_AC82_4512_8B5F_1031526056C5__INCLUDED_)
#define AFX_CURRENTTIME_H__C365BDE5_AC82_4512_8B5F_1031526056C5__INCLUDED_

#include <sys/timeb.h>
#include <time.h>
#include <stdio.h>
#include <string.h>

class CCurrentTime
{
public:
	CCurrentTime() {}
	virtual ~CCurrentTime() {}

	const char* Now()
	{
		time_t now;
		time(&now);
		char* s = ctime(&now);
		if( s )
		{
			strncpy(m_sTime,s,255);
			size_t len = strlen(s);
			if( m_sTime[len-1] == '\n' )
				m_sTime[len-1] = 0;
		}
		return m_sTime;
	}

	const char* NowWithMillies()
	{
		struct timeb timebuffer;
		ftime( &timebuffer );
		char *timeline = ctime( & ( timebuffer.time ) );

		sprintf(m_sTime, "%.19s.%.3hu %.4s", timeline, timebuffer.millitm, &timeline[20] );
		return m_sTime;
	}

private:
	char m_sTime[256];
};

#endif                                                                                
