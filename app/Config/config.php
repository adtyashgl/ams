<?php

	include ROOT."/ConfigrationLibrary.php";
	$config['AppDefaultDatabase'] = ConfigrationLibrary::getConfigVars('database');
	$config['AppDefaultEnvironment'] = ConfigrationLibrary::ENV;
	$config['AppDefaultDatabaseDefault'] = ConfigrationLibrary::DATABASEDEFAULT;
	$config['ROOTURL'] = ConfigrationLibrary::getConfigVars('RootUrl');

	$config['RetValue'] = array('Success' => 0,
		                     'Failure' =>1);

	$config['Role'] = array('NA' => 0,
		                'Owner' => 1,
		                'Manager' => 2);
?>
