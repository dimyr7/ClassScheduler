#ifndef SGLECLBD_H
#define SGLECLBD_H
#include "SectionGroup.hpp"
#include <vector>
class SGLecLBD : public Course::SectionGroup{
	public:
		/*
		 * Object creation
		 */
		SGLecLBD(std::string id);
		SGLecLBD(const SGLecLBD& copy);
		SGLecLBD& operator=(const SGLecLBD& copy);
		~SGLecLBD();
		/*
		 * Getters
		 */
		std::vector<Section*>	getLectures() const;
		std::vector<Section*>	getLabDis() const;
		
		bool addSection(Section* section);

		/*
		 * Generates all possible combinations of sections
		 */
		std::vector<SectionCombo*> getCombos() const;

		/*
		 * returns true if this section belongs in this section group based on ID
		 */
		bool validID(const Section* section) const;

	private:
		
		/*
		 * add new section to a section group
		 */
				
		std::string 			_id;
		std::vector<Section*> 	_lectures;
		std::vector<Section*>	_labDiscussions;
		friend std::ostream& operator<<(std::ostream& os, const SGLecLBD& group);

};
#endif
