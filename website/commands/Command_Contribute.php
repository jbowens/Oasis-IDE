<?php

namespace oasis\commands;

use \esprit\core\BaseCommand;
use \esprit\core\Request;
use \esprit\core\Response;

/**
 * @author jbowens
 * @since September 2012
 */ 
class Command_Contribute extends BaseCommand {

    const CMD_NAME = "Contribute";

    public function getName() {
        return self::CMD_NAME;
    }

    public function run( Request $request, Response $response )
    {
        return $response;
    }

}
