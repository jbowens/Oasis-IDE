#include <iostream>
#include "interactions.h"

using std::vector;

/**
 * Constructor.
 *
 * Takes in a config object.
 */
Interactions::Interactions(Config &config) : c(config) {
}

/**
 * Destructor
 */
Interactions::~Interactions() {
}

/**
 * Resets the interactions controller, restarting the REPL and using the
 * definitions in the provided .ml file.
 */ 
void Interactions::reset(std::string filePath) {
  this->definitionsFile = filePath;
}

/**
 * Adds an output listener to be notified with any output from the 
 * Interactions window.
 */
void Interactions::registerOutputListener(TextOutputListener *o) {
  for( vector<TextOutputListener>::size_type i  = 0; i < this->listeners.size(); i++ ) {
    if( this->listeners[i] == o )
        return;
  }
  /* This listener isn't listening to output yet, so we can add safely add it. */
  this->listeners.push_back(o);
}

/**
 * Removes an output listner so that it's no longer notified about
 * new output.
 */
void Interactions::removeOutputListener(TextOutputListener *o) {
  vector<TextOutputListener*>::iterator i = this->listeners.begin();
  for( ; i < this->listeners.end(); i++ ) {
    if( *i == o ) {
      (this->listeners).erase(i);
      return;
    }
  }
}

/**
 * Takes a character inputted by the user, and passes it on to the
 * OCaml REPL.
 */
void Interactions::processUserInput(char c) {

}
