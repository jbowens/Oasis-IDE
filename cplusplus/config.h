#ifndef CONFIG_H
#define CONFIG_H

#include <iostream>
#include <fstream>
#include <map>
#include <sstream>

/**
 * The config class handles all settings for the application that need to persist between
 * sessions. Settings are stored as XML between sessions.
 */
class Config {
    private:
        std::string fileLocation;
        std::map<std::string, std::string> settings;
        void loadDataFromFile(std::string file);

    public:
        Config(std::string fileLoc);
        ~Config();
        std::string getSetting(std::string key);
        void setSetting(std::string key, std::string val);
        void setSetting(std::string key, int val);
        void save();
};

#endif // CONFIG_H