#ifndef SECTION_GROUP_H
#define SECTION_GROUP_H
#include "Course.hpp"
#include <vector>
/**
 * This an abstract class made to represent a group of sections.
 * These sections will need to have the id in common. Most commonly, it is the first letter of the section Name
 * This can generate valid section combinations from the sections in this group
 */
class Course::SectionGroup{	
	public:
		/**
		 * Constructor
		 * @param id is the id of this section group, most commony the first letter of the name
		 */
		SectionGroup(std::string id);

		/**
		 * Copy constructor
		 * @param copy the SectionGroup to copy
		 */
		SectionGroup(const SectionGroup& copy);

		/**
		 * Assignment operator
		 * @param copy the SectionGroup to copy
		 * @return this section group with copied attributes
		 */
		SectionGroup& operator=(const SectionGroup& copy);

		/**
		 * Destructor
		 * Doesnt delete the sections that are part of this section group
		 */
		~SectionGroup();


		/**
		 * Checks if the id provided is the same as the id of this sectiongroup
		 * @param id is the id to check
		 * @return true if the id is valid, false otherwise
		 */
		bool validID(const std::string id);

		/**
		 * All Section groups must implement a way to add a section to a section group
		 * @param section is the section to be added
		 * @return true if section was added successfully, false otherwise
		 */
		virtual bool 	addSection(Section* section) = 0;

		/**
		 * All SectionGroups must know how to generate valid section combinations
		 * @return a vector of valid Section Groups that are generated from these sections
		 */
		virtual std::vector<SectionCombo*> getCombos() const = 0;

	private:
		/**
		 * The id of the group
		 */
		std::string 	_id;
};

#endif
