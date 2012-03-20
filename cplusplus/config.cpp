#include "config.h"

using namespace std;

/**
 * Constructor for the config object.
 */
Config::Config(string fileLoc) {
    this->fileLocation = fileLoc;
    this->loadDataFromFile(fileLoc);
}

/**
 * Destructor for the config object.
 */
Config::~Config() {

}

/**
 * Loads data from the provided settings file.
 */
void Config::loadDataFromFile(string file) {
    this->settings.clear();
    cout << "Loading from " << file << endl;
    ofstream settingsfile;
    settingsfile.open( file.c_str() );
    if( settingsfile.is_open() ) {
        cout << "That shit is open!" << endl;        
    }
    settingsfile.close();
}

/**
 * Gets the value associated with the provided key, assuming one exists.
 */
string Config::getSetting(string key) {
    return this->settings.at(key);
}

/**
 * Sets the provided key to map to the provided value.
 */
void Config::setSetting(string key, string val) {
    this->settings.insert(pair<string,string>(key,val));
}

/**
 * Saves val at the provided key. This method is just intended to make it easier
 * to store integers without having to manually convert them to strings. The
 * value of the integer will be stored as a string.
 */
void Config::setSetting(string key, int val) {
    string s;
    stringstream out;
    out << val;
    s = out.str();
    this->setSetting(key, s);
}

/**
 * Saves the config object to a file.
 */
void Config::save() {
    // TODO: Implement
}

/** Testing **/
int main() {

    Config c( "settings.xml" );

    cout << "Yeah, brah!" << endl;

    return 0;
}
