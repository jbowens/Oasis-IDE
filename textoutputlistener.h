#ifndef TEXTOUTPUTLISTENER_H
#define TEXTOUTPUTLISTENER_H

#include <iostream>

/**
 * An interface class for classes that need to listen to output from the
 * Interactions class.
 */
 class TextOutputListener {
    public:
        virtual ~TextOutputListener();
        virtual void receiveOutput( std::string out );
 };

#endif // TEXTOUTPUTLISTENER_H