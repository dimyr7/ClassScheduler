#ifndef SGLECLBD_H
#define SGLECLBD_H
#include "Course.hpp"
#include "SectionGroup.hpp"
#include "SectionCombo.hpp"
class SGLecLBD : Course::SectionGroup{
	friend class Course;
	public:
		/*
		 * Getters
		 */
		std::vector<Section*>	getLectures() const;
		std::vector<Section*>	getLabDis() const;
	private:
		/*
		 * Object creation
		 */
		SGLecLBD(std::string id);
		SGLecLBD(const SGLecLBD& copy);
		SGLecLBD& operator=(const SGLecLBD& copy);

		/*
		 * add new section to a section group
		 */
		bool addSection(Section* section);

		/*
		 * Generates all possible combinations of sections
		 */
		std::vector<SectionCombo*>* getCombos() const;

		/*
		 * returns true if this section belongs in this section group based on ID
		 */
		bool validID(const Section* section) const;
		
		std::string 			_id;
		std::vector<Section*> 	_lectures;
		std::vector<Section*>	_labDiscussions;
		friend std::ostream& operator<<(std::ostream& os, const SGLecLBD& group);

};
#endif
