#ifndef COURSE_H
#define COURSE_H

#include "Section.hpp"
#include "SectionCombo.hpp"
#include <string>
#include <vector>
/**
 *  Represenets a Course oject such that each course belongs to a department and has a course number
 *  Each course also has many sections that are partitioned into section groups and a set of sections that are valid
 */
class Course{
	public:
		/**
		 * Creates a new Course object with no sections, section groups, or combinations
		 * @param department is a string that is the department code e.g 'CS' and not 'Computer Science'
		 * @param courseNumber is a string that represents a course nunmber e.g. '125'
		 */
		Course(std::string department, std::string courseNumber);

		/**
		 * Destructor for course object
		 * Deletes all sections, sectiongroups, and sectioncombos
		 */
		~Course();
		
		/**
		 * A getter function that returns a string representing the department code e.g. 'CS'
		 * @return the department code
		 */
		std::string	getDepartment() const;

		/**
		 * A getter function that returns the string representing the course number e.g. '125'
		 * @return the course number
		 */
		std::string getCourseNumber() const;

		/**
		 * A getter function that returns a vector of section pointers 
		 * @return a vector of Section pointers
		 */
		std::vector<Section*> getSections() const;

		/**
		 * Takes all the sections that are belong to this course and generates all possible section combinations.
		 * The section combinations are returned in a vector of SectionCombo pointers
		 * @return a vector of SectionCombo pointers that correspond to valid combinations
		 */
		std::vector<SectionCombo*> getCombos() ;

		/**
		 * Adds a new sections that belongs to this course
		 * @param section is a pointer to a section that belongs to this course
		 */
		void addSection(Section* section);

			
		
		/**
		 * Number of lecture types
		 */
		static const int NUM_OF_SECTION_TYPES = 11;

	private:
		/**
		 * A SectionGroup is a way to categorize sections. e.g. Lecture & Lab-Discussion
		 * One Section from each group will be a valid combination.
		 */
		class SectionGroup;

		/**
		 * All lecture types enumerated
		 */
		enum TypeOfSection{
			CNF,
			DIS,
			IND,
			LAB,
			LBD,
			LEC,
			LCD,
			ONL,
			PR,
			Q,
			STA
		};

				
		/**
		 * A string that holds the department code
		 * e.g. CS
		 */
		std::string	_department;
		
		/**
		 * A string that holds the course number
		 * e.g. 125
		 */
		std::string	_courseNumber;

		/**
		 * A vector of section groups that belong to this course
		 */
		std::vector<Section*> _sections;

		/**
		 * A vector of valid section combinations
		 * Only populated when getCombos() is called
		 */
		std::vector<SectionCombo*> _combos;

		
		/**
		 * Returns the type of the section
		 * @param section determines the type it is
		 * @return a value of the enum TypeOfSection
		 */
		static TypeOfSection getTypeOfSection(const Section* section);

		/**
		 *
		 */
		static size_t getNumSectionTypes(const std::vector<Section*> sections);
	};
#endif
