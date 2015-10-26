#include <fstream>
#include <iostream>
#include "Week.hpp"

using std::iostream;
using std::ifstream;
using namespace CourseInfo;
using namespace std;
int main(int argc, char* argv[]){
	
	if(argc ==  2){
		cout<<"Using file "<<argv[1]<<endl;
	}
	
	Week* w = new Week();
	Time* t1 = new Time(4, 0);
	Time* t2 = new Time(6, 0);
	w->setDay(Week::Day::monday, t1, t2);
	t1->setHour(8);
	array<Time*, 2> times = w->getTimes(Week::Day::monday);	

	cout<<"First Time: "<< *times[0]<<endl;
	cout<<"Second Time: "<< *times[1]<<endl;
	return 0;
}
