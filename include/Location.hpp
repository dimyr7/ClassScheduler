#ifndef LOCATION_H
#define LOCATION_H
#include <string>
#include <iostream>
/**
 * Represents a location
 * This includes the coordinates ans well as the building name and room number
 */
class Location{
	public: 
		/**
	 	 * Constructor
		 * @param buildingName, defaults to empty string
		 * @param room, the room number as a string, defaults to empty string
		 * @param latitude, defaults to 0
		 * @param longitude, defaults to 0
		 */
		Location(std::string buildingName = "", std::string room = "", double latitude = 0, double longitude = 0);

		/**
		 * Location destructor
		 */
		~Location();

		/**
		 * Copy constructor
		 * @param copy is the Location to copy
		 */
		Location(const Location&  copy);

		/**
		 * Assignment operator
		 * @param copy is the Location to copy
		 * @return this with the new values
		 */
		Location& operator=(const Location& copy);

		/**
		 * Returns the latitude of this location
		 * @return the degrees of latitude
		 */
		double  getLatitude() const;

		/**
		 * Returns the longitude of this location
		 * @return the degrees of longitude
		 */
		double 	getLongitude() const;

		/**
		 * Returns the building name
		 * @return building name
		 */
		std::string 	getBuildingName() const;

		/**
		 * Returns the room number
		 * @return room number
		 */
		std::string  getRoomNumber() const;

		/**
		 * A function to validate the latititude
		 * @param latitutde is the latitude of a location in degrees
		 * @return true if latitude is valid, false otherwise
		 */
		static bool validLatitude(double latitude);

		/**
		 * A function to validate the longitude
		 * @param longitude is the longitude of a location in degrees
		 * @return true if the longitude is valid, false otherwise
		 */
		static bool validLongitude(double longitude);

	private:
	
		/**
		 * Prints the location object to stream include the coordinates and building name and room number
		 * @param os is the stream to write to
		 * @param location to write to stream
		 * @return stream os after writting
		 */
		friend std::ostream& operator<<(std::ostream& os, const Location& location);

		
		/**
		 * Retpresentst the latitude in degrees
		 */
		double _latitude;

		/**
		 * Represents the longitude in degrees
		 */
		double _longitude;

		/**
		 * The building name
		 */
		std::string _buildingName;

		/**
		 * The building room number
		 */
		std::string _roomNumber;

};
#endif
