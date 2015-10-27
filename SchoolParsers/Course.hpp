#ifndef COURSE_H
#define COURSE_H
#include <string>
#include <vector>
class Section;
class SectionCombo;
class Course{
	public:
		Course();
		Course(std::string department, std::string courseNumber);
		~Course();

		std::string 					getDepartment();
		std::string 					getCourseNumber();
		std::vector<Section *> 			getSections();
		std::vector<SectionCombo *>  	getCombos();

		bool	generateCombo(); 
		bool 	setDepartment(std::string department);
		bool 	setCourseNumber(std::string number);
		void 	addSection(Section* section);
		bool 	isSyncd();
	private:
		std::string 					_department;
		std::string 					_courseNumber;
		std::vector<Section *> 			_sections;
		std::vector<SectionCombo *> 	_combos;
		bool					_syncd;

};
#endif
