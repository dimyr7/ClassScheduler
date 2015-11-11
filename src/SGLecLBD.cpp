#include "SectionCombo.hpp"
#include "SGLecLBD.hpp"
/*
 * Object Creation
 */
SGLecLBD::SGLecLBD(std::string id){
	this->_id = id;	
}

/*
 * Adds a new section to the correct section type e.g. Lecture or Lab/Dis
 */
bool 	SGLecLBD::addSection(Section* section){
	std::string type = section->getSectionType();
	/*
	 * If the section is a lecture and has the correct identifier
	 */

	if(type.compare("Lecture") == 0 && this->validID(section)){
		this->_lectures.push_back(section);
		return true;
	}
	/*
	 * If the section is a LBD and has teh correct identifier 
	 */
	else if(type.compare("Laboratory-Discussion") == 0 && this->validID(section)){
		this->_labDiscussions.push_back(section);
		return true;
	}

	/*
	 * This section doesnt belong here
	 */
	return false;
}


std::vector<SectionCombo*> SGLecLBD::getCombos() const{
	std::vector<SectionCombo*> combos;
	/*
	 * For all possilbr lectures
	 */
	//for(std::vector<Section*>::const_iterator it = this->_lectures.begin(); it != this->_lectures.end(); it++){
	for(int i = 0; i < (int)this->_lectures.size(); i++){
		//Section* lecture = *it;
		Section* lecture = this->_lectures[i];
		/*
		 * For all possible lab-dis 
		 */
		//for(std::vector<Section*>::const_iterator is = this->_labDiscussions.begin(); is != this->_labDiscussions.end(); is++){
		for(int j = 0; j < (int)this->_labDiscussions.size(); j++){
			//Section* labdis = *is;	
			Section* labdis = this->_labDiscussions[j];

			/*
			 * If the lectures & lab-dis overlap, then its not a valid combination
			 */
			if(Section::overlap(lecture, labdis)){
				continue;
			}
			/*
			 * Otherwise it is valid
			 */
			SectionCombo* sectionCombo = new SectionCombo();
			sectionCombo->addSection(lecture);
			sectionCombo->addSection(labdis);
			combos.push_back(sectionCombo);
		}
		
	}
	return combos;
}

std::vector<Section*> SGLecLBD::getLectures()const {
	return this->_lectures;
}

std::vector<Section*> SGLecLBD::getLabDis() const{
	return this->_labDiscussions;
}

bool SGLecLBD::validID(const Section* section) const{
	/*
	 * Checks if the first character of a section name is the same as the this's id
	 */
	std::string id = section->getSectionName().substr(0, 1);

	return (this->_id.compare(id) == 0);
}


std::ostream& operator<<(std::ostream& os, const SGLecLBD& group){
	os << "=== SectioGroup Lec-LBD ===" << std::endl;
	os << "=== Lectures ===" << std::endl;
	/*
	 * Print all lecture section names
	 */
	for(std::vector<Section*>::const_iterator it = group.getLectures().begin(); it != group.getLectures().end(); it++){
		os << (*it)->getSectionName() << std::endl;
	}

	/*
	 * Print all Lab-Dis section names
	 */
	os << "=== Lab/Discussion ===" <<std::endl;
	for(std::vector<Section*>::const_iterator it = group.getLabDis().begin(); it != group.getLabDis().end(); it++){
		os << (*it)->getSectionName() << std::endl;
	}
	return os;
}
