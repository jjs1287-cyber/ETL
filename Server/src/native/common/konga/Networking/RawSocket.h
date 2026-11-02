/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef RAW_SOCKET_T
#define RAW_SOCKET_T

#ifdef WIN32
	#include <winsock.h>
#endif

#include <boost/noncopyable.hpp>
#include <atomic>

namespace konga {
namespace networking {

#ifdef WIN32
	typedef SOCKET KongaSocketT;
#else
	typedef int KongaSocketT;
#endif

class RawSocket : private boost::noncopyable {
public:
	RawSocket(KongaSocketT s = -1) : m_s(s) {}
	KongaSocketT operator()() const { return m_s.load(); }
	KongaSocketT GetSocket() const { return m_s.load(); }
	void SetSocket(KongaSocketT s) { m_s.store(s); }
	KongaSocketT TakeSocket() { return m_s.exchange(static_cast<KongaSocketT>(-1)); }

private:
	std::atomic<KongaSocketT> m_s;
};

}                        
}                   

#endif                
