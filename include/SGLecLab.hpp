#ifndef SGLECLAB_H
#define SGLECLAB_H
#include "SectionGroup.hpp"
class SGLecLab : public Course::SectionGroup{

	public:
		SGLecLab(std::string id);
		SGLecLab(const SGLecLab& copy);
		SGLecLab& operator=(const SGLecLab& copy);
	
		std::vector<Section*>  getLecSections() const;
		std::vector<Section*>  getLabSections() const;

		bool addSection(Section* section);

		std::vector<SectionCombo*> getCombos() const;


	private:
		std::string _id;
		std::vector<Section*> _lectures;
		std::vector<Section*> _labs;
		friend std::ostream& operator<<(std::ostream& os, const SGLecLab& group);

};
#endif
