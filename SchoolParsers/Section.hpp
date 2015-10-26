#ifndef SECTION_H 
#define SECTION_H
#include <string>

#include "Week.hpp"
#include "Semester.hpp"
#include "Instructor.hpp"
#include "Location.hpp"
using std::string;
namespace CourseInfo{
	
	class Section{
		friend class SectionBuilder;
		public:
			/*
			 * Getters
			 */
			string 		getSectionName() const;
			string 		getSectionType() const;
			string 		getDescription() const;

			Instructor*	getInstructor() const;
			Week* 		getWeek() const;
			Semester*	getSemester() const;
			Location* 	getBuilding() const;

		private:
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
			 * Setters
			 */
			void setSectionName(string sectionName);
			void setSectionType(string sectionType);
			void setDescription(string description);

			void setInstructor(Instructor* instructor);
			void setDaysOfWeek(Week* daysOfWeek);
			void setSemester(Semester* semester);
			void setLocation(Location* building);

			/*
			 * Data Members
			 */

			string 	_sectionName;		
			string 	_sectionType;	
			string 	_description;

			Instructor*	_instructor; 
			Week* 		_daysOfWeek;
			Semester* 	_dates;
			Location* 	_bulding;

	};
}
#endif
