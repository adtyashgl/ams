<?php

class RestController extends AppController
{
	public $components = array('RequestHandler', 'Paginator');


	public function beforeFilter() {
		parent::beforeFilter();
		$this->Auth->allow('getFranchises','getEmployees','logEntry','logExit');
                $this->RequestHandler->ext = 'json';
	}


	/******************************************************************************
	 * URI: GET https://franchises/<userId>
	 *
	 * ****************************************************************************/

	public function getFranchises($userId)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');
		
		$this->loadModel('User');
                $role = $this->User->getRole($userId);

	        CakeLog::write('debug','[getFranchises] Role is ' . $role);	
		if($role == Configure::read('Role.Owner')){
	           $this->loadModel('Franchise');		
	           $locations = $this->Franchise->getAllFranchises();
		}else if ($role == Configure::read('Role.Manager')){
	           $this->loadModel('Franchise');		
	           $locations = $this->Franchise->getFranchise($userId);
		}else{
		  throw new BadRequestException('Un-Authorized. You are not configured to setup this device',401);
		}

		if(!empty($locations)){
			$response['locations'] = $locations;
			$response['count'] = count($locations);
		}else{
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "No franchises configured. Please contact Administrator";
		}
		
		$this->set('data',$response);
		$this->set('_serialize','data');
	}	


	/*******************************************************************************
	 * URI : GET https://employees/<franchiseId> 
	 *
	 * ****************************************************************************/   
	public function getEmployees($franchiseId)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');
		
		$employees = array();

		$this->loadModel('Employee');
		$employees = $this->Employee->getEmployees($franchiseId);

		if(!empty($employees)){
			$response['employees'] = $employees;
			$response['count']     = count($employees);
		}else{
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "No Employees configured for this Franchise. Please contact Administrator";
		}

		$this->set('data',$response);
		$this->set('_serialize','data');
	}
	
	/*******************************************************************************
	 * URI : POST https://entry
	 *
	 * ****************************************************************************/   
	public function logEntry($employeeId,$timestamp)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');

		CakeLog::write('debug','[logEntry] Printing request ' . print_r($this->request,true));
                $employeeImagePath = $this->request->params['form']['image']['tmp_name'];

		$savedPath = "";
		if(!$this->_saveImage($employeeId,Configure::read('Action.Entry'),$timestamp,
			$employeeImagePath,$savedPath)){
				CakeLog::write('error','[entry] _saveImage Failed');	
		}

		$this->loadModel('Attendance');

		if(!$this->Attendance->logEntry($employeeId,$timestamp,$savedPath)){
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "Could not save the record in DB";
		}
		
		CakeLog::write('debug','[logEntry] Data response ' . print_r($data,true));


		$this->set('data',$response);
		$this->set('_serialize','data');
	}
	
	/*******************************************************************************
	 * URI : POST https://exit
	 *
	 * ****************************************************************************/   
	public function logExit($employeeId,$inTimeStamp,$outTimeStamp)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');
		CakeLog::write('debug','[logExit] Printing request ' . print_r($this->request,true));

                $employeeInImagePath  = $this->request->params['form']['inImage']['tmp_name'];
                $employeeOutImagePath = $this->request->params['form']['outImage']['tmp_name'];

		$savedOutPath = "";
		if(!$this->_saveImage($employeeId,Configure::read('Action.Exit'),$outTimeStamp,
			$employeeOutImagePath,$savedOutPath)){
				CakeLog::write('error','[logExit] _saveImage Exit Image Failed');	
		}
		
		$savedInPath = "";
		if(!$this->_saveImage($employeeId,Configure::read('Action.Entry'),$inTimeStamp,
			$employeeInImagePath,$savedInPath)){
				CakeLog::write('error','[exit] _saveImage Entry Image Failed');	
		}

		$this->loadModel('Attendance');

		if(!$this->Attendance->logExit($employeeId,$inTimeStamp,$outTimeStamp,$savedInPath,$savedOutPath)){
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "Could not save the record in DB";
		}

		$this->set('data',$response);
		$this->set('_serialize','data');
	}

}
