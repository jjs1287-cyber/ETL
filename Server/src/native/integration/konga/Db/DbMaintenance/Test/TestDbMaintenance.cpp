#include <iostream>
#include <string>

#include <konga/Utils/StatusReceiver.h>

#include <konga/Db/DbMaintenance/PgSqlMaintenance.h>
#include <konga/Utils/CrException.h>

using namespace std;
using namespace konga;

namespace {
	class SimpleStatusReceiver : public StatusReceiver {
	public:
		SimpleStatusReceiver(void) {}
		~SimpleStatusReceiver(void) {}

		void Status(const std::string& stStatus) {
			cout << stStatus << endl;
		}

		void Progress(float prog) {

		}

		void ReportError(const std::string& stError) {
			cerr << stError << endl;
		}
	};
}

int main(int argc, char* argv[])
{
	if ( argc < 2 ) {
		cout << "specify the password as the first argument" << endl;
		return 1;
	}

	string stConnectionString("Driver=PostgreSQL;Server=windoc;Database=PDb;" \
		"Uid=postgres;Pwd=" + string(argv[1]));

	string stErr;
	try {
		SimpleStatusReceiver caller;
		PgSqlMaintenance(stConnectionString, true, true, caller);
	}
	catch( const CrException& e ) {
		cerr << "Fatal error: " << endl;
		cerr << e.GetErrorString() << endl;
		return 2;
	}

		return 0;
}
