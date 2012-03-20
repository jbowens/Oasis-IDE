#ifndef INTERACTIONS_H
#define INTERACTIONS_H

#include <vector>
#include "config.h"
#include "textoutputlistener.h"

/**
 * The Interactions class handles interfacing with the OCaml REPL.
 */
class Interactions {
    private:
        Config &c;
        std::string definitionsFile;
        std::vector<TextOutputListener*> listeners;

    public:
        Interactions(Config &c);
        ~Interactions();
        void reset(std::string filePath);
        void registerOutputListener(TextOutputListener *o);
        void removeOutputListener(TextOutputListener *o);
        void processUserInput( char c );
};

#endif // INTERACTIONS_H
