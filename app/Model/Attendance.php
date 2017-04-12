<?php
// app/Model/User.php
App::uses('AppModel', 'Model');

class Attendance extends AppModel {


	public function logEntry($employeeId,$timestamp,$savedPath){
          $attendance = array();
	  $attendance['Attendance']['employee_id']  = $employeeId;
	  $attendance['Attendance']['in_time']      = $timestamp;
	  $attendance['Attendance']['in_time_pic']  = $savedPath;
	  $attendance['Attendance']['created']      = date('Y-m-d');
	  $attendance['Attendance']['modified']     = date('Y-m-d');

	  if($this->save($attendance)){
		  return true;
	  }else{
		  return false;
	  }

	}
	
	public function logExit($employeeId,$inTimeStamp,$outTimeStamp,$inSavedPath,$outSavedPath){

		$record = $this->find('first',
			array('conditions' => array('Attendance.employee_id' => $employeeId,
			'Attendance.in_time' => $inTimeStamp)
		));
		
		if(empty($record)){
			$attendance = array();
			$attendance['Attendance']['employee_id']  = $employeeId;
			$attendance['Attendance']['in_time']      = $inTimeStamp;
			$attendance['Attendance']['in_time_pic']  = $inSavedPath;
			$attendance['Attendance']['out_time']     = $outTimeStamp;
			$attendance['Attendance']['out_time_pic'] = $outSavedPath;
			$attendance['Attendance']['created']      = date('Y-m-d');
			$attendance['Attendance']['modified']     = date('Y-m-d');

			if($this->save($attendance)){
				return true;
			}else{
				return false;
			}
		}else{
			$record['Attendance']['out_time']     = $outTimeStamp;
			$record['Attendance']['out_time_pic'] = $outSavedPath;
			$record['Attendance']['modified']     = date('Y-m-d');

			if($this->save($record)){
				return true;
			}else{
				return false;
			}
		}
	}


}
