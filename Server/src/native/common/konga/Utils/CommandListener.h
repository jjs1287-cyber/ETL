/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef COMMANDLISTENER_H
#define COMMANDLISTENER_H

#include <string>
#include <boost/function/function1.hpp>

namespace konga {

namespace networking {
	class Command;
	class Result;
}

  
                                                                  
                                                                       
                                                                           
                               
  
class CommandListener {
public:
	typedef boost::function1<networking::Result*, const networking::Command&> CommandFunctionT;
public:
	CommandListener(const std::string& sModuleName) : m_sModuleName(sModuleName) {}
	virtual ~CommandListener() {}

	virtual void AddCommandFunction(const std::string& sCommand, const CommandFunctionT f) = 0;
	virtual void StartListening() = 0;
	virtual void StopListening() = 0;

	virtual std::string GetModuleName() const { return m_sModuleName; }

private:
	std::string m_sModuleName;
};

}                   

#endif                     
