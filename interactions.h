#ifndef INTERACTIONS_H
#define INTERACTIONS_H

#include "config.h"

/**
 * The Interactions class handles interfacing with the OCaml REPL.
 */
class Interactions {
    private:
        Config &c;

    public:
        Interactions(Config &c);
        ~Interactions();
};

#endif // INTERACTIONS_H