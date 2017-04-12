<?php

class RestController extends AppController
{
	public $components = array('RequestHandler', 'Paginator');


	public function beforeFilter() {
		parent::beforeFilter();
		$this->Auth->allow('getFranchises','getEmployees');
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

		
		if($role == Configure::read('Role.Owner')){
	           $this->loadModel('Franchise');		
	           $locations = $this->Franchise->getAllFranchises();
		}else if ($role == Configure::read('Role.Manager'){
	           $this->loadModel('Franchise');		
	           $locations = $this->Franchise->getFranchise($userId);
		}else{
		  throw new BadRequestException('Un-Authorized. You are not configured to setup this device',401);
		}

		if(!empty($locations)){
			$response['locations'] = $locations;
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
	public function entry($employeeId,$timestamp)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');

                $employeeImagePath = $this->request->params['form']['image']['tmp_name'];

		$savedPath = "";
		if($this->_saveImage($employeeId,Configure::read('Action.Entry'),$timestamp,
			$employeeImagePath,$savedPath){
				CakeLog::write('error','[entry] _saveImage Failed');	
		}

		$this->loadModel('Attendance');

		if($this->Attendance->logEntry($employeeId,$timestamp,$savedPath){
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "Could not save the record in DB";
		}

		$this->set('data',$response);
		$this->set('_serialize','data');
	}
	
	/*******************************************************************************
	 * URI : POST https://exit
	 *
	 * ****************************************************************************/   
	public function exit($employeeId,$inTimeStamp,$outTimeStamp)
	{
		$response = array();
		$response['status'] = Configure::read('RetValue.Success');

                $employeeImagePath = $this->request->params['form']['image']['tmp_name'];

		$savedPath = "";
		if($this->_saveImage($employeeId,Configure::read('Action.Exit'),$outTimeStamp,
			$employeeImagePath,$savedPath){
				CakeLog::write('error','[exit] _saveImage Failed');	
		}

		$this->loadModel('Attendance');

		if($this->Attendance->logExit($employeeId,$inTimeStamp,$outTimeStamp,$savedPath){
			$response['status'] = Configure::read('RetValue.Failed');
			$response['reason'] = "Could not save the record in DB";
		}

		$this->set('data',$response);
		$this->set('_serialize','data');
	}

}
