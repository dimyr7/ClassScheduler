#ifndef COURSE_H
#define COURSE_H
#include <string>
#include <vector>

#include "Section.hpp"
#include "SectionCombo.hpp"
//class Section;
//class SectionCombo;
class Course{
	public:
		/*
		 * Makes a new course object given a department and course number
		 */
		Course(std::string department, std::string courseNumber);

		/*
		 * Destructor for course object
		 * deletes all sections, sectiongroups, and sectioncombos
		 */
		~Course();
		
		/*
		 * Getters
		 */
		std::string 					getDepartment() const;
		std::string 					getCourseNumber() const;
		std::vector<Section *> 			getSections() const;
		std::vector<SectionCombo *>  	getCombos() ;
	

		/*
		 * With the current sections populated, it will generate valid combinations of sections
		 */
		bool	generateCombo(); 

		/*
		 * Setters
		 */
		void 	setDepartment(std::string department);
		void  	setCourseNumber(std::string number);

		/*
		 * Add a new section to a course
		 */
		void 	addSection(Section* section);

		/*
		 * TODO
		 */
		bool 	isSyncd() const;
		class SectionGroup;


	private:

		/*
		 * Sections are organized into sections groups
		 */
		

		std::string 					_department;
		std::string 					_courseNumber;
		std::vector<Section*> 			_sections;
		std::vector<SectionCombo*> 		_combos;
		std::vector<SectionGroup*>		_groups;
		bool					_syncd;

};
#endif
