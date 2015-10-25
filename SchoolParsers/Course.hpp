#ifndef COURSE_H
#define COURSE_H

#include "section.hpp"
#include <string>
#include <vector>
using namespace std;
class Course{
	
	public:
		Course();
		Course(string department, string courseNumber);
		~Course();

		string 					getDepartment();
		string 					getCourseNumber();
		vector<Section *> 		getSections();
		vector<SectionCombo *>  getCombos();

		bool	generateCombo(); 
		bool 	setDepartment(string department);
		bool 	setCourseNumber(string number);
		void 	addSection(Section* section);
		bool 	isSyncd();
	private:
		//static bool validateDepartment(string department);
		//static bool validateCourseNumber(string department);

		string 					_department;
		string 					_courseNumber;
		vector<Section *> 		_sections;
		vector<SectionCombo *> 	_combos;
		bool					_syncd;

};

#endif
