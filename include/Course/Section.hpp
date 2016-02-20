#ifndef SECTION_H 
#define SECTION_H

#include "Week.hpp"
#include "Location.hpp"
#include "Instructor.hpp"
#include "Semester.hpp"

#include <string>
#include <vector>
/**
 * This represents a section that belongs to a class
 */
class Section{
	public:
		class SectionBuilder;
		/*
		 * Getters
		 */
		std::string 		getSectionName() const;
		std::string 		getSectionType() const;
		std::string 		getDescription() const;
		std::string 		getCRN() const;


        std::vector<Instructor*> 		getInstructors() const;
		Week* 			                getWeek() const;
		Semester*		                getSemester() const;
		Location* 		                getBuilding() const;
		
		/*
		 * Constructor
		 */
		Section();

		/*
		 * Destructor
		 */
		~Section();

		/*
		 * Copy Constructor
		 */
		Section(const Section& copy);

		/*
		 * Copy Assignment Operator
		 */
		Section& operator=(const Section& copy);

		/*
		 * Getters
		 */
		static bool overlap(Section* a, Section* b);
	private:
		
		
		/*
		 * Setters
		 */
		void setSectionName(std::string sectionName);
		void setSectionType(std::string sectionType);
		void setDescription(std::string description);
		void setCRN(std::string crn);

		void addInstructor(Instructor* instructor);
		void setDaysOfWeek(Week* daysOfWeek);
		void setSemester(Semester* semester);
		void setLocation(Location* building);

		friend std::ostream& operator<<(std::ostream& os, const Section& section);
		/*
		 * Data Members
		 */

		std::string 	_sectionName;		
		std::string 	_sectionType;	
		std::string 	_description;
		std::string 	_crn;

        std::vector<Instructor*> 	_instructors; 
		Week* 		                _daysOfWeek;
		Semester* 	                _dates;
		Location* 	                _bulding;

};
#endif
