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

		string getDepartment();
		string getCourseNumber();
		vector<Section *> getSections();

		bool setDepartment(string department);
		bool setCourseNumber(string number);
		void addSection(Section* section);

		static bool validateDepartment(string department);
		static bool validateCourseNumber(string department);

	private:
		string _department;
		string _courseNumber;
		vector<Section *> _sections;


};

#endif
