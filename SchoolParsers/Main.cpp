#include <fstream>
#include <iostream>
#include "Course.hpp"
#include "SectionBuilder.hpp"

using std::iostream;
using std::ifstream;
using namespace std;
int main(){
	
	
	Section::SectionBuilder b;
	
	b.setSectionName("AL1");
	b.setSectionType("Lecture-Discussion");
	b.setDescription("Lecture for a class");
	b.setCRN("11111");

	b.setInstructorName("Alan Turing");

	b.setStartTime(Section::Week::Day::monday, 6, 0);
	b.setStartTime(Section::Week::Day::tuesday, 7, 0);
	
	b.setEndTime(Section::Week::Day::monday, 8, 0);
	b.setEndTime(Section::Week::Day::tuesday, 9, 0);

	b.setSemesterStart(1, 1, 2015);
	b.setSemesterEnd(30, 12, 2015);

	b.setSemsterYear("2015");
	b.setSemesterSeason("All Year");
	b.setSemesterName("365 All year");

	b.setLocationLat(2.4);
	b.setLocationLon(-23.6);
	b.setLocationBuilding("Siebel");
	b.setLocationRoom("1204");


	Section* section = b.buildSection();
	std::cout << (*section) << std::endl;

	return 0;
}
