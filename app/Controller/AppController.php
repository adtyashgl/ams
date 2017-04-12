<?php
/**
 * Application level Controller
 *
 * This file is application-wide controller file. You can put all
 * application-wide controller-related methods here.
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @package       app.Controller
 * @since         CakePHP(tm) v 0.2.9
 * @license       http://www.opensource.org/licenses/mit-license.php MIT License
 */

App::uses('Controller', 'Controller');

/**
 * Application Controller
 *
 * Add your application-wide methods in the class below, your controllers
 * will inherit them.
 *
 * @package		app.Controller
 * @link		http://book.cakephp.org/2.0/en/controllers.html#the-app-controller
 */
class AppController extends Controller {
	public $components = array(
		'Session',
		'Auth' => array(
			'authenticate' => array(
				'Form' => array(
					'passwordHasher' => 'Blowfish'
				),
			)
		)
	);

	public function beforeFilter() {
	}

	/******************************************************************************
	 * Function   : _saveImage
	 * Description: Saves the employee Image 
	 *              The image is saved under a folder named $employeeId/entry folder for entry
	 *              and $employeeId/exit folder for exit with timestamp
	 *
	 * ****************************************************************************/
	protected function _saveImage($employeeId,$action,$timestamp,$srcFilePath,&$savedFilePath)
	{	
		$imagesFolder = Configure::read('EmployeeImagePath');
	        $actionPath = "";
	        if($action == Configure::read('Action.Entry')){
			$actionPath = "entry";
		}else if($action == Configure::read('Action.Exit')){
			$actionPath = "exit";
		}else{
			CakeLog::write('error','[_saveImage]Invalid action supplied ' . $action);
			return false;
		}
		 	
		$folderUrl = WWW_ROOT.$imagesFolder.$employeeId.DS.$actionPath;

		CakeLog::write('debug','[_saveImage]...FolderURL ' . $folderUrl);
		if(!is_dir($folderUrl)){
			mkdir($folderUrl,0777,true);
		}

		$fileName = $folderUrl.DS.$timestamp;
		CakeLog::write('debug','[_saveImage]... FileName ' . $fileName);

		if(copy($srcFilePath,$fileName)){
			$savedFilePath = $fileName;
			return true;
		}else{
			return false;
		}
	}

	//...
}
