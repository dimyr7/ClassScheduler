#ifndef SECTION_H 
#define SECTION_H
#include <string>

#include "Week.hpp"
#include "Semester.hpp"
#include "Instructor.hpp"
#include "Location.hpp"
using std::string;
class Section{

	public:
		Section();

		void setSectionName(string sectionName);
		void setSectionType(string sectionType);
		void setDescription(string description);

		void setInstructor(Instructor* instructor);
		void setDaysOfWeek(Week* daysOfWeek);
		void setSemester(Semester* semester);
		void setLocation(Location* building);

		string 		getSectionName();
		string 		getSectionType();
		string 		getDescription();

		Instructor*	getInstructor();
		Week* 		getWeek();
		Semester*	getSemester();
		Location* 	getBuilding();
	private:
		
		string 	_sectionName;		// AL1
		string 	_sectionType;		// Lecture
		string 	_description;		// 

		Instructor*	_instructor; 		// Angrave
		Week* 		_daysOfWeek;
		Semester* 	_dates;
		Location* 	_bulding;

};
#endif
