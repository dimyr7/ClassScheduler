#include "Course/Course.hpp"
#include "Course/CourseStore.hpp"
#include "Course/CourseGroup.hpp"
#include "Course/Schedule.hpp"
#include "Communication/CourseFiller.hpp"

#include <sys/resource.h>
#include <sys/types.h>
#include <sys/time.h>
#include <string>




int main(){
	CourseStore* store = new CourseStore();
	std::cout << "Starting filling course store" << std::endl;
	CourseFiller::fill(store);
	std::cout << "Finished filling" << std::endl;

	Course* cs125 = store->get("CS125");
	Course* cs173 = store->get("CS173");

	CourseGroup g(2);
	g.addCourse(cs125);
	g.addCourse(cs173);

	std::vector<Schedule*> schedules = g.genSchedules();
	std::vector<SectionCombo*>  aSchedule = schedules[0]->getCombos();
	std::cout << "Done" << std::endl;
	for(std::vector<SectionCombo*>::const_iterator it = aSchedule.begin(); it != aSchedule.end(); it++){
		SectionCombo* oneCombo = *it;
		std::vector<Section*> sections = oneCombo->getSections();
		for(std::vector<Section*>::const_iterator is = sections.begin(); is != sections.end(); is++){
			Section* section = *is;
			std::cout << section->getCRN() << std::endl;
		}
	}

	return 0;
}



