<?php
class ConfigrationLibrary{

    const ENV = 'local';
    const DATABASEDEFAULT = 'ams';
    const VERSION = '1.0';

    //static constants are set here
    public static function ConfigConstants(&$ConfigVars){ 
        $ConfigVars['database'] = array(
                                            'datasource' => 'Database/Mysql',
                                            'persistent' => false,
                                            'host' => '127.0.0.1',
                                            'port'=>3306,
                                            'login' => 'root',
                                            #'password'=>'ypZMJfYcuO/5',
                                            'password'=> 'aditya07',
                                            'database' => self::DATABASEDEFAULT,
                                            'prefix' => ''
                                );
        //checking that it is runnig locally or not.
        $ConfigVars['RootUrl'] = ($_SERVER["REMOTE_ADDR"]=="127.0.0.1") ? 'http://localhost/simplypark/' : 'http://127.0.0.1/simplypark/';

    }

    //usage: ConfigrationLibrary::vars('someVar')
    public static function getConfigVars($v) {
       	static $ConfigVars; 
        if (!is_array($ConfigVars)) { $ConfigVars = array(); ConfigrationLibrary::ConfigConstants($ConfigVars); } //setup access to the static contants in our Final library 
        return $ConfigVars[$v]; //return some var 
    }
}
