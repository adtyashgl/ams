<?php
// app/Model/User.php
App::uses('AppModel', 'Model');

class Franchise extends AppModel {

	public function getAllFranchises(){

		$franchises = $this->find('all');

		if(!empty($franchises)){
			CakeLog::write('debug','Inside franchises...' . print_r($franchises,true));
		}
		return $franchises;

	}

	public function getFranchiseManagedBy($managerId){
		$franchises = $this->find('all',
			                   array('conditions' => array('Franchise.manager_id' => $managerId))
			                 );

		if(!empty($franchises)){
			CakeLog::write('debug','Inside franchises...' . print_r($franchises,true));
		}
		return $franchises;


	}


}
