#ifndef LOCATION_H
#define LOCATION_H
#include <string>
namespace CourseInfo{
using std::string;
class Location{
	public:
		/*
		 * if the lat or long is not valid, it is set to (0, 0)
		 */
		Location(double latitude, double longitude);
		~Location();

		bool setCoord(double latitude, double longitude);
		bool setLatitude(double latitude);
		bool setLongitude(double longitude);

		void setBuildingName(string building);	
		void setRoomNumber(string roomNumber);

		double  getLatitude();
		double 	getLongitude();

		string 	getBuildingName();
		string  getRoomNumber();

		bool validLatitude(double latitude);
		bool validLongitude(double longitude);
	private:
		double _latitude;
		double _longitude;
		string _buildingName;
		string _roomNumber;

};
}
#endif
