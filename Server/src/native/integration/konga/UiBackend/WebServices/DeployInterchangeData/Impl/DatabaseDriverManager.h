/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_WS_DEPLOY_DATABASE_DRIVER_MANAGER_H
#define ETLFORGE_WS_DEPLOY_DATABASE_DRIVER_MANAGER_H

#include <string>
#include <vector>

namespace konga {
namespace interchange_deploy {

	class DatabaseDriverManager {
	public:
		DatabaseDriverManager(void);
		~DatabaseDriverManager(void);

		bool ExistsDriver(const std::string& stDriverName) const;

	private:
		void GetDrivers() const;

		mutable bool m_bWasInitialized;
		mutable std::vector<std::string> m_vExistingDrivers;
	};
}
}

#endif                                                

