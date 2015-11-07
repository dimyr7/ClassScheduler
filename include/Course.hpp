#ifndef COURSE_H
#define COURSE_H
#include <string>
#include <vector>
class Section;
class SectionCombo;
class Course{
	public:
		Course(std::string department, std::string courseNumber);
		~Course();

		std::string 					getDepartment() const;
		std::string 					getCourseNumber() const;
		std::vector<Section *> 			getSections() const;
		std::vector<SectionCombo *>  	getCombos() const;

		bool	generateCombo(); 
		void 	setDepartment(std::string department);
		void  	setCourseNumber(std::string number);
		void 	addSection(Section* section);
		bool 	isSyncd() const;
	private:
		std::string 					_department;
		std::string 					_courseNumber;
		std::vector<Section*> 			_sections;
		std::vector<SectionCombo*> 		_combos;
		bool					_syncd;

};
#endif
