/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef AUTO_CLEANUP_NETWORKING_H
#define AUTO_CLEANUP_NETWORKING_H

namespace konga {
namespace networking {

class auto_free_addrinfo {
public:
	auto_free_addrinfo(struct addrinfo* pAddrInfo) : m_p(pAddrInfo) {}
	~auto_free_addrinfo(void);

private:
	struct addrinfo* m_p;
};

}                        
}                   


#endif                             
