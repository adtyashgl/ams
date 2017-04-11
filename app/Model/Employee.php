<?php
// app/Model/User.php
App::uses('AppModel', 'Model');

class Employee extends AppModel {

	public function getEmployees($franchiseId){

		$employees = $this->find('all',
			                  array('conditions' => array('Employee.franchise_id' => $franchiseId))
		                        );
			           

		if(!empty($employees)){
			CakeLog::write('debug','Inside employees...' . print_r($employees,true));
		}
		return $employees;

	}
}
