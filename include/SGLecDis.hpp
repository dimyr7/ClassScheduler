#ifndef SGLECDIS_H
#define SGLECDIS_H
#include "SectionGroup.hpp"
#include <vector>
class SGLecDis : public Course::SectionGroup{
	public:
		/**
		 * Constructor
		 * @param id is the id of the section group
		 */
		SGLecDis(std::string id);

		/**
		 * Copy constructor
		 * @param copy is the sectiongroup to copy
		 */
		SGLecDis(const SGLecDis& copy);


		/**
		 * Assignment operator
		 * @param copy is the SectionGroup to copy
		 * @return this with the copied attritbues
		 */
		SGLecDis& operator=(const SGLecDis& copy);


		/**
		 * Getter for the lecture sections that belong to this group
		 * @return a vector of sections that are lectures in this group
		 */
		std::vector<Section*>	getLecSections() const;

		/**
		 * Getter for the discussion sections that belong to this group
		 * @return a vector of sections that are discussions in this group
		 */
		std::vector<Section*>	getDisSections() const;
	
		/**
		 * Adds a new section to this section group
		 * @param sections is the new section to add
		 * @return true if added successfully, false otherwise
		 */
		bool addSection(Section* section);

		/**
		 * Generates all the valid combination of section
		 * @return a vector of valid sectioncombos
		 */
		std::vector<SectionCombo*> getCombos() const;


	private:
		
		/**
		 * The id used to represent this section group
		 */
		std::string 			_id;

		/**
		 * A vector of sections that are lectures
		 */
		std::vector<Section*> 	_lectures;

		/**
		 * A vector of lab-discussions
		 */
		std::vector<Section*>	_discussions;


		/**
		 * Prints a section group to stream
		 * @param os the stream to write to
		 * @param group is the section group to write
		 * @retunr the stream os with things written to it
		 */
		friend std::ostream& operator<<(std::ostream& os, const SGLecDis& group);

};
#endif
