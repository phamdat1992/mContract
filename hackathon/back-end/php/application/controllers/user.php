<?php

/*
 * Created by :
 * Pham Huu Thanh Dat | dat.pham@zalora.com
 */

if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

session_start();

class User extends CI_Controller
{
    public function __construct()
    {
        parent:: __construct();
        $this->load->model(
            [
                'user_model'
            ]
        );
    }

    public function get_user()
    {
        $data = $this->user_model->get_user($_GET['email']);

        if (count($data) == 1) {
            $data = reset($data);
            echo json_encode($data);
        }
    }

    public function add_user()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) >= 1) {
            return;
        }

        $this->user_model->add_user($_GET['email']);
        echo json_encode(
            [
                'status' => 'success'
            ]
        );
    }

    public function add_user_info()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) === 1) {
            $user = reset($user);
            if (count($this->user_model->get_user_info($user["id_icontract_user"] == 0))) {
                $data = [
                    'fk_icontract_user' => $user["id_icontract_user"],
                    'company_name' => $_POST['company_name'],
                    'company_code' => $_POST['company_code'],
                    'company_mst' => $_POST['company_mst'],
                    'company_address' => $_POST['company_address'],
                    'company_phone' => $_POST['company_phone'],
                    'user_name' => $_POST['user_name'],
                    'user_cmnd' => $_POST['user_cmnd'],
                    'user_birthday' => $_POST['user_birthday'],
                    'user_cmnd_beginning_date' => $_POST['user_cmnd_beginning_date'],
                    'user_cmnd_address' => $_POST['user_cmnd_address']
                ];

                $this->user_model->add_user_info($data);
                echo json_encode(
                    [
                        'status' => 'success'
                    ]
                );
            }
        }
    }

    public function get_user_info()
    {
        $user = $this->user_model->get_user($_GET['email']);

        if (count($user) === 1) {
            $user = reset($user);
            echo json_encode(
                $this->user_model->get_user_info($user["id_icontract_user"])
            );
        }
    }
}
