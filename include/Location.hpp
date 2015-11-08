#ifndef LOCATION_H
#define LOCATION_H
#include <string>
#include <iostream>
class Location{
	public: 
		/*
	 	 * if the lat or long is not valid, it is set to (0, 0)
		 */
		Location(double latitude = 0, double longitude = 0);
		/*
		 * Location destructor
		 */
		~Location();
		/*
		 * Copy constructor
		 */
		Location(const Location&  copy);
		/*
		 * Assignment operator
		 */
		Location& operator=(const Location& copy);

		/*
		 * getters
		 */
		double  getLatitude() const;
		double 	getLongitude() const;

		std::string 	getBuildingName() const;
		std::string  getRoomNumber() const;

		bool setCoord(double latitude, double longitude);
		bool setLatitude(double latitude);
		bool setLongitude(double longitude);

		void setBuildingName(std::string building);	
		void setRoomNumber(std::string roomNumber);

		/*
		 * validators
		 */
		static bool validLatitude(double latitude);
		static bool validLongitude(double longitude);

		friend std::ostream& operator<<(std::ostream& os, const Location& location);
	private:
		

		/*
		 * setters
		 * Returns true on success
		 * Return false otherwise and doesnt  change anything
		 */
		


		
		/*
		 * data members
		 */
		double _latitude;
		double _longitude;
		std::string _buildingName;
		std::string _roomNumber;

};
#endif
