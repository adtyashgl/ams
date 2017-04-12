<?php
// app/Model/User.php
App::uses('AppModel', 'Model');
App::uses('BlowfishPasswordHasher', 'Controller/Component/Auth');

class User extends AppModel {
	public $belongsTo = 'Employee';
	public $validate = array(
		'username' => array(
			'required' => array(
				'rule' => 'notBlank',
				'message' => 'A username is required'
			)
		),
		'password' => array(
			'required' => array(
				'rule' => 'notBlank',
				'message' => 'A password is required'
			)
		)	
	);

	public function beforeSave($options = array()) {
		if (isset($this->data[$this->alias]['password'])) {
			$passwordHasher = new BlowfishPasswordHasher();
			$this->data[$this->alias]['password'] = $passwordHasher->hash(
				$this->data[$this->alias]['password']
			);
		}
		return true;
	}

	public function getRole($userId){

		$role = $this->find('first',
			array('conditions' => array('User.id' => $userId),
			'contain' => array(
				'Employee' => array('fields'=> array('id')))
		             )
	                );

		if(!empty($role)){
			CakeLog::write('debug','[getRole] : Role Array' . print_r($role,true));
			return $role['Employee']['role_id'];
		}else{
			return Configure::read('Role.NA');
		}


	}


}
