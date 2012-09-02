<?php

error_reporting(E_ALL | E_STRICT);

require_once "/var/lib/php/esprit/autoloader.php";


$config = esprit\core\Config::createFromJSON("config.json");

$controller = esprit\core\Controller::createController( $config );

$logRecorder = new esprit\core\util\FileLogRecorder("/home/oasis/errors_log", esprit\core\util\Logger::WARNING);
$controller->getLogger()->addLogRecorder( $logRecorder );

if( $config->get("debug") )
{
    $fineRecorder = new esprit\core\util\FileLogRecorder("/home/oasis/debug_log", esprit\core\util\Logger::FINE);
    $controller->getLogger()->addLogRecorder( $fineRecorder );
}

$controller->run();

$controller->close();
