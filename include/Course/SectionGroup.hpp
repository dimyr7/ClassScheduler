#ifndef SECTION_GROUP_H
#define SECTION_GROUP_H
#include "Course.hpp"
#include <vector>
#include <valarray>
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
		SectionGroup(size_t numTypes, std::string id);

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
		bool 	addSection(Section* section);

		/**
		 * All SectionGroups must know how to generate valid section combinations
		 * @return a vector of valid SectionCombos that are generated from these sections in this group
		 */
		std::vector<SectionCombo*> getCombos();

	private:
		/**
		 * Given a set of indecies assiciated with choosing different type of sections, this will modify them to get the next combinations
		 * @param index is a set of indecies that will be modified to the next possible valid combinations
		 * @return true if the operation succesfully moved forward, false if the indecies were reset to the initial value of all 0s
		 */
		bool nextIteration(std::valarray<size_t> &index);		

		/**
		 * Checks if a vector of sections overlap
		 * @param potCombo is the potential combination of sections
		 * @return true if any of the 2 sections overlap, false otherwise
		 */
		static bool overlap(std::vector<Section*> potCombo);

		/**
		 * The id of the group
		 */
		std::string 	_id;

		/**
		 * number of section types in this combo
		 */
		size_t 			_numTypes;

		/**
		 * Sections that belong to this group catagorized by section type
		 */
		std::valarray< std::vector<Section*> >    _sections;	

		/**
		 * The different section types
		 */
		std::valarray<std::string> _sectionTypes;
};
#endif
