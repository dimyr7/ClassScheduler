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
		/**
		 * A Getter for the SectionName
		 * @return the Section Name, e.g. AL1
		 */
		std::string 		getSectionName() const;

		/**
		 * A getter for the section type
		 * @return the section type, e.g. DIS
		 */
		std::string 		getSectionType() const;

		/**
		 * A getter for the description
		 * @return the description
		 */
		std::string 		getDescription() const;

		/**
		 * A Getter for the CRN
		 * @return the CRN, e.g. 12345
		 */
		std::string 		getCRN() const;

		/**
		 * A getter for the instructors teaching this course
		 * @return a vector of Instructor pointers
		 */
        std::vector<Instructor*> 		getInstructors() const;

		/**
		 * A getter for the week schedule for this section
		 * @return a week pointer
		 */
		Week* 			                getWeek() const;

		/**
		 * A getter for the semeseter for this section
		 * @return a semester pointer
		 */
		Semester*		                getSemester() const;

		/**
		 * A getter for the location object
		 * @return a location pointer
		 */
		Location* 		                getBuilding() const;
		
		/**
		 * Constructor
		 */
		Section();

		/**
		 * Destructor
		 */
		~Section();

		/**
		 * Copy Constructor
		 * @param copy is the section to copy
		 */
		Section(const Section& copy);

		/**
		 * Copy Assignment Operator
		 * @papam copy is the Section to copy
		 * @return this section with the copied data
		 */
		Section& operator=(const Section& copy);

		/**
		 * Checks if to sections over lap at any of the meeting times
		 * @param a is one of the sections
		 * @param b is the other section
		 * @return true if the two sections overlap, false otherwise
		 */
		static bool overlap(Section* a, Section* b);
	private:
		
		
		/**
		 * Sets the section name
		 * @param sectionName is the name of this section
		 */
		void setSectionName(std::string sectionName);

		/**
		 * Sets the section type
		 * @param SectionType is the section type
		 */
		void setSectionType(std::string sectionType);

		/**
		 * Sets the description of the section
		 * @param description is the description of hte section
		 */
		void setDescription(std::string description);

		/**
		 * Sets the CRN of the section
		 * @param crn is the CRN of the section
		 */
		void setCRN(std::string crn);


		/**
		 * Sets the instrucotr object of the section
		 * @param instructor is the instructor of the section
		 */
		void addInstructor(Instructor* instructor);

		/**
		 * Sets the week of hte section
		 * @param daysOfWeek is the week object of this section
		 */
		void setDaysOfWeek(Week* daysOfWeek);

		/**
		 * Sets the semester of the section
		 * @param semester is the semester of the section
		 */
		void setSemester(Semester* semester);

		/**
		 * Sets the location of the section
		 * @param building is the location of the building of the section
		 */
		void setLocation(Location* building);

		/**
		 * Allows a section to be printed to stream
		 * @param os is the stream to write to
		 * @param section is the section to be written to stream
		 * @return is the stream with the written infomation
		 */
		friend std::ostream& operator<<(std::ostream& os, const Section& section);

		/**
		 * String representation of the sectionName
		 */
		std::string 	_sectionName;		

		/**
		 * String representation of the section type
		 */
		std::string 	_sectionType;	

		/**
		 * The description of the section
		 */
		std::string 	_description;
		
		/**
		 * String representation of the crn
		 */
		std::string 	_crn;

		/**
		 * All the instrucotrs of this section
		 */
        std::vector<Instructor*> 	_instructors; 

		/**
		 * The week associated with this section
		 */
		Week* 		                _daysOfWeek;

		/**
		 * The semester of this section
		 */
		Semester* 	                _dates;
		
		/**
		 * The location of this section
		 */
		Location* 	                _bulding;

};
#endif
