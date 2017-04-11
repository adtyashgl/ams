<?php
// app/Controller/UsersController.php
App::uses('AppController', 'Controller');

class UsersController extends AppController {
	public $components = array('RequestHandler');

	public function beforeFilter() {
		parent::beforeFilter();
		$this->Auth->allow('add','logout','appLogin','test');
	}

	public function index() {
		$this->User->recursive = 0;
		$this->set('users', $this->paginate());
	}

	public function view($id = null) {
		$this->User->id = $id;
		if (!$this->User->exists()) {
			throw new NotFoundException(__('Invalid user'));
		}
		$this->set('user', $this->User->findById($id));
	}

	public function add() {
		if ($this->request->is('post')) {
			$this->User->create();
			CakeLog::write('debug','Inside add ... ' . print_r($this->request->data,true));
			if ($this->User->save($this->request->data)) {
				$this->Session->setFlash(__('The user has been saved'));
				return $this->redirect(array('action' => 'index'));
			}
			$this->Session->setFlash(
				__('The user could not be saved. Please, try again.')
			);
		}
	}


	public function edit($id = null) {
		$this->User->id = $id;
		if (!$this->User->exists()) {
			throw new NotFoundException(__('Invalid user'));
		}
		if ($this->request->is('post') || $this->request->is('put')) {
			if ($this->User->save($this->request->data)) {
				$this->Session->setFlash(__('The user has been saved'));
				return $this->redirect(array('action' => 'index'));
			}
			$this->Session->setFlash(
				__('The user could not be saved. Please, try again.')
			);
		} else {
			$this->request->data = $this->User->findById($id);
			unset($this->request->data['User']['password']);
		}
	}

	public function delete($id = null) {
		// Prior to 2.5 use
		// $this->request->onlyAllow('post');

		$this->request->allowMethod('post');

		$this->User->id = $id;
		if (!$this->User->exists()) {
			throw new NotFoundException(__('Invalid user'));
		}
		if ($this->User->delete()) {
			$this->Session->setFlash(__('User deleted'));
			return $this->redirect(array('action' => 'index'));
		}
		$this->Session->setFlash(__('User was not deleted'));
		return $this->redirect(array('action' => 'index'));
	}

	public function login() {
		if ($this->request->is('post')) {
			if ($this->Auth->login()) {
				return $this->redirect($this->Auth->redirectUrl());
			}
			$this->Session->setFlash(__('Invalid username or password, try again'));
		}
	}
	

	public function logout() {
		return $this->redirect($this->Auth->logout());
	}
	/**********************************************************************
	 *   App related functions
	 *  
	 *
	 * ********************************************************************/
	
	public function appLogin() {
		$this->request->allowMethod('post','put');

		if(!empty($this->request->data))
		{
			//CakeLog::write('debug','Printing Operator ' . print_r($this->request,true));
			$this->request->data['User']['username'] = $this->request->data['username'];
			$this->request->data['User']['password'] = $this->request->data['password'];
			//CakeLog::write('debug','Printing Operator(AFTER) ' . print_r($this->request,true));

			if($this->Auth->login())
			{
				$this->response->statusCode(200);
				CakeLog::write('debug',"Logged in with User ID : " . CakeSession::read('Auth.User.id'));
				$userId = CakeSession::read('Auth.User.id');
				$this->response->header(array("UserID"=> $userId));

			}
			else{
				CakeLog::write('debug','Inside ...else');
				$this->response->statusCode(401);
			}


		}else{
			$this->response->statusCode(400);
		}

		$this->autoRender = false;	

	}

	public function test(){

          $this->loadModel('User');
	  $this->User->getRole(1);

	  $this->loadModel('Franchise');
	  $this->Franchise->getAllFranchises();
	  $this->Franchise->getFranchiseManagedBy(1);

	  $this->loadModel('Employee');
	  $this->Employee->getEmployees(1);

	}

}
