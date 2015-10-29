#ifndef SECTIONBUILDER_H
#define SECTIONBUILDER_H
#include "Section.hpp"
#include "Week.hpp"
class Section::SectionBuilder{
	public:
		SectionBuilder();

		Section* buildSection();

		void 	setSectioName(std::string name);
		void 	setSectionType(std::string type);
		void 	setDescription(std::string description);
		void 	setCRN(std::string crn);

		void 	setInstructorName(std::string instructor);

		void 	setStartTime(Section::Week::Day day, int hour, int minute);
		void 	setEndTime(Section::Week::Day day, int hour, int minute);

		void 	setSemesterStart(int day, int month, int year);
		void 	setSemesterEnd(int day, int month, int year);

		void 	setSemsterYear(std::string year);
		void 	setSemesterSeason(std::string season);
		void 	setSemesterName(std::string name);

		void 	setLocationLat(double lat);
		void 	setLocationLon(double lon);
		void 	setLocationBuilding(std::string name);
		void 	setLocationRoom(std::string room);


	private:
		bool 	validateInput();		
		/*
		 * Section Info
		 */
		std::string 	_sectionName;
		std::string 	_sectionType;
		std::string 	_description;
		std::string 	_crn;
		/*
		 * Instructor
		 */
		std::string 	_instructorName;	

		/*
		 * Week
		 * Organized by [monday, tuesday, ...][start, end][hour, minute]
		 */
		std::array<std::array<std::array<int, 2>, 2>, 7> _weekTimes;


		/*
		 * Semester
		 * Organized by [day][month][year]
		 */
		std::array<int, 3> 	_semesterStartDate;
		std::array<int, 3> 	_semesterEndDate;
		std::string 			_semesterYear;
		std::string 			_semesterSeason;
		std::string 			_semesterName;


	
		/*
		 * Location
		 */
		double 	_locationLat;
		double 	_locationLon;
		std::string 	_locationBuildingName;
		std::string 	_locationRoomNumber;
};
#endif
