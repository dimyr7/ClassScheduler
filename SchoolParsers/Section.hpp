#ifndef SECTION_H 
#define SECTION_H
#include <string>

class Section{

	public:
		class Instructor;
		class Semester;
		class Location;
		class Week;
		class SectionBuilder;

		/*
		 * Getters
		 */
		std::string 		getSectionName() const;
		std::string 		getSectionType() const;
		std::string 		getDescription() const;

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
		void setSectionName(std::string sectionName);
		void setSectionType(std::string sectionType);
		void setDescription(std::string description);

		void setInstructor(Instructor* instructor);
		void setDaysOfWeek(Week* daysOfWeek);
		void setSemester(Semester* semester);
		void setLocation(Location* building);

		/*
		 * Data Members
		 */

		std::string 	_sectionName;		
		std::string 	_sectionType;	
		std::string 	_description;

		Instructor*	_instructor; 
		Week* 		_daysOfWeek;
		Semester* 	_dates;
		Location* 	_bulding;

};
#endif
